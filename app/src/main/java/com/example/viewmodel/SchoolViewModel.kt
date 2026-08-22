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
    private val _selectedChildId = MutableStateFlow("STU-0482") // Chinedu
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
            UserRole.TEACHER -> "classes"
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
            "Chinedu Emmanuel Okafor",
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

    // Teacher actions
    fun updateSubjectScore(reportId: String, subjectCode: String, ca1: Double, ca2: Double, test: Double, exam: Double) {
        repository.updateSubjectScore(reportId, subjectCode, ca1, ca2, test, exam)
        showToast("Score updated successfully for $subjectCode")
    }

    fun toggleAttendance(studentId: String) {
        repository.toggleAttendance(studentId)
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
        showToast("Application #${app.applicationNo} submitted successfully!")
    }

    fun updateAdmissionStatus(appNo: String, newStatus: String) {
        repository.updateAdmissionStatus(appNo, newStatus)
        showToast("Admission status updated to: $newStatus")
    }

    // Announcement actions
    fun broadcastAnnouncement(title: String, audience: String, message: String, isUrgent: Boolean) {
        repository.broadcastAnnouncement(title, audience, message, isUrgent)
        showToast("Announcement broadcasted to $audience")
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
