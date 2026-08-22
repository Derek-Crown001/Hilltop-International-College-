package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.model.*
import com.example.ui.admin.AdminPortalScreen
import com.example.ui.admission.AdmissionFormDialog
import com.example.ui.components.*
import com.example.ui.parent.ParentPortalScreen
import com.example.ui.public_site.PublicSiteScreen
import com.example.ui.student.StudentPortalScreen
import com.example.ui.teacher.TeacherPortalScreen
import com.example.ui.theme.HilltopCollegeTheme
import com.example.viewmodel.SchoolViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: SchoolViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HilltopCollegeTheme {
                SchoolApp(viewModel)
            }
        }
    }
}

@Composable
fun SchoolApp(viewModel: SchoolViewModel) {
    val currentRole by viewModel.currentRole.collectAsState()
    val studentReportCards by viewModel.studentReportCards.collectAsState()
    val cbtExams by viewModel.cbtExams.collectAsState()
    val cbtResults by viewModel.cbtResults.collectAsState()
    val cbtSession by viewModel.cbtSession.collectAsState()
    val assignments by viewModel.assignments.collectAsState()
    val feeInvoices by viewModel.feeInvoices.collectAsState()
    val paymentReceipts by viewModel.paymentReceipts.collectAsState()
    val attendanceEntries by viewModel.attendanceEntries.collectAsState()
    val newsArticles by viewModel.newsArticles.collectAsState()
    val admissions by viewModel.admissions.collectAsState()
    val hostelRooms by viewModel.hostelRooms.collectAsState()
    val transportRoutes by viewModel.transportRoutes.collectAsState()
    val libraryBooks by viewModel.libraryBooks.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val selectedChildId by viewModel.selectedChildId.collectAsState()

    // Modals state
    val showNotificationsSheet by viewModel.showNotificationsSheet.collectAsState()
    val payingInvoice by viewModel.payingInvoice.collectAsState()
    val viewingReportCard by viewModel.viewingReportCard.collectAsState()
    val viewingReceipt by viewModel.viewingReceipt.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    var showAdmissionForm by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val showToast: (String) -> Unit = { message ->
        viewModel.showToast(message)
    }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CollegeTopBar(
                currentRole = currentRole,
                onRoleSelected = { viewModel.setRole(it) },
                onNotificationClick = { viewModel.toggleNotificationsSheet(true) },
                announcementsCount = announcements.size
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentRole,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "role_navigation_transition"
            ) { role ->
                when (role) {
                    UserRole.GUEST -> {
                        PublicSiteScreen(
                            newsArticles = newsArticles,
                            onSelectRole = { viewModel.setRole(it) },
                            onOpenAdmissionForm = { showAdmissionForm = true },
                            onShowToast = showToast
                        )
                    }
                    UserRole.STUDENT -> {
                        StudentPortalScreen(
                            reportCards = studentReportCards,
                            cbtExams = cbtExams,
                            cbtResults = cbtResults,
                            cbtSession = cbtSession,
                            assignments = assignments,
                            invoices = feeInvoices,
                            onOpenReportCard = { viewModel.openReportCard(it) },
                            onStartCbtExam = { viewModel.startCbtExam(it) },
                            onSelectCbtAnswer = { qId, optIdx -> viewModel.selectCbtAnswer(qId, optIdx) },
                            onNextCbtQuestion = { viewModel.nextCbtQuestion() },
                            onPrevCbtQuestion = { viewModel.prevCbtQuestion() },
                            onJumpToCbtQuestion = { viewModel.jumpToCbtQuestion(it) },
                            onSubmitCbtExam = { viewModel.submitCbtExam() },
                            onExitCbtSession = { viewModel.exitCbtSession() },
                            onSubmitAssignment = { id, text ->
                                viewModel.submitAssignment(id, text)
                            },
                            onPayInvoice = { viewModel.startPaymentFlow(it) },
                            onShowToast = showToast
                        )
                    }
                    UserRole.PARENT -> {
                        ParentPortalScreen(
                            reportCards = studentReportCards,
                            feeInvoices = feeInvoices,
                            paymentReceipts = paymentReceipts,
                            selectedChildId = selectedChildId,
                            onSelectChild = { viewModel.selectChild(it) },
                            onOpenReportCard = { viewModel.openReportCard(it) },
                            onOpenReceipt = { viewModel.openReceipt(it) },
                            onPayInvoice = { viewModel.startPaymentFlow(it) },
                            onShowToast = showToast
                        )
                    }
                    UserRole.TEACHER -> {
                        TeacherPortalScreen(
                            reportCards = studentReportCards,
                            attendanceEntries = attendanceEntries,
                            assignments = assignments,
                            onUpdateScore = { reportId, subCode, ca1, ca2, test, exam ->
                                viewModel.updateSubjectScore(reportId, subCode, ca1, ca2, test, exam)
                            },
                            onToggleAttendance = { viewModel.toggleAttendance(it) },
                            onCreateAssignment = { title, sub, targetClass, due, desc, maxScore ->
                                viewModel.createAssignment(title, sub, targetClass, due, desc, maxScore)
                            },
                            onShowToast = showToast
                        )
                    }
                    UserRole.ADMIN -> {
                        AdminPortalScreen(
                            admissions = admissions,
                            feeInvoices = feeInvoices,
                            paymentReceipts = paymentReceipts,
                            hostelRooms = hostelRooms,
                            transportRoutes = transportRoutes,
                            libraryBooks = libraryBooks,
                            onUpdateAdmissionStatus = { appNo, newStatus ->
                                viewModel.updateAdmissionStatus(appNo, newStatus)
                            },
                            onBroadcastAnnouncement = { title, aud, msg, urgent ->
                                viewModel.broadcastAnnouncement(title, aud, msg, urgent)
                            },
                            onShowToast = showToast
                        )
                    }
                }
            }
        }
    }

    // Notifications Drawer Sheet
    if (showNotificationsSheet) {
        NotificationSheet(
            announcements = announcements,
            onDismiss = { viewModel.toggleNotificationsSheet(false) }
        )
    }

    // Paystack Checkout Modal
    if (payingInvoice != null) {
        PaystackCheckoutModal(
            invoice = payingInvoice!!,
            onDismiss = { viewModel.dismissPaymentFlow() },
            onPaymentSuccess = { invoiceId, amount, gateway, channel, payerName ->
                viewModel.completePayment(invoiceId, amount, gateway, channel, payerName)
            }
        )
    }

    // Printable Terminal Report Card Sheet Modal
    if (viewingReportCard != null) {
        PrintableReportCardModal(
            report = viewingReportCard!!,
            onDismiss = { viewModel.closeReportCard() }
        )
    }

    // Payment Receipt Modal
    if (viewingReceipt != null) {
        OfficialReceiptModal(
            receipt = viewingReceipt!!,
            onDismiss = { viewModel.closeReceipt() }
        )
    }

    // Online Admission Application Form Dialog
    if (showAdmissionForm) {
        AdmissionFormDialog(
            onDismiss = { showAdmissionForm = false },
            onSubmit = { app ->
                viewModel.submitNewAdmission(app)
                showAdmissionForm = false
            }
        )
    }
}
