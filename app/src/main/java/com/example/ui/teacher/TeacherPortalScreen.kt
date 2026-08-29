package com.example.ui.teacher

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun TeacherPortalScreen(
    reportCards: List<StudentReportCard>,
    attendanceEntries: List<AttendanceEntry>,
    assignments: List<Assignment>,
    onUpdateScore: (reportId: String, subjectCode: String, ca1: Double, ca2: Double, test: Double, exam: Double) -> Unit,
    onRegisterReportCard: (StudentReportCard) -> Unit = {},
    onAddAttendanceStudent: (studentName: String, admissionNo: String, className: String) -> Unit = { _, _, _ -> },
    onToggleAttendance: (studentId: String) -> Unit,
    onCreateAssignment: (title: String, subject: String, targetClass: String, dueDate: String, desc: String, maxScore: Int) -> Unit,
    onShowToast: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf("scores") }
    var selectedClass by remember { mutableStateOf("SSS 3 Science A") }

    // State for Score Entry Dialog
    var editingScoreInfo by remember { mutableStateOf<Pair<String, SubjectScore>?>(null) } // reportId to SubjectScore
    var editCa1 by remember { mutableStateOf("9.5") }
    var editCa2 by remember { mutableStateOf("9.0") }
    var editTest by remember { mutableStateOf("9.0") }
    var editExam by remember { mutableStateOf("64.0") }

    // State for Register Report Card Dialog
    var showAddStudentReportDialog by remember { mutableStateOf(false) }
    var newStudentName by remember { mutableStateOf("") }
    var newAdmNo by remember { mutableStateOf("") }
    var newClassName by remember { mutableStateOf("SSS 3 Science A") }
    var newTerm by remember { mutableStateOf("2nd Term") }
    var newSession by remember { mutableStateOf("2025/2026") }

    // State for Add Attendance Student Dialog
    var showAddAttendanceStudentDialog by remember { mutableStateOf(false) }
    var attStudentName by remember { mutableStateOf("") }
    var attAdmNo by remember { mutableStateOf("") }
    var attClass by remember { mutableStateOf("SSS 3 Science A") }

    // State for Create Assignment Dialog
    var showCreateAsnDialog by remember { mutableStateOf(false) }
    var asnTitle by remember { mutableStateOf("") }
    var asnSubject by remember { mutableStateOf("General Mathematics") }
    var asnClass by remember { mutableStateOf("SSS 3 Science A") }
    var asnDue by remember { mutableStateOf("Next Monday, 8:00 AM") }
    var asnDesc by remember { mutableStateOf("") }

    val report = reportCards.find { it.className == selectedClass } ?: reportCards.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Teacher Profile Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(NigerianGreen.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CastForEducation, contentDescription = null, tint = NigerianGreen, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Academic & Faculty Portal",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Grading, Attendance Roster & Assignments",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
        }

        // Sub Tabs
        ScrollableTabRow(
            selectedTabIndex = when (selectedTab) {
                "scores" -> 0
                "attendance" -> 1
                "assignments" -> 2
                "analytics" -> 3
                else -> 0
            },
            edgePadding = 12.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = NigerianGreen
        ) {
            listOf(
                "scores" to "Scores & Report Cards (${reportCards.size})",
                "attendance" to "Daily Attendance (${attendanceEntries.size})",
                "assignments" to "Homework (${assignments.size})",
                "analytics" to "Broadsheet Analytics"
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
                "scores" -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Continuous Assessment & Exam Scores",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = HilltopPrimary
                                        )
                                        Text(
                                            text = "1st CA (10%) + 2nd CA (10%) + Test (10%) + Exam (70%)",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Button(
                                        onClick = { showAddStudentReportDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Register Student", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }

                    if (reportCards.isEmpty()) {
                        item {
                            EmptyTeacherCard(
                                title = "No Student Grade Records",
                                subtitle = "Create student profiles and enter subject marks for continuous assessment and terminal examinations.",
                                icon = Icons.Default.Grade,
                                onAction = { showAddStudentReportDialog = true },
                                actionText = "Register Student Report Card"
                            )
                        }
                    } else {
                        if (report != null) {
                            item {
                                Text(
                                    text = "Student: ${report.studentName} (${report.admissionNo}) • ${report.className}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            items(report.scores) { score ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            editingScoreInfo = Pair(report.reportId, score)
                                            editCa1 = score.ca1.toString()
                                            editCa2 = score.ca2.toString()
                                            editTest = score.testScore.toString()
                                            editExam = score.examScore.toString()
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1.5f)) {
                                            Text(
                                                text = score.subjectName,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = "CA: ${(score.ca1 + score.ca2 + score.testScore).toInt()}/30 • Exam: ${score.examScore.toInt()}/70",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextMuted
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Column(horizontalAlignment = Alignment.End) {
                                                Text(
                                                    text = "${String.format("%.0f", score.totalScore)}%",
                                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = HilltopPrimary)
                                                )
                                                Text(
                                                    text = score.gradeRemark.grade,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = NigerianGreen
                                                    )
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(Icons.Default.Edit, contentDescription = "Edit Score", tint = HilltopPrimary, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "attendance" -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Class Register & Roll Call",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = HilltopPrimary
                                        )
                                        Text(
                                            text = "Today: ${attendanceEntries.count { it.isPresent }} / ${attendanceEntries.size} Present",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = NigerianGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Button(
                                        onClick = { showAddAttendanceStudentDialog = true },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen)
                                    ) {
                                        Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Add Student", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }

                    if (attendanceEntries.isEmpty()) {
                        item {
                            EmptyTeacherCard(
                                title = "Class Attendance Roster Empty",
                                subtitle = "Add students to the class roster to take attendance and record daily punctuality.",
                                icon = Icons.Default.HowToReg,
                                onAction = { showAddAttendanceStudentDialog = true },
                                actionText = "Add Student to Roster"
                            )
                        }
                    } else {
                        items(attendanceEntries) { entry ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onToggleAttendance(entry.studentId) },
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (entry.isPresent) Color(0xFFF0FDF4) else Color(0xFFFEF2F2)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = entry.studentName, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                        Text(text = "${entry.admissionNo} • ${entry.remarks.ifBlank { "Daily Attendance" }}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                    }
                                    Surface(
                                        color = if (entry.isPresent) NigerianGreen else StatusError,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = if (entry.isPresent) "PRESENT" else "ABSENT",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                "assignments" -> {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Published Homework (${assignments.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Button(
                                onClick = { showCreateAsnDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("create_assignment_btn")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("New Assignment", fontSize = 12.sp)
                            }
                        }
                    }

                    if (assignments.isEmpty()) {
                        item {
                            EmptyTeacherCard(
                                title = "No Homework Assignments Published",
                                subtitle = "Create and dispatch homework exercises, reading tasks, and class projects.",
                                icon = Icons.Default.Assignment,
                                onAction = { showCreateAsnDialog = true },
                                actionText = "Create New Assignment"
                            )
                        }
                    } else {
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
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = asn.subject, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = HilltopPrimary)
                                        Text(text = "Target: ${asn.targetClass}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = asn.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                    Text(text = asn.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Max Score: ${asn.maxScore} pts", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                        Text(text = "Due: ${asn.dueDate}", style = MaterialTheme.typography.labelSmall, color = StatusWarning)
                                    }
                                }
                            }
                        }
                    }
                }

                "analytics" -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Class Broadsheet & Performance Summary",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = HilltopPrimary
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                val totalStudents = reportCards.size
                                val totalAttend = attendanceEntries.size
                                val presentCount = attendanceEntries.count { it.isPresent }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    TeacherAnalyticsBox(Modifier.weight(1f), "Registered Students", "$totalStudents", NigerianGreen)
                                    TeacherAnalyticsBox(Modifier.weight(1f), "Roster Roll", "$totalAttend", HilltopPrimary)
                                    TeacherAnalyticsBox(Modifier.weight(1f), "Attendance", if (totalAttend > 0) "${(presentCount * 100) / totalAttend}%" else "0%", AcademicGold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Register Student Report Dialog
    if (showAddStudentReportDialog) {
        Dialog(onDismissRequest = { showAddStudentReportDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Register Student & Grade Card",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = newStudentName,
                        onValueChange = { newStudentName = it },
                        label = { Text("Student Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = newAdmNo,
                            onValueChange = { newAdmNo = it },
                            label = { Text("Admission No") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = newClassName,
                            onValueChange = { newClassName = it },
                            label = { Text("Class") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showAddStudentReportDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (newStudentName.isNotBlank()) {
                                    val defaultScores = listOf(
                                        SubjectScore("Mathematics", "MTH 301", 8.0, 8.5, 9.0, 60.0, "Good comprehension"),
                                        SubjectScore("English Language", "ENG 301", 8.5, 9.0, 8.5, 58.0, "Great essays"),
                                        SubjectScore("Physics", "PHY 301", 8.0, 8.0, 8.5, 55.0, "Solid practical skills"),
                                        SubjectScore("Chemistry", "CHM 301", 8.5, 8.0, 8.0, 56.0, "Diligent student"),
                                        SubjectScore("Biology", "BIO 301", 8.0, 8.5, 8.5, 57.0, "Active participant")
                                    )
                                    val newReport = StudentReportCard(
                                        reportId = "REP-${(1000..9999).random()}",
                                        studentId = "STU-${(100..999).random()}",
                                        studentName = newStudentName,
                                        admissionNo = newAdmNo.ifBlank { "HIC/2026/001" },
                                        className = newClassName,
                                        term = newTerm,
                                        session = newSession,
                                        scores = defaultScores,
                                        classPosition = 1,
                                        totalStudentsInClass = 1,
                                        attendancePresent = 68,
                                        attendanceTotalDays = 70,
                                        classTeacherComment = "Hardworking and attentive scholar.",
                                        principalComment = "Keep up the commendable effort.",
                                        promotionStatus = "Good Standing"
                                    )
                                    onRegisterReportCard(newReport)
                                    showAddStudentReportDialog = false
                                    newStudentName = ""
                                    newAdmNo = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Register Student")
                        }
                    }
                }
            }
        }
    }

    // Add Attendance Student Dialog
    if (showAddAttendanceStudentDialog) {
        Dialog(onDismissRequest = { showAddAttendanceStudentDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Add Student to Attendance Roster",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NigerianGreen
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = attStudentName,
                        onValueChange = { attStudentName = it },
                        label = { Text("Student Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = attAdmNo,
                            onValueChange = { attAdmNo = it },
                            label = { Text("Admission No") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = attClass,
                            onValueChange = { attClass = it },
                            label = { Text("Class") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showAddAttendanceStudentDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (attStudentName.isNotBlank()) {
                                    onAddAttendanceStudent(attStudentName, attAdmNo.ifBlank { "HIC/2026/001" }, attClass)
                                    showAddAttendanceStudentDialog = false
                                    attStudentName = ""
                                    attAdmNo = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Add to Roster")
                        }
                    }
                }
            }
        }
    }

    // Score Edit Dialog
    if (editingScoreInfo != null) {
        val (reportId, score) = editingScoreInfo!!
        val calculatedTotal = (editCa1.toDoubleOrNull() ?: 0.0) + (editCa2.toDoubleOrNull() ?: 0.0) +
                (editTest.toDoubleOrNull() ?: 0.0) + (editExam.toDoubleOrNull() ?: 0.0)
        val calculatedGrade = GradeRemark.fromScore(calculatedTotal)

        Dialog(onDismissRequest = { editingScoreInfo = null }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Edit Score: ${score.subjectName}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimaryDark
                    )
                    Text(text = "Enter scores out of their respective maximum weights", style = MaterialTheme.typography.labelSmall, color = TextMuted)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editCa1,
                            onValueChange = { editCa1 = it },
                            label = { Text("1st CA (10)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = editCa2,
                            onValueChange = { editCa2 = it },
                            label = { Text("2nd CA (10)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = editTest,
                            onValueChange = { editTest = it },
                            label = { Text("Test (10)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = editExam,
                            onValueChange = { editExam = it },
                            label = { Text("Exam (70)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Total: ${String.format("%.1f", calculatedTotal)} / 100", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                            Text(text = "Grade: ${calculatedGrade.grade} (${calculatedGrade.remark})", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = NigerianGreen))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { editingScoreInfo = null }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                onUpdateScore(
                                    reportId,
                                    score.subjectCode,
                                    editCa1.toDoubleOrNull() ?: 0.0,
                                    editCa2.toDoubleOrNull() ?: 0.0,
                                    editTest.toDoubleOrNull() ?: 0.0,
                                    editExam.toDoubleOrNull() ?: 0.0
                                )
                                editingScoreInfo = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Save Score")
                        }
                    }
                }
            }
        }
    }

    // Create Assignment Dialog
    if (showCreateAsnDialog) {
        Dialog(onDismissRequest = { showCreateAsnDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Create New Assignment",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = asnTitle,
                        onValueChange = { asnTitle = it },
                        label = { Text("Assignment Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = asnSubject,
                        onValueChange = { asnSubject = it },
                        label = { Text("Subject (e.g. Mathematics)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = asnDue,
                        onValueChange = { asnDue = it },
                        label = { Text("Submission Deadline") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = asnDesc,
                        onValueChange = { asnDesc = it },
                        label = { Text("Instructions & Exercises") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showCreateAsnDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (asnTitle.isNotBlank()) {
                                    onCreateAssignment(asnTitle, asnSubject, asnClass, asnDue, asnDesc, 20)
                                    showCreateAsnDialog = false
                                    asnTitle = ""
                                    asnDesc = ""
                                }
                            },
                            enabled = asnTitle.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Publish")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TeacherAnalyticsBox(modifier: Modifier = Modifier, label: String, value: String, color: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = color)
        }
    }
}

@Composable
private fun EmptyTeacherCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onAction: () -> Unit,
    actionText: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(NigerianGreen.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = NigerianGreen, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(actionText)
            }
        }
    }
}
