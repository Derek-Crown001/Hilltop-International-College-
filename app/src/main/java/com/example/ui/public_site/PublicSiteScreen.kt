package com.example.ui.public_site

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.NewsArticle
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun PublicSiteScreen(
    newsArticles: List<NewsArticle>,
    onSelectRole: (UserRole) -> Unit,
    onOpenAdmissionForm: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var selectedSection by remember { mutableStateOf("home") }
    var viewingArticle by remember { mutableStateOf<NewsArticle?>(null) }
    var showWhatsAppData by remember { mutableStateOf(false) }
    var newsletterEmail by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Navigation Sub-header / Category Tabs
        ScrollableTabRow(
            selectedTabIndex = when (selectedSection) {
                "home" -> 0
                "academics" -> 1
                "admissions" -> 2
                "fees" -> 3
                "news" -> 4
                "facilities" -> 5
                "contact" -> 6
                else -> 0
            },
            edgePadding = 12.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = HilltopPrimary
        ) {
            listOf(
                "home" to "Home",
                "academics" to "Academics",
                "admissions" to "Admissions",
                "fees" to "School Fees",
                "news" to "News & Events",
                "facilities" to "Campus & Gallery",
                "contact" to "Contact & FAQs"
            ).forEachIndexed { index, (key, label) ->
                Tab(
                    selected = selectedSection == key,
                    onClick = { selectedSection = key },
                    text = {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (selectedSection == key) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    }
                )
            }
        }

        // Section Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 28.dp)
        ) {
            when (selectedSection) {
                "home" -> {
                    // Hero Banner Card
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
                                            listOf(HilltopPrimaryDark, HilltopPrimary, HilltopSecondary)
                                        )
                                    )
                                    .padding(20.dp)
                            ) {
                                Column {
                                    Surface(
                                        color = AcademicGold.copy(alpha = 0.25f),
                                        shape = RoundedCornerShape(20.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = AcademicGold,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "Premier Cambridge & WAEC International School",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = Color.White
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "Nurturing Global Leaders Through Academic Excellence & Moral Character",
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.Black,
                                            lineHeight = 28.sp
                                        ),
                                        color = Color.White
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Comprehensive Nigerian & British Secondary Education for Junior (JSS 1-3) and Senior (SSS 1-3) students in a world-class safe learning ecosystem.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = onOpenAdmissionForm,
                                            colors = ButtonDefaults.buttonColors(containerColor = HilltopAccent),
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier.weight(1f).testTag("apply_admission_btn")
                                        ) {
                                            Icon(Icons.Default.AppRegistration, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Apply Now", fontWeight = FontWeight.Bold)
                                        }

                                        OutlinedButton(
                                            onClick = { onSelectRole(UserRole.STUDENT) },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White),
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Portal Login")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Key Statistics Bar
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatMiniCard(
                                modifier = Modifier.weight(1f),
                                number = "100%",
                                label = "WAEC & IGCSE Pass",
                                icon = Icons.Default.EmojiEvents,
                                iconColor = AcademicGold
                            )
                            StatMiniCard(
                                modifier = Modifier.weight(1f),
                                number = "1,450+",
                                label = "Enrolled Scholars",
                                icon = Icons.Default.Groups,
                                iconColor = HilltopPrimary
                            )
                            StatMiniCard(
                                modifier = Modifier.weight(1f),
                                number = "85+",
                                label = "Master Educators",
                                icon = Icons.Default.Psychology,
                                iconColor = NigerianGreen
                            )
                        }
                    }

                    // Principal's Welcome Address
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(HilltopPrimary.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = HilltopPrimary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Dr. Emmanuel Okafor, Ph.D.",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "Principal & Chief Academic Officer",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = HilltopPrimary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "Welcome to Hilltop International College. For over two decades, we have committed ourselves to rigorous intellectual inquiry, moral integrity, and character formation. Our dual curriculum equips Nigerian youth to thrive at leading universities in Nigeria (UNILAG, UI, Covenant) and globally (Oxford, MIT, Toronto). We invite you to explore our vibrant college community.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 19.sp
                                )
                            }
                        }
                    }

                    // Quick Portal Access Cards
                    item {
                        Text(
                            text = "Access Academic Portals",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PortalQuickCard(
                                modifier = Modifier.weight(1f),
                                title = "Student Portal",
                                sub = "Results, CBT, Notes",
                                icon = Icons.Default.School,
                                color = HilltopPrimary,
                                onClick = { onSelectRole(UserRole.STUDENT) }
                            )
                            PortalQuickCard(
                                modifier = Modifier.weight(1f),
                                title = "Parents Portal",
                                sub = "Pay Fees, Progress",
                                icon = Icons.Default.FamilyRestroom,
                                color = HilltopSecondary,
                                onClick = { onSelectRole(UserRole.PARENT) }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            PortalQuickCard(
                                modifier = Modifier.weight(1f),
                                title = "Teacher Portal",
                                sub = "Scores & Attendance",
                                icon = Icons.Default.CastForEducation,
                                color = NigerianGreen,
                                onClick = { onSelectRole(UserRole.TEACHER) }
                            )
                            PortalQuickCard(
                                modifier = Modifier.weight(1f),
                                title = "Admin Portal",
                                sub = "Finance & Admissions",
                                icon = Icons.Default.AdminPanelSettings,
                                color = Color(0xFF6A1B9A),
                                onClick = { onSelectRole(UserRole.ADMIN) }
                            )
                        }
                    }

                    // Latest News Highlights
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Latest Campus News",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            TextButton(onClick = { selectedSection = "news" }) {
                                Text("View All", color = HilltopPrimary)
                            }
                        }

                        newsArticles.take(2).forEach { article ->
                            NewsCardItem(
                                article = article,
                                onClick = { viewingArticle = article }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                "academics" -> {
                    // Academics section
                    item {
                        AcademicProgramCard(
                            title = "Junior Secondary School (JSS 1 - JSS 3)",
                            badge = "BECE / Basic Education",
                            description = "Foundational holistic education integrating Basic Science, Basic Technology, Mathematics, English Studies, Business Studies, French, Computer Studies, Civic Education, and Agricultural Science.",
                            icon = Icons.Default.AutoStories,
                            color = HilltopPrimary
                        )
                    }

                    item {
                        AcademicProgramCard(
                            title = "Senior Secondary - STEM & Sciences (SSS 1 - SSS 3)",
                            badge = "WAEC / NECO / UTME",
                            description = "Rigorous training in Further Mathematics, Physics, Chemistry, Biology, Technical Drawing, and AI Computer Science in our cutting-edge laboratories for aspiring doctors, engineers, and tech innovators.",
                            icon = Icons.Default.Science,
                            color = HilltopAccentDark
                        )
                    }

                    item {
                        AcademicProgramCard(
                            title = "Senior Secondary - Arts, Humanities & Commercial",
                            badge = "WASSCE & Cambridge",
                            description = "Developing analytical thinkers through Literature in English, Government, Financial Accounting, Commerce, Economics, History, and French Language.",
                            icon = Icons.Default.AccountBalance,
                            color = NigerianGreen
                        )
                    }

                    item {
                        AcademicProgramCard(
                            title = "Cambridge IGCSE & SAT Preparatory Track",
                            badge = "Global Certification",
                            description = "Specialized international exam coaching enabling seamless admissions into Ivy League, Russell Group, and prestigious worldwide universities with scholarship readiness.",
                            icon = Icons.Default.Public,
                            color = AcademicGold
                        )
                    }
                }

                "admissions" -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = "2026/2027 Admissions Guide",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = HilltopPrimary
                                )
                                Text(
                                    text = "Admissions are currently ongoing for JSS 1, JSS 2, and SSS 1 (Science, Arts, Commercial).",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                AdmissionStepItem(1, "Online Application", "Fill the bio-data and academic history form.")
                                AdmissionStepItem(2, "Entrance CBT Exam", "Take the Computer Based Test in Maths & English.")
                                AdmissionStepItem(3, "Oral Interview", "Candidate & Parent interaction with the Admissions Board.")
                                AdmissionStepItem(4, "Offer & Acceptance", "Receive formal letter and secure seat via portal.")

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = onOpenAdmissionForm,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.AppRegistration, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Begin Online Application")
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
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = "Approved School Fees Schedule (2025/2026)",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = HilltopPrimary
                                )
                                Text(
                                    text = "All fee payments are securely processed via Paystack, Flutterwave, or Zenith Bank Transfer.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                FeeScheduleRow("JSS 1 - JSS 3 (Day Student)", "₦210,000 / Term", "Tuition, ICT, Labs, Library, Medicals")
                                FeeScheduleRow("JSS 1 - JSS 3 (Full Boarding)", "₦480,000 / Term", "Includes Air-conditioned Hostel, 3 Meals, Prep")
                                FeeScheduleRow("SSS 1 - SSS 3 (Day Student)", "₦240,000 / Term", "Tuition, Advanced STEM Labs, ICT, Mock CBT")
                                FeeScheduleRow("SSS 1 - SSS 3 (Full Boarding)", "₦530,000 / Term", "Boarding, Science Practicals, Weekend Tutorials")
                                FeeScheduleRow("WAEC / NECO / IGCSE Reg (SSS 3)", "₦120,000", "Final Year External Exam Biometrics & Materials")
                            }
                        }
                    }
                }

                "news" -> {
                    item {
                        Text(
                            text = "College Press & Bulletins",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    items(newsArticles) { article ->
                        NewsCardItem(
                            article = article,
                            onClick = { viewingArticle = article }
                        )
                    }
                }

                "facilities" -> {
                    item {
                        FacilityCard(
                            title = "Ultra-Modern Science & STEM Laboratories",
                            tag = "Physics • Chemistry • Biology",
                            desc = "Equipped with digital microscopes, fume chambers, spectrophotometers, and individual workstations meeting British & WAEC standards."
                        )
                    }
                    item {
                        FacilityCard(
                            title = "High-Tech Computer Science & AI Innovation Lab",
                            tag = "120 Workstations • High-speed Fiber",
                            desc = "Enabling software development, Python programming, robotics automation, and automated CBT examination testing."
                        )
                    }
                    item {
                        FacilityCard(
                            title = "Olympic-Standard Sports Complex & Pool",
                            tag = "Athletics • Football • Swimming • Basketball",
                            desc = "Fostering physical discipline, sportsmanship, and swimming masterclasses led by certified coaches."
                        )
                    }
                    item {
                        FacilityCard(
                            title = "Serene Residential Boarding Hostels",
                            tag = "Nelson Mandela Hall • Queen Amina Hall",
                            desc = "Safe, comfortable living quarters with 24/7 power backup, clean treated water, infirmary, and resident house masters."
                        )
                    }
                }

                "contact" -> {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = "Get in Touch with Hilltop College",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = HilltopPrimary
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                ContactRow(Icons.Default.LocationOn, "Campus Address", "Plot 12 Admiralty Way, Lekki Phase 1 / Victoria Island, Lagos State, Nigeria")
                                ContactRow(Icons.Default.Phone, "Telephone Lines", "+234 (0) 1 450 8890, +234 803 555 1290")
                                ContactRow(Icons.Default.Email, "Official Email", "admissions@hilltopcollege.edu.ng, info@hilltopcollege.edu.ng")
                                ContactRow(Icons.Default.AccessTime, "Office Hours", "Monday – Friday: 7:30 AM – 4:30 PM")

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = { showWhatsAppData = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.Chat, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Chat with Admissions on WhatsApp")
                                }
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Subscribe to Hilltop Newsletter",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Receive term calendars, scholarship announcements, and academic updates directly.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = newsletterEmail,
                                        onValueChange = { newsletterEmail = it },
                                        placeholder = { Text("parent@example.com") },
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                    Button(
                                        onClick = {
                                            if (newsletterEmail.isNotEmpty()) {
                                                onShowToast("Subscribed! Updates will be sent to $newsletterEmail")
                                                newsletterEmail = ""
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Subscribe")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Article Reader Dialog
    if (viewingArticle != null) {
        val article = viewingArticle!!
        Dialog(onDismissRequest = { viewingArticle = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = HilltopPrimary.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = article.category,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = HilltopPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        IconButton(onClick = { viewingArticle = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimaryDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Published on ${article.date} by ${article.author}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = article.fullContent,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextDark,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewingArticle = null },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary)
                    ) {
                        Text("Close Article")
                    }
                }
            }
        }
    }

    // WhatsApp Direct Dialog
    if (showWhatsAppData) {
        Dialog(onDismissRequest = { showWhatsAppData = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = NigerianGreen, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Admissions WhatsApp Helpdesk",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextDark
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Connect with Mrs. Folashade Adeleke (Head of Admissions). Instant guidance on entry CBT requirements, campus tours, and boarding accommodations.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Direct Line: +234 803 555 1290",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = NigerianGreen
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = { showWhatsAppData = false }, modifier = Modifier.weight(1f)) {
                            Text("Back")
                        }
                        Button(
                            onClick = {
                                onShowToast("Connecting to WhatsApp Admissions channel: +2348035551290...")
                                showWhatsAppData = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NigerianGreen),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Text("Open WhatsApp")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatMiniCard(
    modifier: Modifier = Modifier,
    number: String,
    label: String,
    icon: ImageVector,
    iconColor: Color
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
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = number,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun PortalQuickCard(
    modifier: Modifier = Modifier,
    title: String,
    sub: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                Text(text = sub, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
        }
    }
}

@Composable
private fun NewsCardItem(
    article: NewsArticle,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                        text = article.category,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = HilltopPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = article.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AcademicProgramCard(
    title: String,
    badge: String,
    description: String,
    icon: ImageVector,
    color: Color
) {
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
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Surface(
                    color = color.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = color,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun AdmissionStepItem(step: Int, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(HilltopPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = step.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
            Text(text = desc, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun FeeScheduleRow(title: String, amount: String, includes: String) {
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
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = includes, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                text = amount,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = HilltopPrimary)
            )
        }
    }
}

@Composable
private fun FacilityCard(title: String, tag: String, desc: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = tag, style = MaterialTheme.typography.labelSmall, color = HilltopAccentDark, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ContactRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = HilltopPrimary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = HilltopPrimary)
            Text(text = value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
