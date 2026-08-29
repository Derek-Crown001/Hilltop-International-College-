package com.example.ui.admin

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
fun AdminPortalScreen(
    admissions: List<AdmissionApplication>,
    feeInvoices: List<FeeInvoice>,
    paymentReceipts: List<PaymentReceipt>,
    hostelRooms: List<HostelRoom>,
    transportRoutes: List<TransportRoute>,
    libraryBooks: List<LibraryBook>,
    onUpdateAdmissionStatus: (String, String) -> Unit,
    onDeleteAdmission: (String) -> Unit = {},
    onSubmitNewAdmission: (AdmissionApplication) -> Unit = {},
    onBroadcastAnnouncement: (title: String, audience: String, message: String, isUrgent: Boolean) -> Unit,
    onAddNewsArticle: (title: String, category: String, author: String, summary: String, content: String) -> Unit = { _, _, _, _, _ -> },
    onCreateFeeInvoice: (studentName: String, admissionNo: String, className: String, term: String, session: String, dueDate: String, items: List<FeeItem>) -> Unit = { _, _, _, _, _, _, _ -> },
    onRecordManualPayment: (invoiceId: String, studentName: String, admissionNo: String, amountPaid: Long, payerName: String, channel: String) -> Unit = { _, _, _, _, _, _ -> },
    onAddHostelRoom: (roomNumber: String, hallName: String, capacity: Int, floor: String, prefectName: String) -> Unit = { _, _, _, _, _ -> },
    onDeleteHostelRoom: (String) -> Unit = {},
    onAddTransportRoute: (routeCode: String, routeName: String, busNumber: String, driverName: String, driverPhone: String, pickupPoints: List<String>, departureTime: String, feePerTerm: Long) -> Unit = { _, _, _, _, _, _, _, _ -> },
    onDeleteTransportRoute: (String) -> Unit = {},
    onAddLibraryBook: (title: String, author: String, isbn: String, category: String, shelfLocation: String, totalCopies: Int) -> Unit = { _, _, _, _, _, _ -> },
    onDeleteLibraryBook: (String) -> Unit = {},
    onClearAllLogs: () -> Unit = {},
    onLoadSampleData: () -> Unit = {},
    onShowToast: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf("analytics") }

    // Dialog States
    var showBroadcastDialog by remember { mutableStateOf(false) }
    var showAddNewsDialog by remember { mutableStateOf(false) }
    var showAddAdmissionDialog by remember { mutableStateOf(false) }
    var showCreateInvoiceDialog by remember { mutableStateOf(false) }
    var showRecordPaymentDialog by remember { mutableStateOf(false) }
    var showAddHostelDialog by remember { mutableStateOf(false) }
    var showAddRouteDialog by remember { mutableStateOf(false) }
    var showAddBookDialog by remember { mutableStateOf(false) }
    var showConfirmClearDialog by remember { mutableStateOf(false) }

    var viewingAdmissionLetter by remember { mutableStateOf<AdmissionApplication?>(null) }

    // Broadcast Form
    var annTitle by remember { mutableStateOf("") }
    var annAudience by remember { mutableStateOf("All") }
    var annMessage by remember { mutableStateOf("") }
    var annUrgent by remember { mutableStateOf(false) }

    // News Form
    var newsTitle by remember { mutableStateOf("") }
    var newsCategory by remember { mutableStateOf("Campus Life") }
    var newsAuthor by remember { mutableStateOf("School Administration") }
    var newsSummary by remember { mutableStateOf("") }
    var newsContent by remember { mutableStateOf("") }

    // Admission Form
    var admFullName by remember { mutableStateOf("") }
    var admGender by remember { mutableStateOf("Male") }
    var admDOB by remember { mutableStateOf("12th May 2012") }
    var admState by remember { mutableStateOf("Lagos State") }
    var admClass by remember { mutableStateOf("JSS 1") }
    var admParentName by remember { mutableStateOf("") }
    var admParentPhone by remember { mutableStateOf("") }
    var admParentEmail by remember { mutableStateOf("") }
    var admAddress by remember { mutableStateOf("") }
    var admPrevSchool by remember { mutableStateOf("") }

    // Invoice Form
    var invStudentName by remember { mutableStateOf("") }
    var invAdmNo by remember { mutableStateOf("") }
    var invClass by remember { mutableStateOf("SSS 1") }
    var invTerm by remember { mutableStateOf("2nd Term") }
    var invSession by remember { mutableStateOf("2025/2026") }
    var invDueDate by remember { mutableStateOf("March 30, 2026") }
    var invTuition by remember { mutableStateOf("180000") }
    var invIct by remember { mutableStateOf("25000") }
    var invLab by remember { mutableStateOf("15000") }

    // Payment Form
    var payStudentName by remember { mutableStateOf("") }
    var payAdmNo by remember { mutableStateOf("") }
    var payAmount by remember { mutableStateOf("220000") }
    var payPayerName by remember { mutableStateOf("") }
    var payChannel by remember { mutableStateOf("Zenith Bank Transfer") }

    // Hostel Form
    var hostRoomNo by remember { mutableStateOf("Room A-101") }
    var hostHallName by remember { mutableStateOf("Nelson Mandela Hall") }
    var hostFloor by remember { mutableStateOf("1st Floor") }
    var hostCapacity by remember { mutableStateOf("6") }
    var hostPrefect by remember { mutableStateOf("") }

    // Route Form
    var routeCode by remember { mutableStateOf("RT-01") }
    var routeName by remember { mutableStateOf("Lekki Phase 1 - Ikoyi") }
    var routeBusNo by remember { mutableStateOf("Toyota Coaster - HIC 01") }
    var routeDriverName by remember { mutableStateOf("Mr. Sunday Okoro") }
    var routeDriverPhone by remember { mutableStateOf("+234 803 111 2233") }
    var routePickups by remember { mutableStateOf("Admiralty Way, Bourdillon, Parkview") }
    var routeDeparture by remember { mutableStateOf("6:45 AM") }
    var routeFee by remember { mutableStateOf("60000") }

    // Library Form
    var bookTitle by remember { mutableStateOf("") }
    var bookAuthor by remember { mutableStateOf("") }
    var bookIsbn by remember { mutableStateOf("") }
    var bookCategory by remember { mutableStateOf("Mathematics") }
    var bookShelf by remember { mutableStateOf("Shelf M-01") }
    var bookCopies by remember { mutableStateOf("30") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Admin Header
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
                        .background(Color(0xFF6A1B9A).copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color(0xFF6A1B9A), modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "School Owner & Admin Console",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Full Management, Add & Customization Portal",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
        }

        // Sub Navigation Tabs
        ScrollableTabRow(
            selectedTabIndex = when (selectedTab) {
                "analytics" -> 0
                "admissions" -> 1
                "finance" -> 2
                "hostels" -> 3
                "transport" -> 4
                "library" -> 5
                else -> 0
            },
            edgePadding = 12.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color(0xFF6A1B9A)
        ) {
            listOf(
                "analytics" to "Dashboard",
                "admissions" to "Admissions (${admissions.size})",
                "finance" to "Finance & Invoices",
                "hostels" to "Hostels (${hostelRooms.size})",
                "transport" to "Bus Fleet (${transportRoutes.size})",
                "library" to "Library (${libraryBooks.size})"
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
                "analytics" -> {
                    // KPI Stat Overview (Calculated from real data)
                    item {
                        val totalRevenue = paymentReceipts.sumOf { it.amountPaid }
                        val totalInvoiced = feeInvoices.sumOf { it.totalAmount }
                        val totalPending = (totalInvoiced - totalRevenue).coerceAtLeast(0L)

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
                                            listOf(Color(0xFF4A148C), Color(0xFF6A1B9A), HilltopPrimary)
                                        )
                                    )
                                    .padding(18.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "School Administration Dashboard",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Live Campus Management & Records",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        AdminMetricCard(Modifier.weight(1f), "Admissions", "${admissions.size}", "Candidate Files")
                                        AdminMetricCard(Modifier.weight(1f), "Invoices Issued", "${feeInvoices.size}", "Billings")
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        AdminMetricCard(Modifier.weight(1f), "Recorded Revenue", "₦${"%,d".format(totalRevenue)}", "${paymentReceipts.size} Receipts")
                                        AdminMetricCard(Modifier.weight(1f), "Unsettled Fees", "₦${"%,d".format(totalPending)}", "Pending")
                                    }
                                }
                            }
                        }
                    }

                    // Quick Management Actions (Add Whatever You Want)
                    item {
                        Text(
                            text = "Add / Manage School Data",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showBroadcastDialog = true },
                                modifier = Modifier.weight(1f).testTag("broadcast_notice_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Broadcast Alert", fontSize = 12.sp)
                            }

                            Button(
                                onClick = { showAddNewsDialog = true },
                                modifier = Modifier.weight(1f).testTag("add_news_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.PostAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add News Post", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showCreateInvoiceDialog = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Issue Invoice", fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = { showRecordPaymentDialog = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Record Payment", fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showConfirmClearDialog = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusError),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.DeleteSweep, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Clear All Logs", fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = { onLoadSampleData() },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.PlaylistAddCheck, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Load Templates", fontSize = 12.sp)
                            }
                        }
                    }
                }

                "admissions" -> {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Admission Applications (${admissions.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Button(
                                onClick = { showAddAdmissionDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Candidate", fontSize = 12.sp)
                            }
                        }
                    }

                    if (admissions.isEmpty()) {
                        item {
                            EmptyStateCard(
                                title = "No Admission Applications",
                                subtitle = "No candidate records registered. Tap '+ Add Candidate' to create a new applicant entry.",
                                icon = Icons.Default.PersonAdd,
                                onActionClick = { showAddAdmissionDialog = true },
                                actionText = "Register Candidate"
                            )
                        }
                    } else {
                        items(admissions) { app ->
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
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = app.applicantFullName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                            Text(text = "App No: ${app.applicationNo} • Target: ${app.entryClass}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                        }
                                        Surface(
                                            color = when (app.status) {
                                                "Offered Admission" -> NigerianGreen.copy(alpha = 0.15f)
                                                "Shortlisted for CBT" -> HilltopPrimary.copy(alpha = 0.15f)
                                                else -> StatusWarning.copy(alpha = 0.15f)
                                            },
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = app.status,
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = when (app.status) {
                                                    "Offered Admission" -> NigerianGreen
                                                    "Shortlisted for CBT" -> HilltopPrimary
                                                    else -> StatusWarning
                                                },
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(text = "Parent: ${app.parentGuardianName} (${app.parentPhone})", style = MaterialTheme.typography.bodySmall)
                                    Text(text = "Previous School: ${app.previousSchool}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                    if (app.cbtScore != null) {
                                        Text(text = "Entrance CBT Score: ${app.cbtScore}%", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = NigerianGreen))
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        if (app.status != "Offered Admission") {
                                            Button(
                                                onClick = { onUpdateAdmissionStatus(app.applicationNo, "Offered Admission") },
                                                colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Grant Offer", fontSize = 12.sp)
                                            }
                                        }

                                        OutlinedButton(
                                            onClick = { viewingAdmissionLetter = app },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("View Letter", fontSize = 12.sp)
                                        }

                                        IconButton(
                                            onClick = { onDeleteAdmission(app.applicationNo) }
                                        ) {
                                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = StatusError)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "finance" -> {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Bursary & Invoices",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { showCreateInvoiceDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("New Invoice", fontSize = 11.sp)
                                }
                                Button(
                                    onClick = { showRecordPaymentDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("Record Payment", fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    // Invoices Section
                    item {
                        Text(
                            text = "Issued Student Invoices (${feeInvoices.size})",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = HilltopPrimary
                        )
                    }

                    if (feeInvoices.isEmpty()) {
                        item {
                            EmptyStateCard(
                                title = "No Fee Invoices Issued",
                                subtitle = "Tap '+ New Invoice' to create and bill student fees.",
                                icon = Icons.Default.ReceiptLong,
                                onActionClick = { showCreateInvoiceDialog = true },
                                actionText = "Create Fee Invoice"
                            )
                        }
                    } else {
                        items(feeInvoices) { inv ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(text = inv.studentName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                            Text(text = "ID: ${inv.studentId} • ${inv.className} (${inv.term})", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = "₦${"%,d".format(inv.totalAmount)}",
                                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = HilltopPrimary)
                                            )
                                            Surface(
                                                color = if (inv.status == PaymentStatus.PAID) StatusSuccess.copy(alpha = 0.15f) else StatusWarning.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = inv.status.name,
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = if (inv.status == PaymentStatus.PAID) StatusSuccess else StatusWarning,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "Due: ${inv.dueDate} • Items: ${inv.items.joinToString { it.name }}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }

                    // Payment Receipts Section
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Payment Settlement Receipts (${paymentReceipts.size})",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = NigerianGreen
                        )
                    }

                    if (paymentReceipts.isEmpty()) {
                        item {
                            EmptyStateCard(
                                title = "No Payment Receipts Yet",
                                subtitle = "Receipts will appear here when parents pay via Paystack or when manually recorded.",
                                icon = Icons.Default.Payments,
                                onActionClick = { showRecordPaymentDialog = true },
                                actionText = "Record Manual Receipt"
                            )
                        }
                    } else {
                        items(paymentReceipts) { rec ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = rec.transactionRef, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                        Text(text = "${rec.studentName} • ${rec.payerName}", style = MaterialTheme.typography.labelSmall, color = TextDark)
                                        Text(text = "${rec.paymentGateway} • ${rec.paymentDate}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "₦${"%,d".format(rec.amountPaid)}",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = NigerianGreen)
                                        )
                                        Surface(color = StatusSuccess.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp)) {
                                            Text(text = "Settled", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = StatusSuccess, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "hostels" -> {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Hostels & Boarding (${hostelRooms.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Button(
                                onClick = { showAddHostelDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Room", fontSize = 12.sp)
                            }
                        }
                    }

                    if (hostelRooms.isEmpty()) {
                        item {
                            EmptyStateCard(
                                title = "No Hostel Rooms Registered",
                                subtitle = "Add student halls and room allocations to manage boarding.",
                                icon = Icons.Default.Bed,
                                onActionClick = { showAddHostelDialog = true },
                                actionText = "Add Hostel Room"
                            )
                        }
                    } else {
                        items(hostelRooms) { room ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = "${room.roomNumber} (${room.floor})", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                        Text(text = room.hallName, style = MaterialTheme.typography.bodySmall, color = HilltopPrimary)
                                        Text(text = "Prefect in Charge: ${room.prefectName}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "${room.occupied} / ${room.capacity} Beds",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        IconButton(onClick = { onDeleteHostelRoom(room.roomNumber) }) {
                                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = StatusError)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "transport" -> {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Bus Transit Routes (${transportRoutes.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Button(
                                onClick = { showAddRouteDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Route", fontSize = 12.sp)
                            }
                        }
                    }

                    if (transportRoutes.isEmpty()) {
                        item {
                            EmptyStateCard(
                                title = "No School Bus Routes",
                                subtitle = "Configure bus pickup routes, drivers, and termly transit fees.",
                                icon = Icons.Default.DirectionsBus,
                                onActionClick = { showAddRouteDialog = true },
                                actionText = "Add Bus Route"
                            )
                        }
                    } else {
                        items(transportRoutes) { route ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "${route.routeCode}: ${route.routeName}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                        Text(text = "₦${"%,d".format(route.feePerTerm)}/Term", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = HilltopPrimary)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "Vehicle: ${route.busNumber} • Driver: ${route.driverName} (${route.driverPhone})", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = "Pickup Points: ${route.pickupPoints.joinToString(" • ")}", style = MaterialTheme.typography.bodySmall)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "Departure: ${route.departureTime}", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = StatusSuccess)
                                        IconButton(onClick = { onDeleteTransportRoute(route.routeCode) }) {
                                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = StatusError)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "library" -> {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Library Catalogue (${libraryBooks.size})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Button(
                                onClick = { showAddBookDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Book", fontSize = 12.sp)
                            }
                        }
                    }

                    if (libraryBooks.isEmpty()) {
                        item {
                            EmptyStateCard(
                                title = "Library Catalogue Empty",
                                subtitle = "Add textbooks and literature titles to the school library inventory.",
                                icon = Icons.Default.MenuBook,
                                onActionClick = { showAddBookDialog = true },
                                actionText = "Add Library Book"
                            )
                        }
                    } else {
                        items(libraryBooks) { book ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = book.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                        Text(text = "${book.author} • ISBN: ${book.isbn}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                        Text(text = "Shelf: ${book.shelfLocation} • ${book.category}", style = MaterialTheme.typography.labelSmall, color = HilltopPrimary)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(text = "${book.availableCopies} / ${book.totalCopies}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                        Text(text = "Copies In Stock", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                        IconButton(onClick = { onDeleteLibraryBook(book.id) }) {
                                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = StatusError)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // --- Dialogs ---

    // 1. Broadcast Alert Dialog
    if (showBroadcastDialog) {
        Dialog(onDismissRequest = { showBroadcastDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Broadcast Notice to Portals",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = annTitle,
                        onValueChange = { annTitle = it },
                        label = { Text("Bulletin Headline") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = annMessage,
                        onValueChange = { annMessage = it },
                        label = { Text("Announcement Body") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = annUrgent, onCheckedChange = { annUrgent = it })
                        Text(text = "Mark as High Priority / Urgent Alert", style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showBroadcastDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (annTitle.isNotBlank() && annMessage.isNotBlank()) {
                                    onBroadcastAnnouncement(annTitle, annAudience, annMessage, annUrgent)
                                    showBroadcastDialog = false
                                    annTitle = ""
                                    annMessage = ""
                                }
                            },
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

    // 2. Add News Bulletin Dialog
    if (showAddNewsDialog) {
        Dialog(onDismissRequest = { showAddNewsDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Add News Article / Event",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NigerianGreen
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = newsTitle,
                        onValueChange = { newsTitle = it },
                        label = { Text("Article Headline") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = newsCategory,
                        onValueChange = { newsCategory = it },
                        label = { Text("Category (e.g. Academic, Sports, Events)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = newsSummary,
                        onValueChange = { newsSummary = it },
                        label = { Text("Brief Summary") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = newsContent,
                        onValueChange = { newsContent = it },
                        label = { Text("Full Article Content") },
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showAddNewsDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (newsTitle.isNotBlank() && newsSummary.isNotBlank()) {
                                    onAddNewsArticle(newsTitle, newsCategory, newsAuthor, newsSummary, newsContent.ifBlank { newsSummary })
                                    showAddNewsDialog = false
                                    newsTitle = ""
                                    newsSummary = ""
                                    newsContent = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Publish Article")
                        }
                    }
                }
            }
        }
    }

    // 3. Add Candidate / Admission Dialog
    if (showAddAdmissionDialog) {
        Dialog(onDismissRequest = { showAddAdmissionDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Register New Admission Candidate",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = admFullName,
                        onValueChange = { admFullName = it },
                        label = { Text("Applicant Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = admClass,
                            onValueChange = { admClass = it },
                            label = { Text("Target Class") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = admGender,
                            onValueChange = { admGender = it },
                            label = { Text("Gender") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = admParentName,
                        onValueChange = { admParentName = it },
                        label = { Text("Parent / Guardian Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = admParentPhone,
                        onValueChange = { admParentPhone = it },
                        label = { Text("Parent Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = admPrevSchool,
                        onValueChange = { admPrevSchool = it },
                        label = { Text("Previous School Attended") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showAddAdmissionDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (admFullName.isNotBlank() && admParentName.isNotBlank()) {
                                    val newApp = AdmissionApplication(
                                        applicationNo = "HIC-ADM-2026-${(1000..9999).random()}",
                                        applicantFullName = admFullName,
                                        gender = admGender,
                                        dateOfBirth = admDOB,
                                        stateOfOrigin = admState,
                                        entryClass = admClass,
                                        parentGuardianName = admParentName,
                                        parentPhone = admParentPhone.ifBlank { "+234 800 000 0000" },
                                        parentEmail = admParentEmail.ifBlank { "parent@example.com" },
                                        residentialAddress = admAddress.ifBlank { "Lagos, Nigeria" },
                                        previousSchool = admPrevSchool.ifBlank { "Primary School" },
                                        status = "Submitted",
                                        cbtScore = null,
                                        submissionDate = "Today"
                                    )
                                    onSubmitNewAdmission(newApp)
                                    showAddAdmissionDialog = false
                                    admFullName = ""
                                    admParentName = ""
                                    admParentPhone = ""
                                    admPrevSchool = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Add Candidate")
                        }
                    }
                }
            }
        }
    }

    // 4. Create Fee Invoice Dialog
    if (showCreateInvoiceDialog) {
        Dialog(onDismissRequest = { showCreateInvoiceDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Issue Student Fee Invoice",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = invStudentName,
                        onValueChange = { invStudentName = it },
                        label = { Text("Student Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = invAdmNo,
                            onValueChange = { invAdmNo = it },
                            label = { Text("Admission No") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = invClass,
                            onValueChange = { invClass = it },
                            label = { Text("Class") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = invTuition,
                        onValueChange = { invTuition = it },
                        label = { Text("Tuition Fee Amount (₦)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = invIct,
                        onValueChange = { invIct = it },
                        label = { Text("ICT & Lab Levy (₦)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showCreateInvoiceDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (invStudentName.isNotBlank()) {
                                    val tuitionAmt = invTuition.toLongOrNull() ?: 180000L
                                    val ictAmt = invIct.toLongOrNull() ?: 25000L
                                    val feeItems = listOf(
                                        FeeItem("Tuition & Academic Instruction", tuitionAmt),
                                        FeeItem("ICT, Computer & Digital Library", ictAmt)
                                    )
                                    onCreateFeeInvoice(
                                        invStudentName,
                                        invAdmNo.ifBlank { "HIC/2026/${(100..999).random()}" },
                                        invClass,
                                        invTerm,
                                        invSession,
                                        invDueDate,
                                        feeItems
                                    )
                                    showCreateInvoiceDialog = false
                                    invStudentName = ""
                                    invAdmNo = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Generate Invoice")
                        }
                    }
                }
            }
        }
    }

    // 5. Record Payment Dialog
    if (showRecordPaymentDialog) {
        Dialog(onDismissRequest = { showRecordPaymentDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Record Manual / Bank Payment",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = NigerianGreen
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = payStudentName,
                        onValueChange = { payStudentName = it },
                        label = { Text("Student Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = payPayerName,
                        onValueChange = { payPayerName = it },
                        label = { Text("Payer / Parent Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = payAmount,
                        onValueChange = { payAmount = it },
                        label = { Text("Amount Paid (₦)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = payChannel,
                        onValueChange = { payChannel = it },
                        label = { Text("Payment Channel (e.g. Zenith Bank Transfer, Cash)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showRecordPaymentDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (payStudentName.isNotBlank() && payPayerName.isNotBlank()) {
                                    val amt = payAmount.toLongOrNull() ?: 100000L
                                    onRecordManualPayment(
                                        "MAN-INV-001",
                                        payStudentName,
                                        payAdmNo.ifBlank { "HIC/2026/001" },
                                        amt,
                                        payPayerName,
                                        payChannel
                                    )
                                    showRecordPaymentDialog = false
                                    payStudentName = ""
                                    payPayerName = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Save Receipt")
                        }
                    }
                }
            }
        }
    }

    // 6. Add Hostel Room Dialog
    if (showAddHostelDialog) {
        Dialog(onDismissRequest = { showAddHostelDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Add Hostel Room Allocation",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = hostRoomNo,
                        onValueChange = { hostRoomNo = it },
                        label = { Text("Room Identifier (e.g. Room A-101)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = hostHallName,
                        onValueChange = { hostHallName = it },
                        label = { Text("Hall Name (e.g. Nelson Mandela Hall)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = hostCapacity,
                            onValueChange = { hostCapacity = it },
                            label = { Text("Beds Capacity") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = hostFloor,
                            onValueChange = { hostFloor = it },
                            label = { Text("Floor Location") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = hostPrefect,
                        onValueChange = { hostPrefect = it },
                        label = { Text("Prefect / Housemaster in Charge") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showAddHostelDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (hostRoomNo.isNotBlank()) {
                                    val cap = hostCapacity.toIntOrNull() ?: 6
                                    onAddHostelRoom(hostRoomNo, hostHallName, cap, hostFloor, hostPrefect.ifBlank { "House Prefect" })
                                    showAddHostelDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Add Room")
                        }
                    }
                }
            }
        }
    }

    // 7. Add Bus Route Dialog
    if (showAddRouteDialog) {
        Dialog(onDismissRequest = { showAddRouteDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Add School Bus Transit Route",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = routeCode,
                            onValueChange = { routeCode = it },
                            label = { Text("Code (RT-01)") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = routeName,
                            onValueChange = { routeName = it },
                            label = { Text("Route Name") },
                            modifier = Modifier.weight(2f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = routeBusNo,
                        onValueChange = { routeBusNo = it },
                        label = { Text("Bus / Vehicle Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = routeDriverName,
                            onValueChange = { routeDriverName = it },
                            label = { Text("Driver Name") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = routeDriverPhone,
                            onValueChange = { routeDriverPhone = it },
                            label = { Text("Driver Phone") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = routePickups,
                        onValueChange = { routePickups = it },
                        label = { Text("Pickup Points (comma-separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = routeDeparture,
                            onValueChange = { routeDeparture = it },
                            label = { Text("Departure Time") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = routeFee,
                            onValueChange = { routeFee = it },
                            label = { Text("Fee Per Term (₦)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showAddRouteDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (routeCode.isNotBlank() && routeName.isNotBlank()) {
                                    val fee = routeFee.toLongOrNull() ?: 60000L
                                    val points = routePickups.split(",").map { it.trim() }.filter { it.isNotBlank() }
                                    onAddTransportRoute(
                                        routeCode,
                                        routeName,
                                        routeBusNo,
                                        routeDriverName,
                                        routeDriverPhone,
                                        points.ifEmpty { listOf("Campus Gate") },
                                        routeDeparture,
                                        fee
                                    )
                                    showAddRouteDialog = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Save Route")
                        }
                    }
                }
            }
        }
    }

    // 8. Add Library Book Dialog
    if (showAddBookDialog) {
        Dialog(onDismissRequest = { showAddBookDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Add Library Book Title",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = bookTitle,
                        onValueChange = { bookTitle = it },
                        label = { Text("Book Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = bookAuthor,
                        onValueChange = { bookAuthor = it },
                        label = { Text("Author(s)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = bookCategory,
                            onValueChange = { bookCategory = it },
                            label = { Text("Subject / Category") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = bookShelf,
                            onValueChange = { bookShelf = it },
                            label = { Text("Shelf Location") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = bookCopies,
                        onValueChange = { bookCopies = it },
                        label = { Text("Total Copies Stocked") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showAddBookDialog = false }, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (bookTitle.isNotBlank() && bookAuthor.isNotBlank()) {
                                    val copies = bookCopies.toIntOrNull() ?: 20
                                    onAddLibraryBook(
                                        bookTitle,
                                        bookAuthor,
                                        bookIsbn.ifBlank { "978-${(1000000..9999999).random()}" },
                                        bookCategory,
                                        bookShelf,
                                        copies
                                    )
                                    showAddBookDialog = false
                                    bookTitle = ""
                                    bookAuthor = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Add Book")
                        }
                    }
                }
            }
        }
    }

    // 9. Confirm Clear All Logs Dialog
    if (showConfirmClearDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmClearDialog = false },
            title = { Text("Clear All Logs & Records?") },
            text = { Text("This will erase all demo and saved logs across admissions, invoices, hostel rooms, and library entries, providing a clean slate for the school owner.") },
            confirmButton = {
                Button(
                    onClick = {
                        onClearAllLogs()
                        showConfirmClearDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusError)
                ) {
                    Text("Clear All")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showConfirmClearDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Admission Letter Preview
    if (viewingAdmissionLetter != null) {
        val app = viewingAdmissionLetter!!
        Dialog(onDismissRequest = { viewingAdmissionLetter = null }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "HILLTOP INTERNATIONAL COLLEGE",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "OFFICIAL PROVISIONAL ADMISSION LETTER",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = HilltopAccentDark,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Dear ${app.parentGuardianName},\n\nWe are pleased to inform you that following entrance assessment, ${app.applicantFullName} has been offered provisional admission into ${app.entryClass} at Hilltop International College.\n\nPlease proceed to complete acceptance fee clearance on the portal.\n\nYours faithfully,\nSchool Administrator",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewingAdmissionLetter = null },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary)
                    ) {
                        Text("Close Letter")
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminMetricCard(modifier: Modifier = Modifier, title: String, value: String, sub: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f))
            Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
            Text(text = sub, style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = AcademicGold)
        }
    }
}

@Composable
private fun EmptyStateCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onActionClick: () -> Unit,
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
                    .background(HilltopPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = HilltopPrimary, modifier = Modifier.size(28.dp))
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
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(actionText)
            }
        }
    }
}
