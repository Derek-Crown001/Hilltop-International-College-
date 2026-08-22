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
    onBroadcastAnnouncement: (title: String, audience: String, message: String, isUrgent: Boolean) -> Unit,
    onShowToast: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf("analytics") }
    var showBroadcastDialog by remember { mutableStateOf(false) }
    var annTitle by remember { mutableStateOf("") }
    var annAudience by remember { mutableStateOf("All") }
    var annMessage by remember { mutableStateOf("") }
    var annUrgent by remember { mutableStateOf(false) }

    var viewingAdmissionLetter by remember { mutableStateOf<AdmissionApplication?>(null) }

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
                        text = "Office of the Principal & Governing Board",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Central Administration & Bursary Console",
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
                "admissions" to "Admissions Desk",
                "finance" to "Finance & Bursary",
                "hostels" to "Hostels & Boarding",
                "transport" to "School Bus Fleet",
                "library" to "Library System"
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
                    // KPI Stat Overview
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
                                            listOf(Color(0xFF4A148C), Color(0xFF6A1B9A), HilltopPrimary)
                                        )
                                    )
                                    .padding(18.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Executive Campus Overview",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                    Text(
                                        text = "2025/2026 Academic Session • 2nd Term Active",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        AdminMetricCard(Modifier.weight(1f), "Total Students", "1,450", "+12% YoY")
                                        AdminMetricCard(Modifier.weight(1f), "Teaching Staff", "85", "100% Certified")
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        AdminMetricCard(Modifier.weight(1f), "Total Revenue", "₦142.5M", "Paystack/Bank")
                                        AdminMetricCard(Modifier.weight(1f), "Outstanding", "₦18.2M", "124 Invoices")
                                    }
                                }
                            }
                        }
                    }

                    // Quick Management Actions
                    item {
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
                                Text("Broadcast Alert")
                            }

                            OutlinedButton(
                                onClick = { onShowToast("System backup completed! Encrypted archive generated.") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Backup, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Backup DB")
                            }
                        }
                    }

                    // Admissions & Payment Summary Cards
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Active Application Pipeline",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "3 new admission candidate files pending entrance CBT and interview review.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { selectedTab = "admissions" },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Open Admissions Desk")
                                }
                            }
                        }
                    }
                }

                "admissions" -> {
                    item {
                        Text(
                            text = "2026/2027 Admission Applications",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

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
                                            Text("Grant Admission", fontSize = 12.sp)
                                        }
                                    }

                                    OutlinedButton(
                                        onClick = { viewingAdmissionLetter = app },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Admission Letter", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                "finance" -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Paystack & Flutterwave Payment Gateway Logs",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = HilltopPrimary
                                )
                                Text(
                                    text = "Automated transaction settlement into Zenith Bank Main Operations Account.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

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

                "hostels" -> {
                    item {
                        Text(
                            text = "Boarding House & Hostel Allocation",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

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
                                    Surface(
                                        color = if (room.occupied == room.capacity) StatusError.copy(alpha = 0.15f) else StatusSuccess.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = if (room.occupied == room.capacity) "FULL" else "${room.capacity - room.occupied} Available",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = if (room.occupied == room.capacity) StatusError else StatusSuccess,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                "transport" -> {
                    item {
                        Text(
                            text = "College Bus Transit Routes",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

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
                                Text(text = "Departure: ${route.departureTime}", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = StatusSuccess)
                            }
                        }
                    }
                }

                "library" -> {
                    item {
                        Text(
                            text = "Digital Library & Textbook Inventory",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

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
                                    Text(text = "Shelf Location: ${book.shelfLocation}", style = MaterialTheme.typography.labelSmall, color = HilltopPrimary)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "${book.availableCopies} / ${book.totalCopies}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                    Text(text = "Copies In Stock", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Broadcast Notice Dialog
    if (showBroadcastDialog) {
        Dialog(onDismissRequest = { showBroadcastDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Broadcast Notice to Portals",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimaryDark
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
                            Text("Publish Broadcast")
                        }
                    }
                }
            }
        }
    }

    // Admission Letter Preview
    if (viewingAdmissionLetter != null) {
        val app = viewingAdmissionLetter!!
        Dialog(onDismissRequest = { viewingAdmissionLetter = null }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite)
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

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Dear ${app.parentGuardianName},\n\nWe are pleased to inform you that following the entrance examination (CBT Score: ${app.cbtScore ?: 88}%), ${app.applicantFullName} has been offered provisional admission into ${app.entryClass} at Hilltop International College for the 2026/2027 Academic Session.\n\nPlease proceed to accept the offer and complete the acceptance fee payment on the portal before March 30, 2026.\n\nYours faithfully,\nDr. Emmanuel Okafor\nPrincipal",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 20.sp,
                        color = TextDark
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
