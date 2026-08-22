package com.example.ui.parent

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun ParentPortalScreen(
    reportCards: List<StudentReportCard>,
    feeInvoices: List<FeeInvoice>,
    paymentReceipts: List<PaymentReceipt>,
    selectedChildId: String,
    onSelectChild: (String) -> Unit,
    onOpenReportCard: (StudentReportCard) -> Unit,
    onOpenReceipt: (PaymentReceipt) -> Unit,
    onPayInvoice: (FeeInvoice) -> Unit,
    onShowToast: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf("overview") }
    var showMessageTeacherDialog by remember { mutableStateOf(false) }
    var teacherMessageText by remember { mutableStateOf("") }

    val currentReport = reportCards.find { it.studentId == selectedChildId } ?: reportCards.firstOrNull()
    val childInvoices = feeInvoices.filter { it.studentId == selectedChildId }
    val childReceipts = paymentReceipts.filter {
        val childName = currentReport?.studentName ?: ""
        it.studentName.contains(childName.split(" ").firstOrNull() ?: "")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Child Selector Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Ward:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))

                FilterChip(
                    selected = selectedChildId == "STU-0482",
                    onClick = { onSelectChild("STU-0482") },
                    label = { Text("Chinedu (SSS 3)", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )

                Spacer(modifier = Modifier.width(6.dp))

                FilterChip(
                    selected = selectedChildId == "STU-0621",
                    onClick = { onSelectChild("STU-0621") },
                    label = { Text("Amina (JSS 2)", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }

        // Sub Navigation Tabs
        ScrollableTabRow(
            selectedTabIndex = when (selectedTab) {
                "overview" -> 0
                "fees" -> 1
                "receipts" -> 2
                "attendance" -> 3
                "messages" -> 4
                else -> 0
            },
            edgePadding = 12.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = HilltopPrimary
        ) {
            listOf(
                "overview" to "Academic Progress",
                "fees" to "Pay Fees & Levies",
                "receipts" to "Receipts",
                "attendance" to "Attendance & Conduct",
                "messages" to "Teacher Messaging"
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
                "overview" -> {
                    // Child Summary Banner
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.linearGradient(
                                            listOf(HilltopSecondary, HilltopPrimaryDark)
                                        )
                                    )
                                    .padding(18.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = currentReport?.studentName ?: "Chinedu Emmanuel Okafor",
                                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                                color = Color.White
                                            )
                                            Text(
                                                text = "Adm No: ${currentReport?.admissionNo} • Class: ${currentReport?.className}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White.copy(alpha = 0.85f)
                                            )
                                        }
                                        Surface(
                                            color = AcademicGold,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "Position: #${currentReport?.classPosition ?: 2}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = Color.Black,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Card(
                                            modifier = Modifier.weight(1f),
                                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Text("Term Average", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f))
                                                Text("${String.format("%.1f", currentReport?.averageScore ?: 87.2)}%", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                            }
                                        }
                                        Card(
                                            modifier = Modifier.weight(1f),
                                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
                                        ) {
                                            Column(modifier = Modifier.padding(8.dp)) {
                                                Text("GPA Scale", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f))
                                                Text("${String.format("%.2f", currentReport?.gpa ?: 4.4)} / 5.0", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Terminal Report Card Card
                    if (currentReport != null) {
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
                                                text = "${currentReport.term} Performance Sheet",
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Text(
                                                text = "Session ${currentReport.session}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        Button(
                                            onClick = { onOpenReportCard(currentReport) },
                                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                            shape = RoundedCornerShape(10.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            modifier = Modifier.testTag("parent_view_report_btn")
                                        ) {
                                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Official PDF", style = MaterialTheme.typography.labelMedium)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    currentReport.scores.take(5).forEach { sc ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 3.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = sc.subjectName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "${String.format("%.0f", sc.totalScore)}%",
                                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                                    color = HilltopPrimary
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Surface(
                                                    color = NigerianGreen.copy(alpha = 0.15f),
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = sc.gradeRemark.grade,
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
                }

                "fees" -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = HilltopPrimary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Instant Bursary Reconciliation",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = HilltopPrimary
                                    )
                                    Text(
                                        text = "Pay tuition, boarding, PTA, and exam registration securely via Paystack, Flutterwave, or Direct Bank Transfer.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    items(childInvoices) { inv ->
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
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = inv.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                        Text(text = "Due Date: ${inv.dueDate}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                    }
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

                                Spacer(modifier = Modifier.height(10.dp))

                                // Itemized breakdown
                                inv.items.forEach { item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = item.name, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                        Text(text = "₦${"%,d".format(item.amount)}", style = MaterialTheme.typography.labelSmall, color = TextDark)
                                    }
                                }

                                Divider(modifier = Modifier.padding(vertical = 8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = "Total: ₦${"%,d".format(inv.totalAmount)}", style = MaterialTheme.typography.bodySmall)
                                        Text(text = "Paid: ₦${"%,d".format(inv.amountPaid)}", style = MaterialTheme.typography.bodySmall, color = StatusSuccess)
                                    }
                                    if (inv.balanceDue > 0) {
                                        Text(
                                            text = "Due: ₦${"%,d".format(inv.balanceDue)}",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = StatusError)
                                        )
                                    }
                                }

                                if (inv.balanceDue > 0) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = { onPayInvoice(inv) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("parent_pay_fee_btn"),
                                        colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Pay ₦${"%,d".format(inv.balanceDue)} with Paystack/Flutterwave")
                                    }
                                }
                            }
                        }
                    }
                }

                "receipts" -> {
                    item {
                        Text(
                            text = "Payment Receipts & Transaction Records",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    items(paymentReceipts) { rec ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenReceipt(rec) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = rec.receiptNumber, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = HilltopPrimary)
                                    Text(text = "Student: ${rec.studentName}", style = MaterialTheme.typography.labelSmall, color = TextDark)
                                    Text(text = "${rec.paymentGateway} • ${rec.paymentDate}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "₦${"%,d".format(rec.amountPaid)}",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = NigerianGreen)
                                    )
                                    Text(
                                        text = "View Receipt",
                                        style = MaterialTheme.typography.labelSmall.copy(color = HilltopPrimary)
                                    )
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
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Attendance & Disciplinary Record",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = HilltopPrimary
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Card(
                                        modifier = Modifier.weight(1f),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4))
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("Days Present", style = MaterialTheme.typography.labelSmall, color = NigerianGreen)
                                            Text("68 Days", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = NigerianGreen)
                                        }
                                    }
                                    Card(
                                        modifier = Modifier.weight(1f),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("Days Absent", style = MaterialTheme.typography.labelSmall, color = StatusError)
                                            Text("2 Days (Sick)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = StatusError)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "House Master's Behavioural Note:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "\"Exemplary conduct in the boarding house. Demonstrates strong leadership as Senior Prefect, punctual to prep, and maintains spotless hostel quarters.\"",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                "messages" -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Class Teacher & Counsellor Desk",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = HilltopPrimary
                                )
                                Text(
                                    text = "Direct communication channel with Mr. Babatunde Adeyemi (Class Teacher).",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(text = "Mr. Babatunde Adeyemi:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = HilltopPrimary)
                                        Text(text = "\"Good day Barrister Samuel. Chinedu is performing exceptionally in Further Maths and Physics. We recommend extra past questions practice for the WAEC distinction target.\"", style = MaterialTheme.typography.bodySmall)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = { showMessageTeacherDialog = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Send Reply to Class Teacher")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showMessageTeacherDialog) {
        Dialog(onDismissRequest = { showMessageTeacherDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Message to Mr. B. Adeyemi",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimaryDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = teacherMessageText,
                        onValueChange = { teacherMessageText = it },
                        placeholder = { Text("Type message or request appointment...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines = 6
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showMessageTeacherDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (teacherMessageText.isNotBlank()) {
                                    onShowToast("Message sent to Class Teacher!")
                                    showMessageTeacherDialog = false
                                    teacherMessageText = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Send Message")
                        }
                    }
                }
            }
        }
    }
}
