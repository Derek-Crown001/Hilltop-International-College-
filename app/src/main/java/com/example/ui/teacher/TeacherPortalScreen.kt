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
    onToggleAttendance: (studentId: String) -> Unit,
    onCreateAssignment: (title: String, subject: String, targetClass: String, dueDate: String, desc: String, maxScore: Int) -> Unit,
    onShowToast: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf("scores") }
    var selectedClass by remember { mutableStateOf("SSS 3 Science A") }
    var selectedSubject by remember { mutableStateOf("Mathematics") }

    // State for Score Entry Dialog
    var editingScoreInfo by remember { mutableStateOf<Pair<String, SubjectScore>?>(null) } // reportId to SubjectScore
    var editCa1 by remember { mutableStateOf("9.5") }
    var editCa2 by remember { mutableStateOf("9.0") }
    var editTest by remember { mutableStateOf("9.0") }
    var editExam by remember { mutableStateOf("64.0") }

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
                        text = "Mr. Babatunde Adeyemi",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "HOD Science & Mathematics • Staff ID: HIC/TCH/012",
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
                "scores" to "Score Entry (CA & Exam)",
                "attendance" to "Daily Attendance",
                "assignments" to "Manage Homework",
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
                                Text(
                                    text = "Continuous Assessment & Exam Score Recording",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = HilltopPrimary
                                )
                                Text(
                                    text = "Grading standard: 1st CA (10%) + 2nd CA (10%) + Mid-Term Test (10%) + Terminal Exam (70%) = 100%",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    FilterChip(
                                        selected = selectedClass == "SSS 3 Science A",
                                        onClick = { selectedClass = "SSS 3 Science A" },
                                        label = { Text("SSS 3 Science A", fontSize = 11.sp) },
                                        modifier = Modifier.weight(1f)
                                    )
                                    FilterChip(
                                        selected = selectedClass == "JSS 2 Gold",
                                        onClick = { selectedClass = "JSS 2 Gold" },
                                        label = { Text("JSS 2 Gold", fontSize = 11.sp) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    if (report != null) {
                        item {
                            Text(
                                text = "Student: ${report.studentName} (${report.admissionNo})",
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
                                    Column {
                                        Text(
                                            text = "Class Register: SSS 3 Science A",
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
                                        onClick = { onShowToast("Attendance register synchronized!") },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen)
                                    ) {
                                        Text("Save Register")
                                    }
                                }
                            }
                        }
                    }

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
                                    Text(text = "${entry.admissionNo} • ${entry.remarks}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
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

                "assignments" -> {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Published Homework",
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
                                Text("New Assignment")
                            }
                        }
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

                "analytics" -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Class Performance Summary (SSS 3 Science A)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = HilltopPrimary
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    TeacherAnalyticsBox(Modifier.weight(1f), "Class Average", "82.4%", NigerianGreen)
                                    TeacherAnalyticsBox(Modifier.weight(1f), "Pass Rate", "100%", HilltopPrimary)
                                    TeacherAnalyticsBox(Modifier.weight(1f), "Distinctions", "31 / 38", AcademicGold)
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "Subject Mean Score Comparison:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                listOf(
                                    "Mathematics" to 86.5,
                                    "Physics" to 84.0,
                                    "Chemistry" to 81.2,
                                    "Further Mathematics" to 87.0
                                ).forEach { (sub, mean) ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = sub, style = MaterialTheme.typography.bodySmall)
                                        Text(text = "$mean%", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = HilltopPrimary)
                                    }
                                }
                            }
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
                colors = CardDefaults.cardColors(containerColor = CardWhite)
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
                        color = AcademicBgLight,
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
                colors = CardDefaults.cardColors(containerColor = CardWhite)
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
        colors = CardDefaults.cardColors(containerColor = AcademicBgLight)
    ) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = color)
        }
    }
}
