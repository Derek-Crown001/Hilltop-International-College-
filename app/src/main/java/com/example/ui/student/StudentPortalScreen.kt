package com.example.ui.student

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.*
import com.example.ui.theme.*
import com.example.viewmodel.CbtActiveSession

@Composable
fun StudentPortalScreen(
    reportCards: List<StudentReportCard>,
    cbtExams: List<CbtExam>,
    cbtResults: List<CbtResult>,
    cbtSession: CbtActiveSession?,
    assignments: List<Assignment>,
    invoices: List<FeeInvoice>,
    onOpenReportCard: (StudentReportCard) -> Unit,
    onStartCbtExam: (CbtExam) -> Unit,
    onSelectCbtAnswer: (Int, Int) -> Unit,
    onNextCbtQuestion: () -> Unit,
    onPrevCbtQuestion: () -> Unit,
    onJumpToCbtQuestion: (Int) -> Unit,
    onSubmitCbtExam: () -> Unit,
    onExitCbtSession: () -> Unit,
    onSubmitAssignment: (String, String) -> Unit,
    onPayInvoice: (FeeInvoice) -> Unit,
    onShowToast: (String) -> Unit
) {
    // If a CBT exam is actively running, show the CBT Test Runner screen!
    if (cbtSession != null) {
        CbtExamRunnerScreen(
            session = cbtSession,
            onSelectAnswer = onSelectCbtAnswer,
            onNext = onNextCbtQuestion,
            onPrev = onPrevCbtQuestion,
            onJumpTo = onJumpToCbtQuestion,
            onSubmit = onSubmitCbtExam,
            onExit = onExitCbtSession
        )
        return
    }

    var selectedTab by remember { mutableStateOf("dashboard") }
    var submittingAssignment by remember { mutableStateOf<Assignment?>(null) }
    var submissionText by remember { mutableStateOf("") }

    val studentReport = reportCards.find { it.studentId == "STU-0482" } ?: reportCards.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Navigation Tabs
        ScrollableTabRow(
            selectedTabIndex = when (selectedTab) {
                "dashboard" -> 0
                "results" -> 1
                "cbt" -> 2
                "assignments" -> 3
                "timetable" -> 4
                "fees" -> 5
                else -> 0
            },
            edgePadding = 12.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = HilltopPrimary
        ) {
            listOf(
                "dashboard" to "Dashboard",
                "results" to "Term Results",
                "cbt" to "CBT Exams",
                "assignments" to "Homework",
                "timetable" to "Timetable",
                "fees" to "Fees & Levies"
            ).forEachIndexed { index, (key, label) ->
                Tab(
                    selected = selectedTab == key,
                    onClick = { selectedTab = key },
                    text = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (selectedTab == key) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp)
        ) {
            when (selectedTab) {
                "dashboard" -> {
                    // Profile Header Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.linearGradient(
                                            listOf(HilltopPrimary, HilltopPrimaryLight, HilltopAccentDark)
                                        )
                                    )
                                    .padding(18.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.School,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = studentReport?.studentName ?: "Enrolled Student Portal",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = Color.White
                                        )
                                        Text(
                                            text = if (studentReport != null) "Adm No: ${studentReport.admissionNo} • ${studentReport.className}" else "Hilltop International College Scholar",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.85f)
                                        )
                                        Text(
                                            text = "Academic Session: 2025/2026",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AcademicGold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Key KPI Metrics
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StudentMetricBox(
                                modifier = Modifier.weight(1f),
                                label = "Term Average",
                                value = if (studentReport != null) "${String.format("%.1f", studentReport.averageScore)}%" else "—",
                                subtext = if (studentReport != null) studentReport.promotionStatus else "No Grades Yet",
                                color = NigerianGreen
                            )
                            StudentMetricBox(
                                modifier = Modifier.weight(1f),
                                label = "Class Position",
                                value = if (studentReport != null) "${studentReport.classPosition} / ${studentReport.totalStudentsInClass}" else "—",
                                subtext = if (studentReport != null) "Cohort Rank" else "Pending",
                                color = HilltopPrimary
                            )
                            StudentMetricBox(
                                modifier = Modifier.weight(1f),
                                label = "Attendance",
                                value = if (studentReport != null && studentReport.attendanceTotalDays > 0) "${String.format("%.1f", (studentReport.attendancePresent.toDouble() / studentReport.attendanceTotalDays) * 100)}%" else "—",
                                subtext = if (studentReport != null) "${studentReport.attendancePresent}/${studentReport.attendanceTotalDays} Days" else "Active",
                                color = StatusInfo
                            )
                        }
                    }

                    // Latest Term Result Preview Card
                    if (studentReport == null) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp).fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(Icons.Default.School, contentDescription = null, tint = HilltopPrimary, modifier = Modifier.size(32.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "No Term Results Released Yet",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Results and broadsheet remarks will appear here when registered by your class teacher.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextMuted,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "${studentReport.term} Report Card (${studentReport.session})",
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = "Continuous Assessment (30%) + Examination (70%)",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Button(
                                            onClick = { onOpenReportCard(studentReport) },
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                            modifier = Modifier.testTag("view_report_card_btn")
                                        ) {
                                            Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Full Sheet", style = MaterialTheme.typography.labelMedium)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // Top 4 Subjects mini preview
                                    studentReport.scores.take(4).forEach { score ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 3.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = score.subjectName,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "${String.format("%.0f", score.totalScore)} / 100",
                                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                                    color = HilltopPrimary
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Surface(
                                                    color = NigerianGreen.copy(alpha = 0.15f),
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = score.gradeRemark.grade,
                                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                        color = NigerianGreen,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Urgent Action: Pending Assignments & CBT
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Ready for Practice? CBT Exam Hall",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Simulate WAEC & JAMB Computer Based Tests with instant score analysis.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                cbtExams.take(1).forEach { exam ->
                                    Button(
                                        onClick = { onStartCbtExam(exam) },
                                        colors = ButtonDefaults.buttonColors(containerColor = HilltopAccentDark),
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Icon(Icons.Default.Quiz, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Launch ${exam.title.take(30)}...")
                                    }
                                }
                            }
                        }
                    }
                }

                "results" -> {
                    if (studentReport != null) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Subject Grades Breakdown",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = HilltopPrimary
                                        )
                                        Button(
                                            onClick = { onOpenReportCard(studentReport) },
                                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Report Card PDF")
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    studentReport.scores.forEach { score ->
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1.5f)) {
                                                    Text(
                                                        text = score.subjectName,
                                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                                    )
                                                    Text(
                                                        text = "CA1: ${score.ca1.toInt()} | CA2: ${score.ca2.toInt()} | Test: ${score.testScore.toInt()} | Exam: ${score.examScore.toInt()}",
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                                Column(horizontalAlignment = Alignment.End) {
                                                    Text(
                                                        text = "${String.format("%.0f", score.totalScore)}%",
                                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = HilltopPrimary)
                                                    )
                                                    Text(
                                                        text = "${score.gradeRemark.grade} (${score.gradeRemark.remark})",
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            fontWeight = FontWeight.Bold,
                                                            color = if (score.gradeRemark.grade.startsWith("A") || score.gradeRemark.grade.startsWith("B")) NigerianGreen else StatusInfo
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "cbt" -> {
                    item {
                        Text(
                            text = "Computer Based Test (CBT) Center",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Practice for WAEC, NECO & JAMB with interactive timed tests.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    items(cbtExams) { exam ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = HilltopPrimary.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = exam.subject,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = HilltopPrimary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = "${exam.durationMinutes} Mins • ${exam.totalQuestions} Questions",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = exam.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )

                                Text(
                                    text = exam.instructions,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = { onStartCbtExam(exam) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Start CBT Assessment")
                                }
                            }
                        }
                    }

                    if (cbtResults.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Recent CBT Score History",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        items(cbtResults) { res ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = res.examTitle, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                        Text(text = "Taken on: ${res.completedAt}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "${res.score} / ${res.totalQuestions}",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (res.passed) NigerianGreen else StatusError
                                            )
                                        )
                                        Text(
                                            text = if (res.passed) "PASSED (${String.format("%.0f", res.percentage)}%)" else "FAILED",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = if (res.passed) NigerianGreen else StatusError
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                "assignments" -> {
                    item {
                        Text(
                            text = "Homework & Coursework Submissions",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    items(assignments) { asn ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = HilltopPrimary.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "${asn.subject} • Due ${asn.dueDate}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = HilltopPrimary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Surface(
                                        color = if (asn.isSubmitted) StatusSuccess.copy(alpha = 0.15f) else StatusWarning.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = if (asn.isSubmitted) "SUBMITTED" else "PENDING",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = if (asn.isSubmitted) StatusSuccess else StatusWarning,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(text = asn.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                Text(text = "Instructor: ${asn.teacherName}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = asn.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                if (asn.isSubmitted) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        color = Color(0xFFF0FDF4),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(text = "Your Submission:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = NigerianGreen)
                                            Text(text = asn.submissionText, style = MaterialTheme.typography.bodySmall)
                                            if (asn.scoreAwarded != null) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Grade: ${asn.scoreAwarded} / ${asn.maxScore} pts • \"${asn.feedback}\"",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = NigerianGreen
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(
                                        onClick = {
                                            submittingAssignment = asn
                                            submissionText = ""
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Submit Solution")
                                    }
                                }
                            }
                        }
                    }
                }

                "timetable" -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Class Schedule: SSS 3 Science A",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = HilltopPrimary
                                )
                                Text(
                                    text = "Academic Session 2025/2026",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                listOf(
                                    TimetablePeriod(1, "08:00 - 08:45 AM", "General Mathematics", "Mr. B. Adeyemi", "Science Hall A"),
                                    TimetablePeriod(2, "08:45 - 09:30 AM", "Further Mathematics", "Mr. B. Adeyemi", "Science Hall A"),
                                    TimetablePeriod(3, "09:30 - 10:15 AM", "Physics Practical", "Dr. K. Obi", "Physics Lab 1"),
                                    TimetablePeriod(4, "10:45 - 11:30 AM", "Chemistry (Organic)", "Dr. K. Obi", "Chemistry Lab 2"),
                                    TimetablePeriod(5, "11:30 - 12:15 PM", "English & Lexis", "Mrs. N. Chukwuma", "Language Room 4"),
                                    TimetablePeriod(6, "01:00 - 01:45 PM", "Computer Science & Robotics", "Engr. T. Salami", "STEM AI Hub"),
                                    TimetablePeriod(7, "01:45 - 02:30 PM", "Civic Education", "Barr. J. Fashola", "Science Hall A")
                                ).forEach { period ->
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(text = period.subject, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                                Text(text = "${period.teacher} • ${period.room}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                            }
                                            Surface(
                                                color = HilltopPrimary.copy(alpha = 0.1f),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = period.timeSlot,
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = HilltopPrimary,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "fees" -> {
                    item {
                        Text(
                            text = "Student Invoices & Fee Status",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    items(invoices.filter { it.studentId == "STU-0482" }) { inv ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = inv.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1f))
                                    Surface(
                                        color = when (inv.status) {
                                            PaymentStatus.PAID -> StatusSuccess.copy(alpha = 0.15f)
                                            PaymentStatus.PARTIAL -> StatusWarning.copy(alpha = 0.15f)
                                            else -> StatusError.copy(alpha = 0.15f)
                                        },
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = inv.status.name,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = when (inv.status) {
                                                PaymentStatus.PAID -> StatusSuccess
                                                PaymentStatus.PARTIAL -> StatusWarning
                                                else -> StatusError
                                            },
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Total: ₦${"%,d".format(inv.totalAmount)}", style = MaterialTheme.typography.bodySmall)
                                    Text(text = "Paid: ₦${"%,d".format(inv.amountPaid)}", style = MaterialTheme.typography.bodySmall, color = StatusSuccess)
                                }

                                if (inv.balanceDue > 0) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Outstanding Balance: ₦${"%,d".format(inv.balanceDue)}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = StatusError
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { onPayInvoice(inv) },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Pay Balance via Paystack/Flutterwave")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Homework submission dialog
    if (submittingAssignment != null) {
        val asn = submittingAssignment!!
        Dialog(onDismissRequest = { submittingAssignment = null }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Submit Solution: ${asn.title}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = asn.description, style = MaterialTheme.typography.bodySmall, color = TextMuted)

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = submissionText,
                        onValueChange = { submissionText = it },
                        label = { Text("Enter your solution steps or text answer") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        maxLines = 8
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { submittingAssignment = null }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (submissionText.isNotBlank()) {
                                    onSubmitAssignment(asn.id, submissionText)
                                    submittingAssignment = null
                                }
                            },
                            enabled = submissionText.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Submit Now")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CbtExamRunnerScreen(
    session: CbtActiveSession,
    onSelectAnswer: (Int, Int) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onJumpTo: (Int) -> Unit,
    onSubmit: () -> Unit,
    onExit: () -> Unit
) {
    val exam = session.exam
    val question = exam.questions.getOrNull(session.currentQuestionIndex)

    val minutes = session.remainingSeconds / 60
    val seconds = session.remainingSeconds % 60

    if (session.isFinished && session.result != null) {
        // CBT Results Breakdown Screen
        val res = session.result
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(if (res.passed) NigerianGreen.copy(alpha = 0.15f) else StatusError.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (res.passed) Icons.Default.EmojiEvents else Icons.Default.Cancel,
                    contentDescription = null,
                    tint = if (res.passed) NigerianGreen else StatusError,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (res.passed) "Congratulations! You Passed!" else "Assessment Complete",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                color = if (res.passed) NigerianGreen else StatusError
            )

            Text(
                text = exam.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${res.score} / ${res.totalQuestions}",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = HilltopPrimary
                        )
                    )
                    Text(
                        text = "Score: ${String.format("%.1f", res.percentage)}% (Pass mark: ${exam.passMarkPercentage}%)",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (res.passed) NigerianGreen else StatusError
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Question Review & Explanations",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(exam.questions) { q ->
                    val chosen = res.answersMap[q.id]
                    val isCorrect = chosen == q.correctIndex

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCorrect) Color(0xFFF0FDF4) else Color(0xFFFEF2F2)
                        )
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Q${q.id}: ${q.question}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Your choice: ${if (chosen != null) q.options.getOrElse(chosen) { "None" } else "Unanswered"}",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isCorrect) NigerianGreen else StatusError
                            )
                            Text(
                                text = "Correct answer: ${q.options[q.correctIndex]}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = NigerianGreen
                            )
                            Text(
                                text = "Explanation: ${q.explanation}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onExit,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Return to CBT Hall")
            }
        }
        return
    }

    // Active CBT Test Running UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(14.dp)
    ) {
        // Test Header with Countdown Timer
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = HilltopPrimary)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = exam.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Question ${session.currentQuestionIndex + 1} of ${exam.questions.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Surface(
                    color = if (minutes < 3) StatusError else Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%02d:%02d", minutes, seconds),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black),
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Question Navigation Palette
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(exam.questions.size) { idx ->
                val qId = exam.questions[idx].id
                val isAnswered = session.answersMap.containsKey(qId)
                val isCurrent = session.currentQuestionIndex == idx

                Surface(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .clickable { onJumpTo(idx) },
                    color = when {
                        isCurrent -> AcademicGold
                        isAnswered -> NigerianGreen
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = CircleShape
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = (idx + 1).toString(),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isCurrent || isAnswered) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (question != null) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Question ${session.currentQuestionIndex + 1}:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = question.question,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val chosenOption = session.answersMap[question.id]

                    question.options.forEachIndexed { optIndex, optText ->
                        val isSelected = chosenOption == optIndex
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onSelectAnswer(question.id, optIndex) },
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(10.dp),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, HilltopPrimary) else null
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onSelectAnswer(question.id, optIndex) }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = optText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onPrev,
                enabled = session.currentQuestionIndex > 0,
                modifier = Modifier.weight(1f)
            ) {
                Text("Previous")
            }

            if (session.currentQuestionIndex < exam.questions.size - 1) {
                Button(
                    onClick = onNext,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary)
                ) {
                    Text("Next")
                }
            } else {
                Button(
                    onClick = onSubmit,
                    modifier = Modifier.weight(1.2f),
                    colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Submit Test")
                }
            }
        }
    }
}

@Composable
private fun StudentMetricBox(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    subtext: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                color = color
            )
            Text(
                text = subtext,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
