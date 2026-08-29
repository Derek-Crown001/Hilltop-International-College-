package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SchoolRepository {

    private val _currentRole = MutableStateFlow(UserRole.GUEST)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _studentReportCards = MutableStateFlow<List<StudentReportCard>>(emptyList())
    val studentReportCards: StateFlow<List<StudentReportCard>> = _studentReportCards.asStateFlow()

    private val _feeInvoices = MutableStateFlow<List<FeeInvoice>>(emptyList())
    val feeInvoices: StateFlow<List<FeeInvoice>> = _feeInvoices.asStateFlow()

    private val _paymentReceipts = MutableStateFlow<List<PaymentReceipt>>(emptyList())
    val paymentReceipts: StateFlow<List<PaymentReceipt>> = _paymentReceipts.asStateFlow()

    private val _cbtExams = MutableStateFlow<List<CbtExam>>(emptyList())
    val cbtExams: StateFlow<List<CbtExam>> = _cbtExams.asStateFlow()

    private val _cbtResults = MutableStateFlow<List<CbtResult>>(emptyList())
    val cbtResults: StateFlow<List<CbtResult>> = _cbtResults.asStateFlow()

    private val _assignments = MutableStateFlow<List<Assignment>>(emptyList())
    val assignments: StateFlow<List<Assignment>> = _assignments.asStateFlow()

    private val _attendanceEntries = MutableStateFlow<List<AttendanceEntry>>(emptyList())
    val attendanceEntries: StateFlow<List<AttendanceEntry>> = _attendanceEntries.asStateFlow()

    private val _newsArticles = MutableStateFlow<List<NewsArticle>>(emptyList())
    val newsArticles: StateFlow<List<NewsArticle>> = _newsArticles.asStateFlow()

    private val _admissions = MutableStateFlow<List<AdmissionApplication>>(emptyList())
    val admissions: StateFlow<List<AdmissionApplication>> = _admissions.asStateFlow()

    private val _announcements = MutableStateFlow<List<SchoolAnnouncement>>(emptyList())
    val announcements: StateFlow<List<SchoolAnnouncement>> = _announcements.asStateFlow()

    private val _hostelRooms = MutableStateFlow<List<HostelRoom>>(emptyList())
    val hostelRooms: StateFlow<List<HostelRoom>> = _hostelRooms.asStateFlow()

    private val _transportRoutes = MutableStateFlow<List<TransportRoute>>(emptyList())
    val transportRoutes: StateFlow<List<TransportRoute>> = _transportRoutes.asStateFlow()

    private val _libraryBooks = MutableStateFlow<List<LibraryBook>>(emptyList())
    val libraryBooks: StateFlow<List<LibraryBook>> = _libraryBooks.asStateFlow()

    init {
        // Clean start: all demo logs removed as per school owner's request
        clearAllLogs()
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun clearAllLogs() {
        _studentReportCards.value = emptyList()
        _feeInvoices.value = emptyList()
        _paymentReceipts.value = emptyList()
        _cbtExams.value = emptyList()
        _cbtResults.value = emptyList()
        _assignments.value = emptyList()
        _attendanceEntries.value = emptyList()
        _newsArticles.value = emptyList()
        _admissions.value = emptyList()
        _announcements.value = emptyList()
        _hostelRooms.value = emptyList()
        _transportRoutes.value = emptyList()
        _libraryBooks.value = emptyList()
    }

    // --- News Articles Management ---
    fun addNewsArticle(article: NewsArticle) {
        val list = _newsArticles.value.toMutableList()
        list.add(0, article)
        _newsArticles.value = list
    }

    fun deleteNewsArticle(id: String) {
        _newsArticles.value = _newsArticles.value.filter { it.id != id }
    }

    // --- Announcements & Bulletins ---
    fun broadcastAnnouncement(title: String, audience: String, message: String, isUrgent: Boolean) {
        val list = _announcements.value.toMutableList()
        val newAnn = SchoolAnnouncement(
            id = "ANC-${(100..999).random()}",
            title = title,
            targetAudience = audience,
            timestamp = "Just now",
            message = message,
            isUrgent = isUrgent
        )
        list.add(0, newAnn)
        _announcements.value = list
    }

    fun deleteAnnouncement(id: String) {
        _announcements.value = _announcements.value.filter { it.id != id }
    }

    // --- Admissions Desk ---
    fun submitNewAdmission(app: AdmissionApplication) {
        val list = _admissions.value.toMutableList()
        list.add(0, app)
        _admissions.value = list
    }

    fun updateAdmissionStatus(applicationNo: String, newStatus: String) {
        val list = _admissions.value.toMutableList()
        val idx = list.indexOfFirst { it.applicationNo == applicationNo }
        if (idx != -1) {
            list[idx] = list[idx].copy(status = newStatus)
            _admissions.value = list
        }
    }

    fun deleteAdmission(applicationNo: String) {
        _admissions.value = _admissions.value.filter { it.applicationNo != applicationNo }
    }

    // --- Invoices & Fees ---
    fun createFeeInvoice(invoice: FeeInvoice) {
        val list = _feeInvoices.value.toMutableList()
        list.add(0, invoice)
        _feeInvoices.value = list
    }

    fun deleteFeeInvoice(invoiceId: String) {
        _feeInvoices.value = _feeInvoices.value.filter { it.invoiceId != invoiceId }
    }

    fun recordPaymentReceipt(receipt: PaymentReceipt) {
        val receipts = _paymentReceipts.value.toMutableList()
        receipts.add(0, receipt)
        _paymentReceipts.value = receipts
    }

    fun submitPayment(invoiceId: String, amountPaid: Long, gateway: String, channel: String, payerName: String): PaymentReceipt {
        val invoices = _feeInvoices.value.toMutableList()
        val index = invoices.indexOfFirst { it.invoiceId == invoiceId }
        val studentName = if (index != -1) invoices[index].studentName else "Enrolled Student"
        val admissionNo = if (index != -1) "HIC/${invoices[index].studentId.replace("STU-", "2026/")}" else "HIC/2026/001"

        if (index != -1) {
            val oldInv = invoices[index]
            val newAmountPaid = oldInv.amountPaid + amountPaid
            val newStatus = if (newAmountPaid >= oldInv.totalAmount) PaymentStatus.PAID else PaymentStatus.PARTIAL
            invoices[index] = oldInv.copy(amountPaid = newAmountPaid, status = newStatus)
            _feeInvoices.value = invoices
        }

        val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
        val receiptNumber = "HIC/REC/2026/${(1000..9999).random()}"
        val transactionRef = "${gateway.take(3).uppercase()}_TXN_${System.currentTimeMillis().toString().takeLast(8)}"

        val receipt = PaymentReceipt(
            receiptNumber = receiptNumber,
            transactionRef = transactionRef,
            invoiceId = invoiceId,
            studentName = studentName,
            admissionNo = admissionNo,
            amountPaid = amountPaid,
            paymentDate = dateFormat.format(Date()),
            paymentGateway = gateway,
            channel = channel,
            payerName = payerName,
            status = "Successful"
        )

        val receipts = _paymentReceipts.value.toMutableList()
        receipts.add(0, receipt)
        _paymentReceipts.value = receipts
        return receipt
    }

    // --- Hostels ---
    fun addHostelRoom(room: HostelRoom) {
        val list = _hostelRooms.value.toMutableList()
        list.add(room)
        _hostelRooms.value = list
    }

    fun deleteHostelRoom(roomNumber: String) {
        _hostelRooms.value = _hostelRooms.value.filter { it.roomNumber != roomNumber }
    }

    // --- Bus Fleet ---
    fun addTransportRoute(route: TransportRoute) {
        val list = _transportRoutes.value.toMutableList()
        list.add(route)
        _transportRoutes.value = list
    }

    fun deleteTransportRoute(routeCode: String) {
        _transportRoutes.value = _transportRoutes.value.filter { it.routeCode != routeCode }
    }

    // --- Library ---
    fun addLibraryBook(book: LibraryBook) {
        val list = _libraryBooks.value.toMutableList()
        list.add(book)
        _libraryBooks.value = list
    }

    fun deleteLibraryBook(bookId: String) {
        _libraryBooks.value = _libraryBooks.value.filter { it.id != bookId }
    }

    // --- CBT Exams ---
    fun createCbtExam(exam: CbtExam) {
        val list = _cbtExams.value.toMutableList()
        list.add(0, exam)
        _cbtExams.value = list
    }

    fun deleteCbtExam(examId: String) {
        _cbtExams.value = _cbtExams.value.filter { it.examId != examId }
    }

    fun submitCbtExam(examId: String, studentName: String, answersMap: Map<Int, Int>): CbtResult {
        val exam = _cbtExams.value.find { it.examId == examId } ?: return CbtResult(
            examId, "Examination", studentName, 0, 0, 0.0, false, "Just now", answersMap
        )

        var correctCount = 0
        exam.questions.forEach { question ->
            val chosen = answersMap[question.id]
            if (chosen != null && chosen == question.correctIndex) {
                correctCount++
            }
        }

        val percentage = if (exam.questions.isNotEmpty()) {
            (correctCount.toDouble() / exam.questions.size.toDouble()) * 100.0
        } else {
            0.0
        }
        val passed = percentage >= exam.passMarkPercentage
        val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())

        val result = CbtResult(
            examId = exam.examId,
            examTitle = exam.title,
            studentName = studentName,
            score = correctCount,
            totalQuestions = exam.questions.size,
            percentage = percentage,
            passed = passed,
            completedAt = dateFormat.format(Date()),
            answersMap = answersMap
        )

        val resultsList = _cbtResults.value.toMutableList()
        resultsList.add(0, result)
        _cbtResults.value = resultsList
        return result
    }

    // --- Report Cards & Student Grades ---
    fun createReportCard(report: StudentReportCard) {
        val list = _studentReportCards.value.toMutableList()
        val existingIndex = list.indexOfFirst { it.reportId == report.reportId }
        if (existingIndex != -1) {
            list[existingIndex] = report
        } else {
            list.add(0, report)
        }
        _studentReportCards.value = list
    }

    fun updateSubjectScore(reportId: String, subjectCode: String, newCa1: Double, newCa2: Double, newTest: Double, newExam: Double) {
        val reports = _studentReportCards.value.toMutableList()
        val rIndex = reports.indexOfFirst { it.reportId == reportId }
        if (rIndex != -1) {
            val report = reports[rIndex]
            val updatedScores = report.scores.map { score ->
                if (score.subjectCode == subjectCode) {
                    score.copy(ca1 = newCa1, ca2 = newCa2, testScore = newTest, examScore = newExam)
                } else {
                    score
                }
            }
            reports[rIndex] = report.copy(scores = updatedScores)
            _studentReportCards.value = reports
        }
    }

    // --- Attendance Entries ---
    fun addAttendanceEntry(entry: AttendanceEntry) {
        val list = _attendanceEntries.value.toMutableList()
        list.add(entry)
        _attendanceEntries.value = list
    }

    fun toggleAttendance(studentId: String) {
        val list = _attendanceEntries.value.toMutableList()
        val idx = list.indexOfFirst { it.studentId == studentId }
        if (idx != -1) {
            val old = list[idx]
            list[idx] = old.copy(isPresent = !old.isPresent)
            _attendanceEntries.value = list
        }
    }

    // --- Homework & Assignments ---
    fun createAssignment(title: String, subject: String, targetClass: String, dueDate: String, desc: String, maxScore: Int) {
        val list = _assignments.value.toMutableList()
        val newAsn = Assignment(
            id = "ASN-${(100..999).random()}",
            title = title,
            subject = subject,
            targetClass = targetClass,
            dueDate = dueDate,
            teacherName = "Subject Instructor",
            description = desc,
            maxScore = maxScore,
            isSubmitted = false
        )
        list.add(0, newAsn)
        _assignments.value = list
    }

    fun submitAssignmentSolution(assignmentId: String, text: String) {
        val list = _assignments.value.toMutableList()
        val idx = list.indexOfFirst { it.id == assignmentId }
        if (idx != -1) {
            val old = list[idx]
            list[idx] = old.copy(isSubmitted = true, submissionText = text)
            _assignments.value = list
        }
    }

    // --- Optional Sample Templates Seeder ---
    fun loadSampleTemplateData() {
        val sampleScores = listOf(
            SubjectScore("Mathematics", "MTH 301", 9.5, 9.0, 9.0, 64.0, "Outstanding problem solving and logic"),
            SubjectScore("English Language", "ENG 301", 8.5, 9.0, 8.5, 59.0, "Great essays and comprehension skills"),
            SubjectScore("Physics", "PHY 301", 9.0, 10.0, 9.5, 63.0, "Excellent grasp of mechanics and optics"),
            SubjectScore("Chemistry", "CHM 301", 8.0, 9.0, 8.5, 60.0, "Strong organic chemistry mastery"),
            SubjectScore("Biology", "BIO 301", 9.0, 9.0, 9.0, 62.0, "Very thorough practical investigations")
        )

        _studentReportCards.value = listOf(
            StudentReportCard(
                reportId = "REP-2026-01",
                studentId = "STU-001",
                studentName = "David Adeleke",
                admissionNo = "HIC/2026/0001",
                className = "SSS 3 Science A",
                term = "2nd Term",
                session = "2025/2026",
                scores = sampleScores,
                classPosition = 1,
                totalStudentsInClass = 25,
                attendancePresent = 68,
                attendanceTotalDays = 70,
                classTeacherComment = "Diligent and dedicated scholar.",
                principalComment = "Excellent performance.",
                promotionStatus = "Distinction"
            )
        )

        _attendanceEntries.value = listOf(
            AttendanceEntry(studentId = "STU-001", studentName = "David Adeleke", admissionNo = "HIC/2026/0001", isPresent = true, remarks = "Present on time"),
            AttendanceEntry(studentId = "STU-002", studentName = "Amina Fatima Bello", admissionNo = "HIC/2026/0002", isPresent = true, remarks = "Present on time")
        )

        _newsArticles.value = listOf(
            NewsArticle(
                id = "NEWS-01",
                title = "Welcome to the New Academic Session",
                category = "Campus Life",
                date = "Recent",
                author = "Principal's Office",
                summary = "Hilltop College opens its doors to scholars for a term of excellence.",
                fullContent = "We are excited to welcome all returning students, freshers, and parents to the new academic term."
            )
        )

        _announcements.value = listOf(
            SchoolAnnouncement(
                id = "ANC-01",
                title = "Upcoming PTA Assembly & Open Day",
                targetAudience = "All",
                timestamp = "Just now",
                message = "The general PTA assembly will be held this Saturday at 10:00 AM.",
                isUrgent = false
            )
        )

        _hostelRooms.value = listOf(
            HostelRoom("Room A-101", "Nelson Mandela Hall", 6, 4, "1st Floor", "Student Prefect")
        )

        _transportRoutes.value = listOf(
            TransportRoute("RT-01", "Lekki - Ikoyi Express", "Toyota Coaster - HIC 01", "Mr. Sunday Okoro", "+234 803 111 2233", listOf("Admiralty Way", "Bourdillon Road"), "6:45 AM", 60000L)
        )

        _libraryBooks.value = listOf(
            LibraryBook("BK-01", "New General Mathematics for SSS 3", "M.F. Macrae et al.", "978-0582588", "Mathematics", "Shelf M-12", 40, 35)
        )
    }
}
