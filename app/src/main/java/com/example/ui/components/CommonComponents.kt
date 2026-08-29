package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollegeTopBar(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    onNotificationClick: () -> Unit,
    onAiChatClick: () -> Unit = {},
    onVoiceClick: () -> Unit = {},
    announcementsCount: Int = 3
) {
    var showRoleMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { showRoleMenu = true }
            ) {
                // School Crest Icon Badge
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(HilltopPrimary, HilltopAccent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Hilltop College Crest",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Hilltop College",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.2.sp
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(Color(currentRole.badgeColor))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentRole.displayName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Switch Portal Role",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        },
        actions = {
            // Gemini AI Chat Button
            IconButton(
                onClick = onAiChatClick,
                modifier = Modifier.testTag("gemini_ai_topbar_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Gemini AI Tutor & Assistant",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Gemini Voice Mode Button
            IconButton(
                onClick = onVoiceClick,
                modifier = Modifier.testTag("gemini_voice_topbar_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Gemini Voice Mode",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }

            // Portal Switcher Quick Button
            FilledTonalButton(
                onClick = { showRoleMenu = true },
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier
                    .height(34.dp)
                    .testTag("switch_portal_button"),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Portals",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            // Notification Bell
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.testTag("notification_button")
            ) {
                BadgedBox(
                    badge = {
                        if (announcementsCount > 0) {
                            Badge(
                                containerColor = StatusError,
                                contentColor = Color.White
                            ) {
                                Text(announcementsCount.toString())
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Announcements & Alerts",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )

    if (showRoleMenu) {
        RoleSwitcherDialog(
            currentRole = currentRole,
            onSelectRole = { role ->
                onRoleSelected(role)
                showRoleMenu = false
            },
            onDismiss = { showRoleMenu = false }
        )
    }
}

@Composable
fun RoleSwitcherDialog(
    currentRole: UserRole,
    onSelectRole: (UserRole) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = HilltopPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Select Portal Experience",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Explore Hilltop College from any role",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                UserRole.values().forEach { role ->
                    val isSelected = role == currentRole
                    val roleIcon = when (role) {
                        UserRole.GUEST -> Icons.Default.Language
                        UserRole.STUDENT -> Icons.Default.School
                        UserRole.PARENT -> Icons.Default.FamilyRestroom
                        UserRole.TEACHER -> Icons.Default.CastForEducation
                        UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                    }

                    val roleSubtext = when (role) {
                        UserRole.GUEST -> "Public School website, admissions, academics & fees"
                        UserRole.STUDENT -> "Chinedu Okafor (SSS 3) - Results, CBT, Timetable, Homework"
                        UserRole.PARENT -> "Barr. Samuel Okafor - Fee payment, Report card, Levies"
                        UserRole.TEACHER -> "Mr. B. Adeyemi - Score entry, Attendance & Assignments"
                        UserRole.ADMIN -> "Principal's Office - Finance, Approvals, Admissions & CMS"
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectRole(role) }
                            .testTag("role_option_${role.name.lowercase()}"),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp),
                        border = if (isSelected) borderBorder(2.dp, MaterialTheme.colorScheme.primary) else null
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(role.badgeColor).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = roleIcon,
                                    contentDescription = null,
                                    tint = Color(role.badgeColor),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = role.displayName,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = roleSubtext,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun borderBorder(width: androidx.compose.ui.unit.Dp, color: Color) =
    androidx.compose.foundation.BorderStroke(width, color)

@Composable
fun NotificationSheet(
    announcements: List<SchoolAnnouncement>,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                tint = HilltopPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "College Notice Board",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Text(
                        text = "Official administrative bulletins, exam reminders & PTA announcements",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(announcements) { item ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (item.isUrgent) Color(0xFFFFF3E0) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                ),
                                border = if (item.isUrgent) borderBorder(1.dp, StatusWarning) else null
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (item.isUrgent) {
                                            Surface(
                                                color = StatusWarning,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = "URGENT NOTICE",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        } else {
                                            Surface(
                                                color = HilltopPrimary.copy(alpha = 0.12f),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = "Audience: ${item.targetAudience}",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                                    color = HilltopPrimary,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = item.timestamp,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.message,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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

@Composable
fun PaystackCheckoutModal(
    invoice: FeeInvoice,
    onPaymentSuccess: (invoiceId: String, amount: Long, gateway: String, channel: String, payerName: String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedGateway by remember { mutableStateOf("Paystack") }
    var selectedChannel by remember { mutableStateOf("Debit Card") }
    var payerName by remember { mutableStateOf("Barrister Samuel Okafor") }
    var cardNumber by remember { mutableStateOf("5399 4120 8892 4218") }
    var cardExpiry by remember { mutableStateOf("09/28") }
    var cardCvv by remember { mutableStateOf("821") }
    var isProcessing by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = { if (!isProcessing) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Gateway Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Secure Payment Gateway",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Hilltop International College Portal",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            color = if (selectedGateway == "Paystack") Color(0xFF0BA4DB).copy(alpha = 0.15f) else Color(0xFFF5A623).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = selectedGateway,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (selectedGateway == "Paystack") Color(0xFF0BA4DB) else Color(0xFFE65100),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Invoice Amount Display
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = invoice.title,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Student: ${invoice.studentName}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                            Text(
                                text = "₦${"%,d".format(invoice.balanceDue)}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = HilltopPrimary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Select Gateway
                    Text(
                        text = "Choose Payment Gateway",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Paystack", "Flutterwave").forEach { gw ->
                            FilterChip(
                                selected = selectedGateway == gw,
                                onClick = { selectedGateway = gw },
                                label = { Text(gw) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Payment,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Payment Channels
                    Text(
                        text = "Payment Method",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Debit Card", "Bank Transfer", "USSD *737#").forEach { ch ->
                            FilterChip(
                                selected = selectedChannel == ch,
                                onClick = { selectedChannel = ch },
                                label = { Text(ch, fontSize = 11.sp) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    when (selectedChannel) {
                        "Debit Card" -> {
                            OutlinedTextField(
                                value = cardNumber,
                                onValueChange = { cardNumber = it },
                                label = { Text("Card Number (Mastercard / Verve / Visa)") },
                                leadingIcon = { Icon(Icons.Default.CreditCard, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = cardExpiry,
                                    onValueChange = { cardExpiry = it },
                                    label = { Text("Expiry (MM/YY)") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = cardCvv,
                                    onValueChange = { cardCvv = it },
                                    label = { Text("CVV") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }
                        }
                        "Bank Transfer" -> {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Pay via Zenith Bank Virtual Account",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = HilltopPrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Account: 1019283746",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Account Name: Hilltop Int'l College - Auto-Reconciliation",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "Transfer the exact sum of ₦${"%,d".format(invoice.balanceDue)}. Verification is instantaneous.",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = StatusSuccess
                                    )
                                }
                            }
                        }
                        "USSD *737#" -> {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Dial the code below on your registered phone:",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "*737*000*${invoice.balanceDue / 1000}#",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = HilltopPrimary
                                        )
                                    )
                                    Text(
                                        text = "Supported: GTBank, Zenith, Access, UBA, FirstBank",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            enabled = !isProcessing,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                isProcessing = true
                                onPaymentSuccess(
                                    invoice.invoiceId,
                                    invoice.balanceDue,
                                    "$selectedGateway Gateway",
                                    selectedChannel,
                                    payerName
                                )
                            },
                            enabled = !isProcessing,
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("confirm_payment_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary)
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pay ₦${"%,d".format(invoice.balanceDue)}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrintableReportCardModal(
    report: StudentReportCard,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Header with close & action
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Assessment,
                                contentDescription = null,
                                tint = HilltopPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Official Terminal Report Card",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = HilltopPrimaryDark
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextDark)
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 6.dp))

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        item {
                            // School Title Header (Official Letterhead format)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "HILLTOP INTERNATIONAL COLLEGE",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    ),
                                    color = HilltopPrimary,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Plot 12 Admiralty Way, Victoria Island / Lekki, Lagos, Nigeria",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Motto: Excellence, Character & Global Leadership",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = HilltopAccentDark
                                    ),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Student Biodata Matrix
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = AcademicBgLight,
                                    shape = RoundedCornerShape(8.dp),
                                    border = borderBorder(1.dp, Color(0xFFCBD5E1))
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                text = "Student Name: ",
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                color = TextDark
                                            )
                                            Text(
                                                text = report.studentName,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                                color = HilltopPrimary
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Adm No: ${report.admissionNo}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextDark
                                            )
                                            Text(
                                                text = "Class: ${report.className}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = TextDark
                                            )
                                            Text(
                                                text = "Session: ${report.session}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextDark
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "Term: ${report.term}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextDark
                                            )
                                            Text(
                                                text = "Position: ${report.classPosition} / ${report.totalStudentsInClass}",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = NigerianGreen
                                                )
                                            )
                                            Text(
                                                text = "Attendance: ${report.attendancePresent}/${report.attendanceTotalDays} days",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextDark
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "ACADEMIC PERFORMANCE SHEET (CA 30% + EXAM 70%)",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = HilltopPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            // Table Header
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = HilltopPrimary,
                                shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Subject",
                                        modifier = Modifier.weight(2.2f),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                    Text(
                                        text = "CA",
                                        modifier = Modifier.weight(0.8f),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "Exam",
                                        modifier = Modifier.weight(0.8f),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "Total",
                                        modifier = Modifier.weight(0.9f),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "Grade",
                                        modifier = Modifier.weight(0.8f),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        // Subject Score rows
                        items(report.scores) { score ->
                            val caTotal = score.ca1 + score.ca2 + score.testScore
                            val isEven = report.scores.indexOf(score) % 2 == 0

                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = if (isEven) Color(0xFFFAFAFA) else Color.White,
                                border = borderBorder(0.5.dp, Color(0xFFEEEEEE))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(2.2f)) {
                                        Text(
                                            text = score.subjectName,
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = TextDark,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = score.teacherRemark,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = TextMuted,
                                            maxLines = 1
                                        )
                                    }
                                    Text(
                                        text = String.format("%.0f", caTotal),
                                        modifier = Modifier.weight(0.8f),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextDark,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = String.format("%.0f", score.examScore),
                                        modifier = Modifier.weight(0.8f),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextDark,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = String.format("%.0f", score.totalScore),
                                        modifier = Modifier.weight(0.9f),
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = HilltopPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                    Surface(
                                        modifier = Modifier.weight(0.8f),
                                        color = when (score.gradeRemark.grade) {
                                            "A1" -> NigerianGreen.copy(alpha = 0.15f)
                                            "B2", "B3" -> HilltopPrimary.copy(alpha = 0.15f)
                                            "C4", "C5", "C6" -> StatusInfo.copy(alpha = 0.15f)
                                            else -> StatusWarning.copy(alpha = 0.15f)
                                        },
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = score.gradeRemark.grade,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = when (score.gradeRemark.grade) {
                                                "A1" -> NigerianGreen
                                                "B2", "B3" -> HilltopPrimary
                                                "C4", "C5", "C6" -> StatusInfo
                                                else -> StatusWarning
                                            },
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            // Summary Cards
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Card(
                                    modifier = Modifier.weight(1f),
                                    colors = CardDefaults.cardColors(containerColor = AcademicBgLight)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Total Marks", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                        Text(
                                            "${String.format("%.0f", report.totalMarks)} / ${report.scores.size * 100}",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                            color = TextDark
                                        )
                                    }
                                }
                                Card(
                                    modifier = Modifier.weight(1f),
                                    colors = CardDefaults.cardColors(containerColor = AcademicBgLight)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Term Average", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                        Text(
                                            "${String.format("%.1f", report.averageScore)}%",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = NigerianGreen
                                            )
                                        )
                                    }
                                }
                                Card(
                                    modifier = Modifier.weight(1f),
                                    colors = CardDefaults.cardColors(containerColor = AcademicBgLight)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("GPA Points", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                        Text(
                                            "${String.format("%.2f", report.gpa)} / 5.0",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = HilltopPrimary
                                            )
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Behavioral / Psychomotor Evaluation
                            Text(
                                text = "AFFECTIVE & PSYCHOMOTOR SKILLS ASSESSMENT",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = HilltopPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = AcademicBgLight,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    report.psychomotorSkills.entries.chunked(2).forEach { pair ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 2.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            pair.forEach { (skill, rating) ->
                                                Text(
                                                    text = "$skill: ${"★".repeat(rating)}${"☆".repeat(5 - rating)} ($rating/5)",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = TextDark,
                                                    modifier = Modifier.weight(1f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Comments & Endorsement
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "Class Teacher's Remark:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = TextDark
                                    )
                                    Text(
                                        text = "\"${report.classTeacherComment}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextDark
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Principal's Endorsement & Stamp:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = HilltopPrimary
                                    )
                                    Text(
                                        text = "\"${report.principalComment}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextDark
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Status: ${report.promotionStatus}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = NigerianGreen
                                            )
                                        )
                                        Text(
                                            text = "Resumption: ${report.nextTermBegins}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Bottom Action Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Close")
                        }
                        Button(
                            onClick = { onDismiss() },
                            modifier = Modifier.weight(1.5f),
                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Download PDF")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OfficialReceiptModal(
    receipt: PaymentReceipt,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Success Icon
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(StatusSuccess.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = StatusSuccess,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Payment Receipt",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = HilltopPrimaryDark
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Hilltop International College Bursary",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Amount Box
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = AcademicBgLight,
                        shape = RoundedCornerShape(12.dp),
                        border = borderBorder(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Amount Paid",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                            Text(
                                text = "₦${"%,d".format(receipt.amountPaid)}",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = HilltopPrimary
                                )
                            )
                            Surface(
                                color = StatusSuccess.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Status: ${receipt.status}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = StatusSuccess,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Details
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ReceiptDetailRow("Receipt No:", receipt.receiptNumber)
                        ReceiptDetailRow("Transaction Ref:", receipt.transactionRef)
                        ReceiptDetailRow("Student Name:", receipt.studentName)
                        ReceiptDetailRow("Admission No:", receipt.admissionNo)
                        ReceiptDetailRow("Paid By:", receipt.payerName)
                        ReceiptDetailRow("Gateway:", receipt.paymentGateway)
                        ReceiptDetailRow("Channel:", receipt.channel)
                        ReceiptDetailRow("Date & Time:", receipt.paymentDate)
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Done")
                        }
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1.5f),
                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Share / Print")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReceiptDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = TextDark,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
