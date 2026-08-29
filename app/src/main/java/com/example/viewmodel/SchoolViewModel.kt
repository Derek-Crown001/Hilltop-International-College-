package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SchoolRepository
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CbtActiveSession(
    val exam: CbtExam,
    val currentQuestionIndex: Int = 0,
    val answersMap: Map<Int, Int> = emptyMap(),
    val remainingSeconds: Int,
    val isFinished: Boolean = false,
    val result: CbtResult? = null
)

class SchoolViewModel(
    private val repository: SchoolRepository = SchoolRepository()
) : ViewModel() {

    val currentRole = repository.currentRole
    val studentReportCards = repository.studentReportCards
    val feeInvoices = repository.feeInvoices
    val paymentReceipts = repository.paymentReceipts
    val cbtExams = repository.cbtExams
    val cbtResults = repository.cbtResults
    val assignments = repository.assignments
    val attendanceEntries = repository.attendanceEntries
    val newsArticles = repository.newsArticles
    val admissions = repository.admissions
    val announcements = repository.announcements
    val hostelRooms = repository.hostelRooms
    val transportRoutes = repository.transportRoutes
    val libraryBooks = repository.libraryBooks

    // UI Navigation & Tab State
    private val _selectedTab = MutableStateFlow("home")
    val selectedTab: StateFlow<String> = _selectedTab.asStateFlow()

    // Active Child Selected in Parent Portal
    private val _selectedChildId = MutableStateFlow("STU-001")
    val selectedChildId: StateFlow<String> = _selectedChildId.asStateFlow()

    // Active Report Card for Modal / Inspection
    private val _viewingReportCard = MutableStateFlow<StudentReportCard?>(null)
    val viewingReportCard: StateFlow<StudentReportCard?> = _viewingReportCard.asStateFlow()

    // Active Receipt for View Modal
    private val _viewingReceipt = MutableStateFlow<PaymentReceipt?>(null)
    val viewingReceipt: StateFlow<PaymentReceipt?> = _viewingReceipt.asStateFlow()

    // Active Invoice for Payment Flow
    private val _payingInvoice = MutableStateFlow<FeeInvoice?>(null)
    val payingInvoice: StateFlow<FeeInvoice?> = _payingInvoice.asStateFlow()

    // CBT Active Session State
    private val _cbtSession = MutableStateFlow<CbtActiveSession?>(null)
    val cbtSession: StateFlow<CbtActiveSession?> = _cbtSession.asStateFlow()
    private var cbtTimerJob: Job? = null

    // Notifications state
    private val _showNotificationsSheet = MutableStateFlow(false)
    val showNotificationsSheet: StateFlow<Boolean> = _showNotificationsSheet.asStateFlow()

    // Toast message trigger
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun setRole(role: UserRole) {
        repository.setRole(role)
        _selectedTab.value = when (role) {
            UserRole.GUEST -> "home"
            UserRole.STUDENT -> "dashboard"
            UserRole.PARENT -> "overview"
            UserRole.TEACHER -> "scores"
            UserRole.ADMIN -> "analytics"
        }
    }

    fun selectTab(tab: String) {
        _selectedTab.value = tab
    }

    fun selectChild(childId: String) {
        _selectedChildId.value = childId
    }

    fun openReportCard(report: StudentReportCard) {
        _viewingReportCard.value = report
    }

    fun closeReportCard() {
        _viewingReportCard.value = null
    }

    fun openReceipt(receipt: PaymentReceipt) {
        _viewingReceipt.value = receipt
    }

    fun closeReceipt() {
        _viewingReceipt.value = null
    }

    fun startPaymentFlow(invoice: FeeInvoice) {
        _payingInvoice.value = invoice
    }

    fun dismissPaymentFlow() {
        _payingInvoice.value = null
    }

    fun completePayment(invoiceId: String, amount: Long, gateway: String, channel: String, payerName: String) {
        val receipt = repository.submitPayment(invoiceId, amount, gateway, channel, payerName)
        _payingInvoice.value = null
        _viewingReceipt.value = receipt
        showToast("Payment of ₦${"%,d".format(amount)} via $gateway was successful!")
    }

    // CBT Functions
    fun startCbtExam(exam: CbtExam) {
        cbtTimerJob?.cancel()
        _cbtSession.value = CbtActiveSession(
            exam = exam,
            currentQuestionIndex = 0,
            answersMap = emptyMap(),
            remainingSeconds = exam.durationMinutes * 60,
            isFinished = false,
            result = null
        )

        cbtTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val current = _cbtSession.value ?: break
                if (current.isFinished) break
                if (current.remainingSeconds <= 1) {
                    submitCbtExam()
                    break
                } else {
                    _cbtSession.value = current.copy(remainingSeconds = current.remainingSeconds - 1)
                }
            }
        }
    }

    fun selectCbtAnswer(questionId: Int, optionIndex: Int) {
        val current = _cbtSession.value ?: return
        val updatedMap = current.answersMap.toMutableMap()
        updatedMap[questionId] = optionIndex
        _cbtSession.value = current.copy(answersMap = updatedMap)
    }

    fun nextCbtQuestion() {
        val current = _cbtSession.value ?: return
        if (current.currentQuestionIndex < current.exam.questions.size - 1) {
            _cbtSession.value = current.copy(currentQuestionIndex = current.currentQuestionIndex + 1)
        }
    }

    fun prevCbtQuestion() {
        val current = _cbtSession.value ?: return
        if (current.currentQuestionIndex > 0) {
            _cbtSession.value = current.copy(currentQuestionIndex = current.currentQuestionIndex - 1)
        }
    }

    fun jumpToCbtQuestion(index: Int) {
        val current = _cbtSession.value ?: return
        if (index in 0 until current.exam.questions.size) {
            _cbtSession.value = current.copy(currentQuestionIndex = index)
        }
    }

    fun submitCbtExam() {
        cbtTimerJob?.cancel()
        val current = _cbtSession.value ?: return
        val result = repository.submitCbtExam(
            current.exam.examId,
            "Enrolled Scholar",
            current.answersMap
        )
        _cbtSession.value = current.copy(
            isFinished = true,
            result = result
        )
        showToast("CBT Exam Submitted! Score: ${result.score}/${result.totalQuestions} (${String.format("%.1f", result.percentage)}%)")
    }

    fun exitCbtSession() {
        cbtTimerJob?.cancel()
        _cbtSession.value = null
    }

    fun createCbtExam(exam: CbtExam) {
        repository.createCbtExam(exam)
        showToast("New CBT Exam '${exam.title}' published successfully!")
    }

    fun deleteCbtExam(examId: String) {
        repository.deleteCbtExam(examId)
        showToast("CBT Exam removed.")
    }

    // Teacher & Academic actions
    fun updateSubjectScore(reportId: String, subjectCode: String, ca1: Double, ca2: Double, test: Double, exam: Double) {
        repository.updateSubjectScore(reportId, subjectCode, ca1, ca2, test, exam)
        showToast("Score updated successfully for $subjectCode")
    }

    fun createReportCard(report: StudentReportCard) {
        repository.createReportCard(report)
        showToast("Student report card registered for ${report.studentName}")
    }

    fun toggleAttendance(studentId: String) {
        repository.toggleAttendance(studentId)
    }

    fun addAttendanceStudent(studentName: String, admissionNo: String, className: String) {
        val entry = AttendanceEntry(
            studentId = "STU-${(100..999).random()}",
            studentName = studentName,
            admissionNo = admissionNo,
            isPresent = true,
            remarks = "Present on time ($className)"
        )
        repository.addAttendanceEntry(entry)
        showToast("Student $studentName added to attendance roster")
    }

    fun submitAssignment(assignmentId: String, solution: String) {
        repository.submitAssignmentSolution(assignmentId, solution)
        showToast("Assignment solution submitted successfully!")
    }

    fun createAssignment(title: String, subject: String, targetClass: String, dueDate: String, desc: String, maxScore: Int) {
        repository.createAssignment(title, subject, targetClass, dueDate, desc, maxScore)
        showToast("New assignment published for $targetClass")
    }

    // Admission actions
    fun submitNewAdmission(app: AdmissionApplication) {
        repository.submitNewAdmission(app)
        showToast("Application #${app.applicationNo} registered successfully!")
    }

    fun updateAdmissionStatus(appNo: String, newStatus: String) {
        repository.updateAdmissionStatus(appNo, newStatus)
        showToast("Admission status updated to: $newStatus")
    }

    fun deleteAdmission(appNo: String) {
        repository.deleteAdmission(appNo)
        showToast("Admission file removed")
    }

    // News & Announcement actions
    fun addNewsArticle(title: String, category: String, author: String, summary: String, content: String) {
        val article = NewsArticle(
            id = "NEWS-${(100..999).random()}",
            title = title,
            category = category,
            date = "Today",
            author = author,
            summary = summary,
            fullContent = content
        )
        repository.addNewsArticle(article)
        showToast("News bulletin '$title' published to website!")
    }

    fun deleteNewsArticle(id: String) {
        repository.deleteNewsArticle(id)
        showToast("News bulletin deleted.")
    }

    fun broadcastAnnouncement(title: String, audience: String, message: String, isUrgent: Boolean) {
        repository.broadcastAnnouncement(title, audience, message, isUrgent)
        showToast("Announcement broadcasted to $audience")
    }

    fun deleteAnnouncement(id: String) {
        repository.deleteAnnouncement(id)
        showToast("Announcement deleted.")
    }

    // Invoices & Finance
    fun createFeeInvoice(
        studentName: String,
        admissionNo: String,
        className: String,
        term: String,
        session: String,
        dueDate: String,
        items: List<FeeItem>
    ) {
        val total = items.sumOf { it.amount }
        val invoice = FeeInvoice(
            invoiceId = "HIC-INV-2026-${(1000..9999).random()}",
            studentId = admissionNo.ifBlank { "STU-${(100..999).random()}" },
            studentName = studentName,
            className = className,
            title = "$term Tuition & Levies ($session)",
            term = term,
            session = session,
            items = items,
            totalAmount = total,
            amountPaid = 0L,
            dueDate = dueDate,
            status = PaymentStatus.UNPAID
        )
        repository.createFeeInvoice(invoice)
        showToast("Fee invoice issued for $studentName (₦${"%,d".format(total)})")
    }

    fun recordManualPayment(
        invoiceId: String,
        studentName: String,
        admissionNo: String,
        amountPaid: Long,
        payerName: String,
        channel: String
    ) {
        val receipt = PaymentReceipt(
            receiptNumber = "HIC/REC/2026/${(1000..9999).random()}",
            transactionRef = "MAN_TXN_${System.currentTimeMillis().toString().takeLast(8)}",
            invoiceId = invoiceId,
            studentName = studentName,
            admissionNo = admissionNo,
            amountPaid = amountPaid,
            paymentDate = "Today",
            paymentGateway = "Bank Transfer / Cash Desk",
            channel = channel,
            payerName = payerName,
            status = "Successful"
        )
        repository.recordPaymentReceipt(receipt)
        showToast("Manual receipt generated for $studentName (₦${"%,d".format(amountPaid)})")
    }

    // Hostel Management
    fun addHostelRoom(roomNumber: String, hallName: String, capacity: Int, floor: String, prefectName: String) {
        val room = HostelRoom(
            roomNumber = roomNumber,
            hallName = hallName,
            capacity = capacity,
            occupied = 0,
            floor = floor,
            prefectName = prefectName
        )
        repository.addHostelRoom(room)
        showToast("Hostel room $roomNumber added to $hallName")
    }

    fun deleteHostelRoom(roomNumber: String) {
        repository.deleteHostelRoom(roomNumber)
        showToast("Hostel room removed.")
    }

    // Transport Management
    fun addTransportRoute(
        routeCode: String,
        routeName: String,
        busNumber: String,
        driverName: String,
        driverPhone: String,
        pickupPoints: List<String>,
        departureTime: String,
        feePerTerm: Long
    ) {
        val route = TransportRoute(
            routeCode = routeCode,
            routeName = routeName,
            busNumber = busNumber,
            driverName = driverName,
            driverPhone = driverPhone,
            pickupPoints = pickupPoints,
            departureTime = departureTime,
            feePerTerm = feePerTerm
        )
        repository.addTransportRoute(route)
        showToast("Bus route $routeCode ($routeName) created successfully")
    }

    fun deleteTransportRoute(routeCode: String) {
        repository.deleteTransportRoute(routeCode)
        showToast("Transport route removed.")
    }

    // Library Management
    fun addLibraryBook(
        title: String,
        author: String,
        isbn: String,
        category: String,
        shelfLocation: String,
        totalCopies: Int
    ) {
        val book = LibraryBook(
            id = "BK-${(100..999).random()}",
            title = title,
            author = author,
            isbn = isbn,
            category = category,
            shelfLocation = shelfLocation,
            totalCopies = totalCopies,
            availableCopies = totalCopies
        )
        repository.addLibraryBook(book)
        showToast("Library title '$title' added to inventory")
    }

    fun deleteLibraryBook(bookId: String) {
        repository.deleteLibraryBook(bookId)
        showToast("Book title removed.")
    }

    // Clear and Sample Data
    fun clearAllLogs() {
        repository.clearAllLogs()
        showToast("All logs and records have been cleared.")
    }

    fun loadSampleTemplateData() {
        repository.loadSampleTemplateData()
        showToast("Sample templates loaded.")
    }

    fun toggleNotificationsSheet(show: Boolean) {
        _showNotificationsSheet.value = show
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
