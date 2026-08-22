package com.example.model

enum class UserRole(val displayName: String, val badgeColor: Long) {
    GUEST("Public Portal", 0xFF00ACC1),
    STUDENT("Student Portal", 0xFF0D47A1),
    PARENT("Parent Portal", 0xFF1565C0),
    TEACHER("Teacher Portal", 0xFF2E7D32),
    ADMIN("Admin Portal", 0xFF6A1B9A)
}

enum class GradeRemark(val grade: String, val remark: String, val points: Double) {
    A1("A1", "Excellent (Distinction)", 5.0),
    B2("B2", "Very Good", 4.0),
    B3("B3", "Good", 3.5),
    C4("C4", "Credit", 3.0),
    C5("C5", "Credit", 2.5),
    C6("C6", "Credit", 2.0),
    D7("D7", "Pass", 1.5),
    E8("E8", "Pass", 1.0),
    F9("F9", "Fail", 0.0);

    companion object {
        fun fromScore(score: Double): GradeRemark = when {
            score >= 75.0 -> A1
            score >= 70.0 -> B2
            score >= 65.0 -> B3
            score >= 60.0 -> C4
            score >= 55.0 -> C5
            score >= 50.0 -> C6
            score >= 45.0 -> D7
            score >= 40.0 -> E8
            else -> F9
        }
    }
}

data class SubjectScore(
    val subjectName: String,
    val subjectCode: String,
    val ca1: Double = 0.0,       // Max 10
    val ca2: Double = 0.0,       // Max 10
    val testScore: Double = 0.0, // Max 10
    val examScore: Double = 0.0, // Max 70
    val teacherRemark: String = "Good performance"
) {
    val totalScore: Double get() = ca1 + ca2 + testScore + examScore
    val gradeRemark: GradeRemark get() = GradeRemark.fromScore(totalScore)
}

data class StudentReportCard(
    val reportId: String,
    val studentId: String,
    val studentName: String,
    val admissionNo: String,
    val className: String,
    val term: String,
    val session: String,
    val scores: List<SubjectScore>,
    val classPosition: Int,
    val totalStudentsInClass: Int,
    val attendancePresent: Int,
    val attendanceTotalDays: Int,
    val classTeacherComment: String,
    val principalComment: String,
    val promotionStatus: String = "Promoted to next class in good standing",
    val nextTermBegins: String = "Sept 15, 2026",
    val psychomotorSkills: Map<String, Int> = mapOf(
        "Punctuality" to 5,
        "Neatness" to 5,
        "Leadership" to 4,
        "Honesty & Integrity" to 5,
        "Attentiveness" to 4,
        "Sports & Physical Activity" to 4
    )
) {
    val totalMarks: Double get() = scores.sumOf { it.totalScore }
    val averageScore: Double get() = if (scores.isNotEmpty()) totalMarks / scores.size else 0.0
    val gpa: Double get() = if (scores.isNotEmpty()) scores.sumOf { it.gradeRemark.points } / scores.size else 0.0
}

enum class PaymentStatus {
    PAID,
    PARTIAL,
    UNPAID,
    PENDING
}

data class FeeItem(
    val name: String,
    val amount: Long // in Nigerian Naira (₦)
)

data class FeeInvoice(
    val invoiceId: String,
    val studentId: String,
    val studentName: String,
    val className: String,
    val title: String,
    val term: String,
    val session: String,
    val totalAmount: Long,
    val amountPaid: Long,
    val dueDate: String,
    val status: PaymentStatus,
    val items: List<FeeItem>
) {
    val balanceDue: Long get() = (totalAmount - amountPaid).coerceAtLeast(0)
}

data class PaymentReceipt(
    val receiptNumber: String,
    val transactionRef: String,
    val invoiceId: String,
    val studentName: String,
    val admissionNo: String,
    val amountPaid: Long,
    val paymentDate: String,
    val paymentGateway: String, // "Paystack", "Flutterwave", "Zenith Bank Transfer", "*737# USSD"
    val channel: String, // "Debit Card (MasterCard/Visa)", "Bank Transfer", "USSD"
    val payerName: String,
    val status: String = "Successful"
)

data class CbtQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class CbtExam(
    val examId: String,
    val title: String,
    val subject: String,
    val targetClass: String,
    val durationMinutes: Int,
    val totalQuestions: Int,
    val passMarkPercentage: Int = 50,
    val instructions: String,
    val questions: List<CbtQuestion>
)

data class CbtResult(
    val examId: String,
    val examTitle: String,
    val studentName: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Double,
    val passed: Boolean,
    val completedAt: String,
    val answersMap: Map<Int, Int> // questionId -> chosenIndex
)

data class Assignment(
    val id: String,
    val title: String,
    val subject: String,
    val targetClass: String,
    val dueDate: String,
    val teacherName: String,
    val description: String,
    val maxScore: Int,
    val isSubmitted: Boolean = false,
    val submissionText: String = "",
    val scoreAwarded: Int? = null,
    val feedback: String? = null
)

data class AttendanceEntry(
    val studentId: String,
    val studentName: String,
    val admissionNo: String,
    val isPresent: Boolean = true,
    val remarks: String = ""
)

data class TimetablePeriod(
    val periodNumber: Int,
    val timeSlot: String,
    val subject: String,
    val teacher: String,
    val room: String
)

data class NewsArticle(
    val id: String,
    val title: String,
    val category: String, // "Academic", "Sports", "WAEC & NECO", "Admissions", "Campus Life"
    val date: String,
    val author: String,
    val summary: String,
    val fullContent: String,
    val imageResType: String = "general"
)

data class AdmissionApplication(
    val applicationNo: String,
    val applicantFullName: String,
    val gender: String,
    val dateOfBirth: String,
    val stateOfOrigin: String,
    val entryClass: String, // JSS 1, JSS 2, SSS 1 Science, etc.
    val parentGuardianName: String,
    val parentPhone: String,
    val parentEmail: String,
    val residentialAddress: String,
    val previousSchool: String,
    val status: String, // "Submitted", "Shortlisted for CBT", "Offered Admission", "Accepted"
    val cbtScore: Int? = null,
    val submissionDate: String
)

data class HostelRoom(
    val roomNumber: String,
    val hallName: String, // "Queen Amina Hall (Girls)", "Nelson Mandela Hall (Boys)"
    val capacity: Int,
    val occupied: Int,
    val floor: String,
    val prefectName: String
)

data class TransportRoute(
    val routeCode: String,
    val routeName: String,
    val busNumber: String,
    val driverName: String,
    val driverPhone: String,
    val pickupPoints: List<String>,
    val departureTime: String,
    val feePerTerm: Long
)

data class LibraryBook(
    val id: String,
    val title: String,
    val author: String,
    val isbn: String,
    val category: String,
    val shelfLocation: String,
    val totalCopies: Int,
    val availableCopies: Int
)

data class SchoolAnnouncement(
    val id: String,
    val title: String,
    val targetAudience: String, // "All", "Students", "Parents", "Teachers"
    val timestamp: String,
    val message: String,
    val isUrgent: Boolean = false
)
