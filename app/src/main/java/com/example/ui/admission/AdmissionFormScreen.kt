package com.example.ui.admission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AdmissionApplication
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdmissionFormDialog(
    onDismiss: () -> Unit,
    onSubmit: (AdmissionApplication) -> Unit
) {
    var candidateName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var dob by remember { mutableStateOf("12th May 2014") }
    var stateOfOrigin by remember { mutableStateOf("Lagos State") }
    var entryClass by remember { mutableStateOf("JSS 1") }
    var parentName by remember { mutableStateOf("") }
    var parentPhone by remember { mutableStateOf("+234 ") }
    var parentEmail by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var prevSchool by remember { mutableStateOf("") }

    var passportUploaded by remember { mutableStateOf(false) }
    var birthCertUploaded by remember { mutableStateOf(false) }
    var resultUploaded by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(14.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Online Admission Application",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = HilltopPrimaryDark
                            )
                            Text(
                                text = "2026/2027 Academic Session Entrance",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text = "SECTION A: CANDIDATE'S BIODATA",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = HilltopPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = candidateName,
                                onValueChange = { candidateName = it },
                                label = { Text("Candidate Full Name (Surname First)") },
                                modifier = Modifier.fillMaxWidth().testTag("adm_candidate_name"),
                                singleLine = true
                            )
                        }

                        item {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = gender,
                                    onValueChange = { gender = it },
                                    label = { Text("Gender (Male/Female)") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = dob,
                                    onValueChange = { dob = it },
                                    label = { Text("Date of Birth") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }
                        }

                        item {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = stateOfOrigin,
                                    onValueChange = { stateOfOrigin = it },
                                    label = { Text("State of Origin") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = entryClass,
                                    onValueChange = { entryClass = it },
                                    label = { Text("Class (JSS1 / SSS1)") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = prevSchool,
                                onValueChange = { prevSchool = it },
                                label = { Text("Previous Primary / Junior Secondary School") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "SECTION B: PARENT / GUARDIAN CONTACT",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = HilltopPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = parentName,
                                onValueChange = { parentName = it },
                                label = { Text("Parent / Guardian Full Name") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        item {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = parentPhone,
                                    onValueChange = { parentPhone = it },
                                    label = { Text("Phone Number") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = parentEmail,
                                    onValueChange = { parentEmail = it },
                                    label = { Text("Email Address") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = { Text("Residential Home Address") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 2
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "SECTION C: DOCUMENT UPLOADS",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = HilltopPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            DocumentUploadRow(
                                title = "Passport Photograph (Red Background)",
                                isUploaded = passportUploaded,
                                onToggle = { passportUploaded = !passportUploaded }
                            )
                            DocumentUploadRow(
                                title = "Birth Certificate / Age Declaration",
                                isUploaded = birthCertUploaded,
                                onToggle = { birthCertUploaded = !birthCertUploaded }
                            )
                            DocumentUploadRow(
                                title = "Previous Term Report Card / Transcript",
                                isUploaded = resultUploaded,
                                onToggle = { resultUploaded = !resultUploaded }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (candidateName.isNotBlank() && parentName.isNotBlank()) {
                                    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                    val app = AdmissionApplication(
                                        applicationNo = "HIC-ADM-2026-0${(100..999).random()}",
                                        applicantFullName = candidateName,
                                        gender = gender,
                                        dateOfBirth = dob,
                                        stateOfOrigin = stateOfOrigin,
                                        entryClass = entryClass,
                                        parentGuardianName = parentName,
                                        parentPhone = parentPhone,
                                        parentEmail = if (parentEmail.isNotBlank()) parentEmail else "parent@gmail.com",
                                        residentialAddress = if (address.isNotBlank()) address else "Lagos, Nigeria",
                                        previousSchool = if (prevSchool.isNotBlank()) prevSchool else "Corona School, Lagos",
                                        status = "Submitted",
                                        cbtScore = null,
                                        submissionDate = dateFormat.format(Date())
                                    )
                                    onSubmit(app)
                                    onDismiss()
                                }
                            },
                            enabled = candidateName.isNotBlank() && parentName.isNotBlank(),
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("submit_admission_form"),
                            colors = ButtonDefaults.buttonColors(containerColor = HilltopPrimary)
                        ) {
                            Text("Submit Application")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DocumentUploadRow(
    title: String,
    isUploaded: Boolean,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        color = AcademicBgLight,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = title, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
            TextButton(onClick = onToggle) {
                Icon(
                    imageVector = if (isUploaded) Icons.Default.CheckCircle else Icons.Default.CloudUpload,
                    contentDescription = null,
                    tint = if (isUploaded) NigerianGreen else HilltopPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isUploaded) "Attached" else "Attach",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isUploaded) NigerianGreen else HilltopPrimary
                    )
                )
            }
        }
    }
}
