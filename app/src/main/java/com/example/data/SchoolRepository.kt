package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SchoolRepository {

    private val _currentRole = MutableStateFlow(UserRole.GUEST)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _studentReportCards = MutableStateFlow<List<StudentReportCard>>(emptyList())
    val studentReportCards: StateFlow<List<StudentReportCard>> = _studentReportCards.asStateFlow()

    private val _feeInvoices = MutableStateFlow<List<FeeInvoice>>(emptyList())
    val feeInvoices: StateFlow<List<FeeInvoice>> = _feeInvoices.asStateFlow()

    private val _paymentReceipts = MutableStateFlow<List<PaymentReceipt>>(emptyList())
    val paymentReceipts: StateFlow<List<PaymentReceipt>> = _paymentReceipts.asStateFlow()

    private val _cbtExams = MutableStateFlow<List<CbtExam>>(emptyList())
    val cbtExams: StateFlow<List<CbtExam>> = _cbtExams.asStateFlow()

    private val _cbtResults = MutableStateFlow<List<CbtResult>>(emptyList())
    val cbtResults: StateFlow<List<CbtResult>> = _cbtResults.asStateFlow()

    private val _assignments = MutableStateFlow<List<Assignment>>(emptyList())
    val assignments: StateFlow<List<Assignment>> = _assignments.asStateFlow()

    private val _attendanceEntries = MutableStateFlow<List<AttendanceEntry>>(emptyList())
    val attendanceEntries: StateFlow<List<AttendanceEntry>> = _attendanceEntries.asStateFlow()

    private val _newsArticles = MutableStateFlow<List<NewsArticle>>(emptyList())
    val newsArticles: StateFlow<List<NewsArticle>> = _newsArticles.asStateFlow()

    private val _admissions = MutableStateFlow<List<AdmissionApplication>>(emptyList())
    val admissions: StateFlow<List<AdmissionApplication>> = _admissions.asStateFlow()

    private val _announcements = MutableStateFlow<List<SchoolAnnouncement>>(emptyList())
    val announcements: StateFlow<List<SchoolAnnouncement>> = _announcements.asStateFlow()

    private val _hostelRooms = MutableStateFlow<List<HostelRoom>>(emptyList())
    val hostelRooms: StateFlow<List<HostelRoom>> = _hostelRooms.asStateFlow()

    private val _transportRoutes = MutableStateFlow<List<TransportRoute>>(emptyList())
    val transportRoutes: StateFlow<List<TransportRoute>> = _transportRoutes.asStateFlow()

    private val _libraryBooks = MutableStateFlow<List<LibraryBook>>(emptyList())
    val libraryBooks: StateFlow<List<LibraryBook>> = _libraryBooks.asStateFlow()

    init {
        loadInitialData()
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    private fun loadInitialData() {
        // Initial Student Report Cards for SSS 3 Science (Chinedu Okafor)
        val sss3Scores = listOf(
            SubjectScore("Mathematics", "MTH 301", 9.5, 9.0, 9.0, 64.0, "Outstanding problem solving and logic"),
            SubjectScore("English Language", "ENG 301", 8.5, 9.0, 8.5, 59.0, "Great essays and comprehension skills"),
            SubjectScore("Physics", "PHY 301", 9.0, 10.0, 9.5, 63.0, "Excellent grasp of mechanics and optics"),
            SubjectScore("Chemistry", "CHM 301", 8.0, 9.0, 8.5, 60.0, "Strong organic chemistry mastery"),
            SubjectScore("Biology", "BIO 301", 9.0, 9.0, 9.0, 62.0, "Very thorough practical investigations"),
            SubjectScore("Further Mathematics", "FMTH 301", 9.0, 8.5, 9.0, 61.0, "High analytical proficiency in calculus"),
            SubjectScore("Civic Education", "CVE 301", 10.0, 9.5, 9.5, 65.0, "Exemplary citizenship and leadership"),
            SubjectScore("Computer Studies / ICT", "ICT 301", 10.0, 10.0, 10.0, 68.0, "Distinction in Python & Database modules"),
            SubjectScore("Economics", "ECN 301", 8.5, 8.0, 8.5, 58.0, "Solid understanding of macroeconomics")
        )

        val report1 = StudentReportCard(
            reportId = "REP-2025-01",
            studentId = "STU-0482",
            studentName = "Chinedu Emmanuel Okafor",
            admissionNo = "HIC/2023/0482",
            className = "SSS 3 Science A",
            term = "1st Term",
            session = "2025/2026",
            scores = sss3Scores,
            classPosition = 2,
            totalStudentsInClass = 38,
            attendancePresent = 68,
            attendanceTotalDays = 70,
            classTeacherComment = "Chinedu is an exceptionally dedicated student who consistently exhibits sharp intellectual curiosity and good moral conduct.",
            principalComment = "An enviable performance. Keep this momentum high for the upcoming WAEC, NECO & UTME examinations.",
            promotionStatus = "Distinction Standing (Candidate for Valedictorian)"
        )

        val jss2Scores = listOf(
            SubjectScore("Mathematics", "MTH 201", 8.5, 9.0, 8.0, 58.0, "Very good grasp of basic algebra"),
            SubjectScore("English Studies", "ENG 201", 9.0, 9.0, 9.5, 63.0, "Eloquent speaker and avid reader"),
            SubjectScore("Basic Science", "BSC 201", 9.0, 8.5, 9.0, 60.0, "Curious experimenter"),
            SubjectScore("Basic Technology", "BTECH 201", 8.0, 8.5, 8.0, 56.0, "Good technical drawing skills"),
            SubjectScore("Business Studies", "BUS 201", 9.0, 9.0, 9.0, 62.0, "Great bookkeeping acumen"),
            SubjectScore("Civic Education", "CVE 201", 10.0, 9.5, 9.5, 64.0, "Respectful and disciplined"),
            SubjectScore("Information Technology", "ICT 201", 9.5, 10.0, 9.0, 65.0, "Proficient in presentation slides"),
            SubjectScore("Agricultural Science", "AGR 201", 8.5, 8.5, 9.0, 57.0, "Active during school farm practicals")
        )

        val report2 = StudentReportCard(
            reportId = "REP-2025-02",
            studentId = "STU-0621",
            studentName = "Amina Fatima Okafor",
            admissionNo = "HIC/2024/0621",
            className = "JSS 2 Gold",
            term = "1st Term",
            session = "2025/2026",
            scores = jss2Scores,
            classPosition = 4,
            totalStudentsInClass = 42,
            attendancePresent = 69,
            attendanceTotalDays = 70,
            classTeacherComment = "Amina is brilliant, polite, and very cooperative in class group work.",
            principalComment = "Commendable effort. Continues to make the college proud.",
            promotionStatus = "Excellent Standing"
        )

        _studentReportCards.value = listOf(report1, report2)

        // Fee Invoices
        val invoice1 = FeeInvoice(
            invoiceId = "INV-2025-0891",
            studentId = "STU-0482",
            studentName = "Chinedu Emmanuel Okafor",
            className = "SSS 3 Science A",
            title = "1st Term 2025/2026 Tuition & Levies",
            term = "1st Term",
            session = "2025/2026",
            totalAmount = 285000L,
            amountPaid = 285000L,
            dueDate = "Oct 10, 2025",
            status = PaymentStatus.PAID,
            items = listOf(
                FeeItem("Tuition & Academic Instructions", 180000L),
                FeeItem("Science Laboratory & Chemicals Consumables", 30000L),
                FeeItem("ICT & Computer Lab Facilities", 20000L),
                FeeItem("Medical & Student Health Insurance", 15000L),
                FeeItem("PTA Developmental Levy", 15000L),
                FeeItem("Library & Digital E-Portal Access", 10000L),
                FeeItem("Sports & Extra-Curricular Clubs", 15000L)
            )
        )

        val invoice2 = FeeInvoice(
            invoiceId = "INV-2026-0104",
            studentId = "STU-0482",
            studentName = "Chinedu Emmanuel Okafor",
            className = "SSS 3 Science A",
            title = "2nd Term 2025/2026 Tuition & WAEC/NECO Registration",
            term = "2nd Term",
            session = "2025/2026",
            totalAmount = 345000L,
            amountPaid = 150000L,
            dueDate = "March 15, 2026",
            status = PaymentStatus.PARTIAL,
            items = listOf(
                FeeItem("2nd Term Tuition Fee", 180000L),
                FeeItem("WAEC 2026 Registration & Practical Materials", 65000L),
                FeeItem("NECO 2026 Examination Registration", 55000L),
                FeeItem("JAMB/UTME Intensive Prep & Mock Exams", 25000L),
                FeeItem("Hostel Maintenance & Utilities (Boarding)", 20000L)
            )
        )

        val invoice3 = FeeInvoice(
            invoiceId = "INV-2026-0211",
            studentId = "STU-0621",
            studentName = "Amina Fatima Okafor",
            className = "JSS 2 Gold",
            title = "2nd Term 2025/2026 Tuition & School Bus Transport",
            term = "2nd Term",
            session = "2025/2026",
            totalAmount = 260000L,
            amountPaid = 260000L,
            dueDate = "March 15, 2026",
            status = PaymentStatus.PAID,
            items = listOf(
                FeeItem("2nd Term Tuition Fee", 160000L),
                FeeItem("School Bus Transport (Lekki Phase 1 Route)", 60000L),
                FeeItem("Mid-day Hot Lunch Meal Program", 30000L),
                FeeItem("PTA Welfare & Sports Levy", 10000L)
            )
        )

        _feeInvoices.value = listOf(invoice1, invoice2, invoice3)

        // Payment Receipts
        val receipt1 = PaymentReceipt(
            receiptNumber = "HIC/REC/2025/1109",
            transactionRef = "PAY_PSK_8849201948",
            invoiceId = "INV-2025-0891",
            studentName = "Chinedu Emmanuel Okafor",
            admissionNo = "HIC/2023/0482",
            amountPaid = 285000L,
            paymentDate = "Sept 28, 2025 10:14 AM",
            paymentGateway = "Paystack Gateway",
            channel = "Debit Card (MasterCard •••• 4218)",
            payerName = "Barrister Samuel Okafor"
        )

        val receipt2 = PaymentReceipt(
            receiptNumber = "HIC/REC/2026/0034",
            transactionRef = "FLW_TXN_9921048821",
            invoiceId = "INV-2026-0104",
            studentName = "Chinedu Emmanuel Okafor",
            admissionNo = "HIC/2023/0482",
            amountPaid = 150000L,
            paymentDate = "Jan 12, 2026 02:45 PM",
            paymentGateway = "Flutterwave Checkout",
            channel = "Direct Bank Transfer (Zenith Bank NIP)",
            payerName = "Barrister Samuel Okafor"
        )

        _paymentReceipts.value = listOf(receipt1, receipt2)

        // CBT Mock Exams
        val mathQuestions = listOf(
            CbtQuestion(
                id = 1,
                question = "Evaluate log₁₀(25) + log₁₀(4) without using mathematical tables.",
                options = listOf("1", "2", "100", "50"),
                correctIndex = 1,
                explanation = "log₁₀(25) + log₁₀(4) = log₁₀(25 × 4) = log₁₀(100) = 2."
            ),
            CbtQuestion(
                id = 2,
                question = "Find the roots of the quadratic equation 2x² - 5x + 2 = 0.",
                options = listOf("x = 2 or x = 1/2", "x = -2 or x = -1/2", "x = 1 or x = 4", "x = -1 or x = 2"),
                correctIndex = 0,
                explanation = "Factorizing: (2x - 1)(x - 2) = 0 => x = 1/2 or x = 2."
            ),
            CbtQuestion(
                id = 3,
                question = "If the 3rd term of an Arithmetic Progression (AP) is 11 and the 7th term is 27, find the first term 'a' and common difference 'd'.",
                options = listOf("a = 3, d = 4", "a = 2, d = 5", "a = 5, d = 3", "a = 1, d = 4"),
                correctIndex = 0,
                explanation = "T3 = a + 2d = 11; T7 = a + 6d = 27. Subtracting: 4d = 16 => d = 4. Then a = 11 - 8 = 3."
            ),
            CbtQuestion(
                id = 4,
                question = "Differentiate y = 3x⁴ - 5x² + 7 with respect to x.",
                options = listOf("12x³ - 10x", "12x³ - 5x + 7", "7x³ - 10x", "12x⁴ - 10x"),
                correctIndex = 0,
                explanation = "dy/dx = d/dx(3x⁴) - d/dx(5x²) + d/dx(7) = 12x³ - 10x."
            ),
            CbtQuestion(
                id = 5,
                question = "A fair die is rolled once. What is the probability of obtaining a prime number?",
                options = listOf("1/2", "1/3", "2/3", "1/6"),
                correctIndex = 0,
                explanation = "Prime numbers on a 6-sided die are {2, 3, 5} (3 outcomes out of 6). Probability = 3/6 = 1/2."
            )
        )

        val englishQuestions = listOf(
            CbtQuestion(
                id = 1,
                question = "Choose the word nearest in meaning to the italicized word: 'The principal gave a *lucid* explanation of the college disciplinary policy.'",
                options = listOf("Clear and easy to understand", "Confusing and vague", "Lengthy and boring", "Aggressive"),
                correctIndex = 0,
                explanation = "'Lucid' means expressed clearly; easy to understand."
            ),
            CbtQuestion(
                id = 2,
                question = "Select the option that best completes the sentence: 'Neither the prefects nor the head boy _____ present at the assembly.'",
                options = listOf("was", "were", "are", "have been"),
                correctIndex = 0,
                explanation = "With 'neither... nor', the verb agrees with the subject closest to it ('the head boy' is singular, so 'was')."
            ),
            CbtQuestion(
                id = 3,
                question = "Identify the figure of speech in: 'The classroom was a bustling beehive of eager learners.'",
                options = listOf("Metaphor", "Simile", "Personification", "Hyperbole"),
                correctIndex = 0,
                explanation = "It is a direct comparison without using 'like' or 'as', which is a metaphor."
            ),
            CbtQuestion(
                id = 4,
                question = "Choose the correct spelling:",
                options = listOf("Accommodation", "Acommodation", "Accomodation", "Acomodation"),
                correctIndex = 0,
                explanation = "Correct spelling has double 'c' and double 'm': Accommodation."
            ),
            CbtQuestion(
                id = 5,
                question = "What is the antonym of 'EPHEMERAL'?",
                options = listOf("Permanent", "Transitory", "Fleeting", "Short-lived"),
                correctIndex = 0,
                explanation = "Ephemeral means short-lived; the opposite is permanent/lasting."
            )
        )

        val physicsQuestions = listOf(
            CbtQuestion(
                id = 1,
                question = "An object is dropped from rest from a cliff of height 80m. Taking g = 10 m/s², find the time taken to reach the ground.",
                options = listOf("4 seconds", "2 seconds", "8 seconds", "16 seconds"),
                correctIndex = 0,
                explanation = "h = 1/2 gt² => 80 = 1/2(10)t² => 5t² = 80 => t² = 16 => t = 4s."
            ),
            CbtQuestion(
                id = 2,
                question = "Which of the following electromagnetic waves has the highest frequency and shortest wavelength?",
                options = listOf("Gamma rays", "X-rays", "Ultraviolet rays", "Radio waves"),
                correctIndex = 0,
                explanation = "Gamma rays possess the highest frequency in the electromagnetic spectrum."
            ),
            CbtQuestion(
                id = 3,
                question = "State Ohm's Law formula for an electric circuit:",
                options = listOf("V = I × R", "P = V × I", "I = V² / R", "R = V × I"),
                correctIndex = 0,
                explanation = "Ohm's Law states Voltage (V) = Current (I) × Resistance (R)."
            )
        )

        val exam1 = CbtExam(
            examId = "CBT-MTH-301",
            title = "WAEC & JAMB Prep: SSS 3 General Mathematics",
            subject = "Mathematics",
            targetClass = "SSS 3",
            durationMinutes = 15,
            totalQuestions = mathQuestions.size,
            passMarkPercentage = 50,
            instructions = "Answer all multiple choice questions. Time allowed is 15 minutes. Calculators are permitted.",
            questions = mathQuestions
        )

        val exam2 = CbtExam(
            examId = "CBT-ENG-301",
            title = "Senior English Language & Lexis CBT Test",
            subject = "English Language",
            targetClass = "SSS 3",
            durationMinutes = 15,
            totalQuestions = englishQuestions.size,
            passMarkPercentage = 50,
            instructions = "Read each sentence carefully and select the best option. Instant feedback will be provided upon submission.",
            questions = englishQuestions
        )

        val exam3 = CbtExam(
            examId = "CBT-PHY-301",
            title = "Senior Physics: Mechanics & Waves Assessment",
            subject = "Physics",
            targetClass = "SSS 3 Science",
            durationMinutes = 10,
            totalQuestions = physicsQuestions.size,
            passMarkPercentage = 60,
            instructions = "Ensure your rough sheet is ready. Keep to the time limit.",
            questions = physicsQuestions
        )

        _cbtExams.value = listOf(exam1, exam2, exam3)

        // Assignments
        _assignments.value = listOf(
            Assignment(
                id = "ASN-001",
                title = "Projectile Motion & Horizontal Range Formula Derivation",
                subject = "Physics",
                targetClass = "SSS 3 Science A",
                dueDate = "Tomorrow, 8:00 AM",
                teacherName = "Mr. Babatunde Adeyemi",
                description = "Derive the mathematical expression for maximum height (H_max) and horizontal range (R) of a projectile launched at an angle θ with velocity u. Solve questions 4 & 7 on Page 142.",
                maxScore = 20,
                isSubmitted = true,
                submissionText = "Derived H_max = (u² sin²θ)/(2g) and Range R = (u² sin 2θ)/g. Question 4: Range = 124.5m. Question 7: Time of flight = 3.6 seconds.",
                scoreAwarded = 19,
                feedback = "Excellent algebraic steps and neat presentation. Keep it up!"
            ),
            Assignment(
                id = "ASN-002",
                title = "Essay: The Impact of Artificial Intelligence on Nigerian Agriculture",
                subject = "English Language",
                targetClass = "SSS 3 Science A",
                dueDate = "Friday, 4:00 PM",
                teacherName = "Mrs. Ngozi Chukwuma",
                description = "Write a 450-word expository essay highlighting how modern drone surveillance, automated irrigation, and soil sensor technology can improve crop yield in Nigeria.",
                maxScore = 30,
                isSubmitted = false
            ),
            Assignment(
                id = "ASN-003",
                title = "Organic Chemistry: IUPAC Nomenclature of Alkanols and Alkanoic Acids",
                subject = "Chemistry",
                targetClass = "SSS 3 Science A",
                dueDate = "Monday next week",
                teacherName = "Dr. Kenneth Obi",
                description = "Draw structural formulas and provide IUPAC names for the 8 isomers of pentanol (C5H11OH).",
                maxScore = 20,
                isSubmitted = false
            )
        )

        // Attendance list for Teacher & Admin view
        _attendanceEntries.value = listOf(
            AttendanceEntry("STU-0482", "Chinedu Emmanuel Okafor", "HIC/2023/0482", true, "Present & on time"),
            AttendanceEntry("STU-0483", "Blessing Adaobi Eze", "HIC/2023/0483", true, "Present"),
            AttendanceEntry("STU-0484", "Tunde Olawale Balogun", "HIC/2023/0484", true, "Present"),
            AttendanceEntry("STU-0485", "Fatima Abubakar Sadiq", "HIC/2023/0485", true, "Present"),
            AttendanceEntry("STU-0486", "Kelechi Junior Nnamdi", "HIC/2023/0486", false, "Sick bay permit granted"),
            AttendanceEntry("STU-0487", "Aisha Mohammed Danjuma", "HIC/2023/0487", true, "Present"),
            AttendanceEntry("STU-0488", "David Olamide Adeleke", "HIC/2023/0488", true, "Present")
        )

        // News & Events
        _newsArticles.value = listOf(
            NewsArticle(
                id = "NWS-01",
                title = "Hilltop College Records 100% Distinction in 2025 WAEC & IGCSE Exams",
                category = "Academic Excellence",
                date = "February 18, 2026",
                author = "Office of the Principal",
                summary = "For the 7th consecutive year, Hilltop International College students achieved straight A's and B's across Mathematics, English, Sciences, and Humanities.",
                fullContent = "The Management, Board of Governors, and Faculty of Hilltop International College are thrilled to announce outstanding performances in the West African Senior School Certificate Examination (WASSCE) and Cambridge IGCSE. Our senior candidates achieved a record 94.8% distinction rate, solidifying Hilltop's reputation as Nigeria's premier academic institution."
            ),
            NewsArticle(
                id = "NWS-02",
                title = "2026/2027 Academic Session Admissions Now Open for JSS 1, JSS 2 & SSS 1",
                category = "Admissions",
                date = "February 10, 2026",
                author = "Admissions Board",
                summary = "Prospective parents and guardians can now register their wards online for the National Entrance Examination and Scholarship Assessment.",
                fullContent = "Entrance examination dates: Batch A on March 28, 2026, Batch B on May 16, 2026. Meritorious academic and sports scholarships are available for high-scoring candidates."
            ),
            NewsArticle(
                id = "NWS-03",
                title = "Commissioning of Ultra-Modern STEM Robotics & AI Innovation Hub",
                category = "Campus Facilities",
                date = "January 24, 2026",
                author = "ICT & Engineering Dept",
                summary = "Hilltop College opens a state-of-the-art 120-seat computer science center equipped with 3D printers, IoT kits, and high-speed fiber internet.",
                fullContent = "The facility enables students from JSS 1 through SSS 3 to develop coding, robotics, artificial intelligence, and embedded systems skills in tandem with global curriculum standards."
            ),
            NewsArticle(
                id = "NWS-04",
                title = "Annual Inter-House Sports Festival & Athletics Championship 2026",
                category = "Sports & Culture",
                date = "March 05, 2026",
                author = "Sports Master",
                summary = "Join us at the Main Stadium as Blue House, Green House, Yellow House, and Red House compete for the coveted Chancellor's Trophy.",
                fullContent = "Events include 100m sprint, 4x100m relay, long jump, football finals, high jump, chess tournament, and martial arts demonstrations."
            )
        )

        // Admission Applications for Admin review
        _admissions.value = listOf(
            AdmissionApplication(
                applicationNo = "HIC-ADM-2026-0012",
                applicantFullName = "Somtochukwu Michael Obi",
                gender = "Male",
                dateOfBirth = "14th April 2014",
                stateOfOrigin = "Anambra State",
                entryClass = "JSS 1",
                parentGuardianName = "Chief & Dr. (Mrs.) O. Obi",
                parentPhone = "+234 803 555 1290",
                parentEmail = "obi.family@gmail.com",
                residentialAddress = "14 Admiralty Way, Lekki Phase 1, Lagos",
                previousSchool = "Corona Primary School, Victoria Island",
                status = "Offered Admission",
                cbtScore = 88,
                submissionDate = "Jan 20, 2026"
            ),
            AdmissionApplication(
                applicationNo = "HIC-ADM-2026-0019",
                applicantFullName = "Zainab Haliru Bello",
                gender = "Female",
                dateOfBirth = "22nd August 2011",
                stateOfOrigin = "Kano State",
                entryClass = "SSS 1 Science",
                parentGuardianName = "Alhaji Ibrahim Bello",
                parentPhone = "+234 802 888 7711",
                parentEmail = "i.bello@investments.ng",
                residentialAddress = "Plot 9 Ikoyi Crescent, Ikoyi, Lagos",
                previousSchool = "British International School, Lagos",
                status = "Shortlisted for CBT",
                cbtScore = 92,
                submissionDate = "Feb 02, 2026"
            ),
            AdmissionApplication(
                applicationNo = "HIC-ADM-2026-0025",
                applicantFullName = "Femi Ayomide Adeleke",
                gender = "Male",
                dateOfBirth = "09th November 2013",
                stateOfOrigin = "Osun State",
                entryClass = "JSS 1",
                parentGuardianName = "Engr. Rotimi Adeleke",
                parentPhone = "+234 816 444 3322",
                parentEmail = "rotimi.adeleke@energy.com",
                residentialAddress = "22 Banana Island Road, Ikoyi, Lagos",
                previousSchool = "Children's International School, Lekki",
                status = "Submitted",
                cbtScore = null,
                submissionDate = "Feb 15, 2026"
            )
        )

        // Announcements
        _announcements.value = listOf(
            SchoolAnnouncement(
                id = "ANC-01",
                title = "Mid-Term Break Notification & Resumption Protocols",
                targetAudience = "All",
                timestamp = "Yesterday at 4:30 PM",
                message = "Please be informed that the 2nd Term Mid-Term Break commences on Thursday, Feb 26. Boarders will return on Sunday, March 1 by 5:00 PM.",
                isUrgent = false
            ),
            SchoolAnnouncement(
                id = "ANC-02",
                title = "WAEC & NECO 2026 Registration Deadline Reminder",
                targetAudience = "Parents & SSS 3 Students",
                timestamp = "3 days ago",
                message = "All SSS 3 parents are requested to complete exam fee reconciliations by March 15 to ensure biometric capture for WAEC/NECO e-registration.",
                isUrgent = true
            ),
            SchoolAnnouncement(
                id = "ANC-03",
                title = "PTA General Meeting & Academic Open Day",
                targetAudience = "Parents",
                timestamp = "1 week ago",
                message = "The 1st Semester General PTA Assembly and Open Day session with subject tutors holds on Saturday, March 14 at the College Main Auditorium.",
                isUrgent = false
            )
        )

        // Hostel Rooms
        _hostelRooms.value = listOf(
            HostelRoom("Room A-101", "Nelson Mandela Hall (Senior Boys)", 6, 6, "1st Floor", "Chinedu Emmanuel Okafor"),
            HostelRoom("Room A-102", "Nelson Mandela Hall (Senior Boys)", 6, 5, "1st Floor", "David Adeleke"),
            HostelRoom("Room B-201", "Queen Amina Hall (Senior Girls)", 6, 6, "2nd Floor", "Blessing Adaobi Eze"),
            HostelRoom("Room B-202", "Queen Amina Hall (Junior Girls)", 8, 7, "2nd Floor", "Amina Fatima Okafor")
        )

        // Transport Routes
        _transportRoutes.value = listOf(
            TransportRoute(
                routeCode = "RT-01",
                routeName = "Lekki Phase 1 & Ikoyi Express",
                busNumber = "Toyota Coaster - HIC 01",
                driverName = "Mr. Sunday Okoro",
                driverPhone = "+234 803 111 2233",
                pickupPoints = listOf("Admiralty Way", "Admiralty Tollgate", "Bourdillon Road", "Parkview Estate"),
                departureTime = "6:45 AM",
                feePerTerm = 60000L
            ),
            TransportRoute(
                routeCode = "RT-02",
                routeName = "Ajah - Sangotedo - VGC Route",
                busNumber = "Mercedes Benz Sprinter - HIC 04",
                driverName = "Mallam Usman Garba",
                driverPhone = "+234 805 777 8899",
                pickupPoints = listOf("Crown Estate", "Sangotedo Shoprite", "VGC Main Gate", "Chevron Roundabout"),
                departureTime = "6:30 AM",
                feePerTerm = 65000L
            ),
            TransportRoute(
                routeCode = "RT-03",
                routeName = "Ikeja GRA - Maryland - Campus",
                busNumber = "Toyota HiAce - HIC 07",
                driverName = "Mr. Kayode Johnson",
                driverPhone = "+234 818 222 3344",
                pickupPoints = listOf("Isaac John Street", "Maryland Mall", "Anthony Village", "Third Mainland Bridge"),
                departureTime = "6:15 AM",
                feePerTerm = 75000L
            )
        )

        // Library Catalogue
        _libraryBooks.value = listOf(
            LibraryBook("BK-01", "New General Mathematics for Senior Secondary Schools 3", "M.F. Macrae et al.", "978-0582588", "Mathematics", "Shelf M-12", 45, 18),
            LibraryBook("BK-02", "Comprehensive Certificate Physics for WAEC", "P.N. Okeke & M.W. Anyakoha", "978-9781750", "Sciences", "Shelf P-04", 40, 12),
            LibraryBook("BK-03", "Essential Chemistry for West African Senior Secondary Schools", "I.A. Odesina", "978-2205118", "Sciences", "Shelf C-08", 35, 14),
            LibraryBook("BK-04", "Things Fall Apart (WASSCE Literature Text)", "Chinua Achebe", "978-0385474", "Literature in English", "Shelf L-01", 60, 22),
            LibraryBook("BK-05", "Modern Biology for Senior Secondary Schools", "Sarojini T. Ramalingam", "978-9781482", "Sciences", "Shelf B-03", 30, 9),
            LibraryBook("BK-06", "Government & Civic Education in West Africa", "B.A. Adeleke", "978-1902441", "Social Sciences", "Shelf G-06", 25, 11)
        )
    }

    fun submitPayment(invoiceId: String, amountPaid: Long, gateway: String, channel: String, payerName: String): PaymentReceipt {
        val invoices = _feeInvoices.value.toMutableList()
        val index = invoices.indexOfFirst { it.invoiceId == invoiceId }
        val studentName = if (index != -1) invoices[index].studentName else "Chinedu Emmanuel Okafor"
        val admissionNo = if (studentName.contains("Chinedu")) "HIC/2023/0482" else "HIC/2024/0621"

        if (index != -1) {
            val oldInv = invoices[index]
            val newAmountPaid = oldInv.amountPaid + amountPaid
            val newStatus = if (newAmountPaid >= oldInv.totalAmount) PaymentStatus.PAID else PaymentStatus.PARTIAL
            invoices[index] = oldInv.copy(amountPaid = newAmountPaid, status = newStatus)
            _feeInvoices.value = invoices
        }

        val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
        val receiptNumber = "HIC/REC/2026/${(1000..9999).random()}"
        val transactionRef = "${gateway.take(3).uppercase()}_TXN_${System.currentTimeMillis().toString().takeLast(8)}"

        val receipt = PaymentReceipt(
            receiptNumber = receiptNumber,
            transactionRef = transactionRef,
            invoiceId = invoiceId,
            studentName = studentName,
            admissionNo = admissionNo,
            amountPaid = amountPaid,
            paymentDate = dateFormat.format(Date()),
            paymentGateway = gateway,
            channel = channel,
            payerName = payerName,
            status = "Successful"
        )

        val receipts = _paymentReceipts.value.toMutableList()
        receipts.add(0, receipt)
        _paymentReceipts.value = receipts

        return receipt
    }

    fun submitCbtExam(examId: String, studentName: String, answersMap: Map<Int, Int>): CbtResult {
        val exam = _cbtExams.value.find { it.examId == examId } ?: return CbtResult(
            examId, "Examination", studentName, 0, 0, 0.0, false, "Just now", answersMap
        )

        var correctCount = 0
        exam.questions.forEach { question ->
            val chosen = answersMap[question.id]
            if (chosen != null && chosen == question.correctIndex) {
                correctCount++
            }
        }

        val percentage = (correctCount.toDouble() / exam.questions.size.toDouble()) * 100.0
        val passed = percentage >= exam.passMarkPercentage
        val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())

        val result = CbtResult(
            examId = exam.examId,
            examTitle = exam.title,
            studentName = studentName,
            score = correctCount,
            totalQuestions = exam.questions.size,
            percentage = percentage,
            passed = passed,
            completedAt = dateFormat.format(Date()),
            answersMap = answersMap
        )

        val resultsList = _cbtResults.value.toMutableList()
        resultsList.add(0, result)
        _cbtResults.value = resultsList
        return result
    }

    fun updateSubjectScore(reportId: String, subjectCode: String, newCa1: Double, newCa2: Double, newTest: Double, newExam: Double) {
        val reports = _studentReportCards.value.toMutableList()
        val rIndex = reports.indexOfFirst { it.reportId == reportId }
        if (rIndex != -1) {
            val report = reports[rIndex]
            val updatedScores = report.scores.map { score ->
                if (score.subjectCode == subjectCode) {
                    score.copy(ca1 = newCa1, ca2 = newCa2, testScore = newTest, examScore = newExam)
                } else {
                    score
                }
            }
            reports[rIndex] = report.copy(scores = updatedScores)
            _studentReportCards.value = reports
        }
    }

    fun toggleAttendance(studentId: String) {
        val list = _attendanceEntries.value.toMutableList()
        val idx = list.indexOfFirst { it.studentId == studentId }
        if (idx != -1) {
            val old = list[idx]
            list[idx] = old.copy(isPresent = !old.isPresent)
            _attendanceEntries.value = list
        }
    }

    fun submitAssignmentSolution(assignmentId: String, text: String) {
        val list = _assignments.value.toMutableList()
        val idx = list.indexOfFirst { it.id == assignmentId }
        if (idx != -1) {
            val old = list[idx]
            list[idx] = old.copy(isSubmitted = true, submissionText = text)
            _assignments.value = list
        }
    }

    fun createAssignment(title: String, subject: String, targetClass: String, dueDate: String, desc: String, maxScore: Int) {
        val list = _assignments.value.toMutableList()
        val newAsn = Assignment(
            id = "ASN-${(100..999).random()}",
            title = title,
            subject = subject,
            targetClass = targetClass,
            dueDate = dueDate,
            teacherName = "Mr. Babatunde Adeyemi",
            description = desc,
            maxScore = maxScore,
            isSubmitted = false
        )
        list.add(0, newAsn)
        _assignments.value = list
    }

    fun submitNewAdmission(app: AdmissionApplication) {
        val list = _admissions.value.toMutableList()
        list.add(0, app)
        _admissions.value = list
    }

    fun updateAdmissionStatus(applicationNo: String, newStatus: String) {
        val list = _admissions.value.toMutableList()
        val idx = list.indexOfFirst { it.applicationNo == applicationNo }
        if (idx != -1) {
            list[idx] = list[idx].copy(status = newStatus)
            _admissions.value = list
        }
    }

    fun broadcastAnnouncement(title: String, audience: String, message: String, isUrgent: Boolean) {
        val list = _announcements.value.toMutableList()
        val newAnn = SchoolAnnouncement(
            id = "ANC-${(10..99).random()}",
            title = title,
            targetAudience = audience,
            timestamp = "Just now",
            message = message,
            isUrgent = isUrgent
        )
        list.add(0, newAnn)
        _announcements.value = list
    }
}
