/**
 * Hilltop International College Portal - Master Data Store
 * Contains default seed data and state store for all Nigerian secondary school entities.
 */

window.HilltopData = {
    // Current Active Session & Term
    session: "2025/2026",
    term: "2nd Term",

    // Current Active Role ('public', 'student', 'parent', 'teacher', 'admin')
    currentRole: 'public',

    // Students Data
    students: [
        {
            id: "STU-0482",
            admissionNo: "HIC/2023/0482",
            name: "Chinedu Emmanuel Okafor",
            className: "SSS 3 Science A",
            gender: "Male",
            house: "Nelson Mandela Hall",
            role: "Senior Prefect",
            parentId: "PAR-0112",
            parentName: "Barr. Samuel Okafor",
            parentPhone: "+234 803 456 7890",
            avatar: "👨‍🎓"
        },
        {
            id: "STU-0621",
            admissionNo: "HIC/2024/0621",
            name: "Amina Fatima Okafor",
            className: "JSS 2 Gold",
            gender: "Female",
            house: "Queen Amina Hall",
            role: "Class Captain",
            parentId: "PAR-0112",
            parentName: "Barr. Samuel Okafor",
            parentPhone: "+234 803 456 7890",
            avatar: "👩‍🎓"
        }
    ],

    // Term Performance Report Cards
    reportCards: [
        {
            reportId: "REP-2026-0482",
            studentId: "STU-0482",
            studentName: "Chinedu Emmanuel Okafor",
            admissionNo: "HIC/2023/0482",
            className: "SSS 3 Science A",
            session: "2025/2026",
            term: "2nd Term",
            classPosition: 2,
            totalStudentsInClass: 38,
            averageScore: 87.2,
            gpa: 4.44,
            attendancePresent: 68,
            attendanceTotalDays: 70,
            nextTermResumption: "Monday, May 4, 2026",
            principalRemarks: "Exceptional academic brilliance combined with exemplary leadership as Senior Prefect. A certain Distinction candidate for WAEC & Cambridge IGCSE.",
            formTeacherRemarks: "Chinedu maintains an unwavering focus. Commendable performance across all Science and Mathematics subjects.",
            houseMasterRemarks: "Clean, obedient, and inspires junior boarders in Nelson Mandela Hall.",
            scores: [
                { subjectCode: "FMTH", subjectName: "Further Mathematics", ca1: 9.5, ca2: 9.0, testScore: 9.0, examScore: 64.0, totalScore: 91.5, grade: "A1", remark: "Excellent Distinction" },
                { subjectCode: "MTH", subjectName: "General Mathematics", ca1: 10.0, ca2: 9.5, testScore: 8.5, examScore: 61.0, totalScore: 89.0, grade: "A1", remark: "Excellent Distinction" },
                { subjectCode: "PHY", subjectName: "Physics (Practical & Theory)", ca1: 8.5, ca2: 9.0, testScore: 8.5, examScore: 62.0, totalScore: 88.0, grade: "A1", remark: "Excellent Distinction" },
                { subjectCode: "CHM", subjectName: "Chemistry", ca1: 9.0, ca2: 8.0, testScore: 9.0, examScore: 59.0, totalScore: 85.0, grade: "A1", remark: "Excellent Distinction" },
                { subjectCode: "ENG", subjectName: "English Language & Lexis", ca1: 8.0, ca2: 8.5, testScore: 8.0, examScore: 58.0, totalScore: 82.5, grade: "A1", remark: "Distinction" },
                { subjectCode: "CSC", subjectName: "Computer Science & Robotics", ca1: 10.0, ca2: 10.0, testScore: 10.0, examScore: 65.0, totalScore: 95.0, grade: "A1", remark: "Outstanding" },
                { subjectCode: "BIO", subjectName: "Biology", ca1: 8.0, ca2: 8.0, testScore: 7.5, examScore: 55.0, totalScore: 78.5, grade: "B2", remark: "Very Good" },
                { subjectCode: "CIV", subjectName: "Civic Education", ca1: 7.5, ca2: 8.0, testScore: 7.5, examScore: 51.0, totalScore: 74.0, grade: "B3", remark: "Good" }
            ],
            psychomotor: [
                { skill: "Handwriting & Neatness", rating: "A" },
                { skill: "Musical & STEM Lab Skills", rating: "A" },
                { skill: "Sports & Athletics", rating: "B" },
                { skill: "Leadership & Initiative", rating: "A" }
            ],
            affective: [
                { trait: "Punctuality", rating: "A" },
                { trait: "Politeness & Respect", rating: "A" },
                { trait: "Honesty & Reliability", rating: "A" },
                { trait: "Emotional Stability", rating: "A" }
            ]
        },
        {
            reportId: "REP-2026-0621",
            studentId: "STU-0621",
            studentName: "Amina Fatima Okafor",
            admissionNo: "HIC/2024/0621",
            className: "JSS 2 Gold",
            session: "2025/2026",
            term: "2nd Term",
            classPosition: 4,
            totalStudentsInClass: 42,
            averageScore: 83.4,
            gpa: 4.15,
            attendancePresent: 69,
            attendanceTotalDays: 70,
            nextTermResumption: "Monday, May 4, 2026",
            principalRemarks: "Very good progress. Shows wonderful enthusiasm in Creative Arts and Basic Science.",
            formTeacherRemarks: "Amina is well-behaved and diligent in her homework submissions.",
            houseMasterRemarks: "Courteous and neat boarder in Queen Amina Hall.",
            scores: [
                { subjectCode: "J_MTH", subjectName: "Basic Mathematics", ca1: 8.5, ca2: 8.5, testScore: 8.0, examScore: 57.0, totalScore: 82.0, grade: "A1", remark: "Distinction" },
                { subjectCode: "J_ENG", subjectName: "English Studies", ca1: 9.0, ca2: 8.5, testScore: 9.0, examScore: 60.0, totalScore: 86.5, grade: "A1", remark: "Excellent Distinction" },
                { subjectCode: "J_BST", subjectName: "Basic Science & Technology", ca1: 8.0, ca2: 8.0, testScore: 8.5, examScore: 56.0, totalScore: 80.5, grade: "A1", remark: "Distinction" },
                { subjectCode: "J_BUS", subjectName: "Business Studies", ca1: 8.5, ca2: 8.0, testScore: 8.0, examScore: 54.0, totalScore: 78.5, grade: "B2", remark: "Very Good" },
                { subjectCode: "J_CCA", subjectName: "Cultural & Creative Arts", ca1: 9.5, ca2: 9.5, testScore: 9.5, examScore: 62.0, totalScore: 90.5, grade: "A1", remark: "Outstanding" },
                { subjectCode: "J_FRS", subjectName: "French Language", ca1: 8.0, ca2: 7.5, testScore: 8.0, examScore: 53.0, totalScore: 76.5, grade: "B2", remark: "Very Good" }
            ],
            psychomotor: [
                { skill: "Handwriting & Neatness", rating: "A" },
                { skill: "Musical & Arts Skills", rating: "A" },
                { skill: "Sports & Athletics", rating: "B" },
                { skill: "Leadership & Initiative", rating: "B" }
            ],
            affective: [
                { trait: "Punctuality", rating: "A" },
                { trait: "Politeness & Respect", rating: "A" },
                { trait: "Honesty & Reliability", rating: "A" },
                { trait: "Emotional Stability", rating: "A" }
            ]
        }
    ],

    // CBT Examinations Seed
    cbtExams: [
        {
            id: "CBT-01",
            title: "WAEC / NECO Senior Mathematics Mock Examination",
            subject: "General Mathematics",
            targetClass: "SSS 3 Science A",
            durationMinutes: 20,
            passMarkPercentage: 60,
            instructions: "Answer all questions. Each correct answer carries 2 marks. Use the on-screen palette to jump to questions.",
            questions: [
                {
                    id: 1,
                    question: "Solve for x in the quadratic equation: 2x² - 5x - 3 = 0.",
                    options: ["x = 3 or x = -1/2", "x = -3 or x = 1/2", "x = 2 or x = -3/2", "x = 1 or x = 6"],
                    correctIndex: 0,
                    explanation: "Factoring: (2x + 1)(x - 3) = 0 => 2x = -1 (x = -1/2) or x = 3."
                },
                {
                    id: 2,
                    question: "If log₁₀(2) = 0.3010 and log₁₀(3) = 0.4771, evaluate log₁₀(18).",
                    options: ["1.2552", "1.0791", "1.5441", "0.9542"],
                    correctIndex: 0,
                    explanation: "log₁₀(18) = log₁₀(2 * 3²) = log₁₀(2) + 2*log₁₀(3) = 0.3010 + 2*(0.4771) = 1.2552."
                },
                {
                    id: 3,
                    question: "A bag contains 5 red balls and 7 blue balls. If two balls are picked at random without replacement, what is the probability that both are red?",
                    options: ["5/33", "7/33", "25/144", "5/12"],
                    correctIndex: 0,
                    explanation: "P(Red 1st) = 5/12; P(Red 2nd) = 4/11. Total P = (5/12) * (4/11) = 20/132 = 5/33."
                },
                {
                    id: 4,
                    question: "The sum of the interior angles of a regular polygon is 1080°. How many sides does the polygon have?",
                    options: ["8 (Octagon)", "6 (Hexagon)", "10 (Decagon)", "12 (Dodecagon)"],
                    correctIndex: 0,
                    explanation: "Sum = (n - 2) * 180° = 1080° => n - 2 = 6 => n = 8 sides."
                },
                {
                    id: 5,
                    question: "Differentiate y = 3x⁴ - 5x² + 7x with respect to x.",
                    options: ["dy/dx = 12x³ - 10x + 7", "dy/dx = 12x³ - 5x + 7", "dy/dx = 7x³ - 10x", "dy/dx = 12x⁴ - 10x² + 7"],
                    correctIndex: 0,
                    explanation: "Applying the power rule: d/dx(3x⁴) = 12x³, d/dx(-5x²) = -10x, d/dx(7x) = 7."
                }
            ]
        },
        {
            id: "CBT-02",
            title: "Physics Mechanics & Modern Waves Assessment",
            subject: "Physics",
            targetClass: "SSS 3 Science A",
            durationMinutes: 15,
            passMarkPercentage: 60,
            instructions: "Take g = 10 m/s². Answer all objective questions.",
            questions: [
                {
                    id: 1,
                    question: "A car accelerates uniformly from rest to a speed of 20 m/s in 5 seconds. Calculate the distance covered.",
                    options: ["50 m", "100 m", "20 m", "40 m"],
                    correctIndex: 0,
                    explanation: "s = (u + v)/2 * t = (0 + 20)/2 * 5 = 10 * 5 = 50 m."
                },
                {
                    id: 2,
                    question: "Which of the following is NOT an electromagnetic wave?",
                    options: ["Sound waves", "X-rays", "Gamma rays", "Ultraviolet rays"],
                    correctIndex: 0,
                    explanation: "Sound waves are mechanical longitudinal waves requiring a material medium."
                }
            ]
        }
    ],

    // CBT Results History
    cbtResults: [
        {
            examId: "CBT-01",
            examTitle: "WAEC Mathematics Mock Exam",
            score: 5,
            totalQuestions: 5,
            percentage: 100.0,
            passed: true,
            completedAt: "Yesterday, 4:15 PM"
        }
    ],

    // Fee Invoices
    feeInvoices: [
        {
            id: "INV-2026-081",
            studentId: "STU-0482",
            title: "2nd Term 2025/2026 Tuition & Boarding Levy",
            totalAmount: 185000,
            amountPaid: 185000,
            balanceDue: 0,
            status: "PAID",
            dueDate: "Jan 15, 2026",
            items: [
                { name: "Senior Secondary Tuition Fee", amount: 95000 },
                { name: "Nelson Mandela Boarding & Nutrition", amount: 60000 },
                { name: "WAEC & NECO Registration Levy", amount: 20000 },
                { name: "ICT, STEM Robotics & Laboratory Levy", amount: 10000 }
            ]
        },
        {
            id: "INV-2026-082",
            studentId: "STU-0621",
            title: "2nd Term 2025/2026 Junior School Tuition & Bus Transport",
            totalAmount: 145000,
            amountPaid: 75000,
            balanceDue: 70000,
            status: "PARTIAL",
            dueDate: "Feb 28, 2026",
            items: [
                { name: "Junior Secondary Tuition Fee", amount: 80000 },
                { name: "College Transit Bus Service (Lekki Route A)", amount: 45000 },
                { name: "PTA Development Levy", amount: 10000 },
                { name: "Library & Co-curricular Levy", amount: 10000 }
            ]
        }
    ],

    // Payment Receipts
    paymentReceipts: [
        {
            receiptNumber: "HIC-RCP-2026-9041",
            transactionRef: "PSTK_REF_9918237490",
            studentName: "Chinedu Emmanuel Okafor",
            admissionNo: "HIC/2023/0482",
            payerName: "Barr. Samuel Okafor",
            amountPaid: 185000,
            paymentGateway: "Paystack (MasterCard)",
            paymentDate: "Jan 12, 2026, 10:14 AM",
            termSession: "2nd Term 2025/2026"
        },
        {
            receiptNumber: "HIC-RCP-2026-9082",
            transactionRef: "PSTK_REF_1102938475",
            studentName: "Amina Fatima Okafor",
            admissionNo: "HIC/2024/0621",
            payerName: "Barr. Samuel Okafor",
            amountPaid: 75000,
            paymentGateway: "Paystack (Bank Transfer)",
            paymentDate: "Feb 02, 2026, 02:45 PM",
            termSession: "2nd Term 2025/2026"
        }
    ],

    // Assignments
    assignments: [
        {
            id: "ASN-01",
            title: "Calculus & Derivatives in Kinematics",
            subject: "Further Mathematics",
            targetClass: "SSS 3 Science A",
            dueDate: "Next Monday, 8:00 AM",
            teacherName: "Mr. B. Adeyemi",
            description: "Solve questions 1 through 15 in New General Further Mathematics, Chapter 8 on Velocity and Acceleration curves.",
            isSubmitted: true,
            submissionText: "Completed and submitted working steps via notebook scan and portal text upload.",
            scoreAwarded: 19,
            maxScore: 20,
            feedback: "Brilliant step-by-step calculus working, Chinedu! Distinction work."
        },
        {
            id: "ASN-02",
            title: "Electromagnetic Induction & Faraday's Law",
            subject: "Physics",
            targetClass: "SSS 3 Science A",
            dueDate: "Friday, 4:00 PM",
            teacherName: "Dr. K. Obi",
            description: "State Lenz's Law and calculate the induced electromotive force for a 500-turn coil rotating in a 0.2 Tesla magnetic field.",
            isSubmitted: false,
            submissionText: "",
            scoreAwarded: null,
            maxScore: 20,
            feedback: ""
        }
    ],

    // Attendance Entries (Teacher Portal)
    attendanceEntries: [
        { studentId: "STU-0482", studentName: "Chinedu Emmanuel Okafor", admissionNo: "HIC/2023/0482", isPresent: true, remarks: "Present (Senior Prefect)" },
        { studentId: "STU-0483", studentName: "Adeleke Oluwaseun", admissionNo: "HIC/2023/0483", isPresent: true, remarks: "Present" },
        { studentId: "STU-0484", studentName: "Balogun Farouk", admissionNo: "HIC/2023/0484", isPresent: false, remarks: "Excused (Medical Clinic)" },
        { studentId: "STU-0485", studentName: "Chukwuma Ngozi", admissionNo: "HIC/2023/0485", isPresent: true, remarks: "Present" },
        { studentId: "STU-0486", studentName: "Danjuma Aisha", admissionNo: "HIC/2023/0486", isPresent: true, remarks: "Present" }
    ],

    // Admissions Desk (Admin Portal)
    admissions: [
        {
            applicationNo: "HIC-ADM-2026-091",
            applicantFullName: "Somtochukwu David Obi",
            gender: "Male",
            dateOfBirth: "2014-06-18",
            entryClass: "JSS 1",
            parentGuardianName: "Engr. Charles Obi",
            parentPhone: "+234 802 334 9901",
            parentEmail: "charles.obi@gmail.com",
            previousSchool: "Corona Primary School, Gbagada",
            status: "Shortlisted for CBT",
            cbtScore: 84,
            submissionDate: "Feb 10, 2026"
        },
        {
            applicationNo: "HIC-ADM-2026-092",
            applicantFullName: "Zainab Haliru Bello",
            gender: "Female",
            dateOfBirth: "2010-09-12",
            entryClass: "SSS 1 Science",
            parentGuardianName: "Alhaji Haliru Bello",
            parentPhone: "+234 803 778 2211",
            parentEmail: "hbello@yahoo.com",
            previousSchool: "Federal Government College, Ijanikin",
            status: "Offered Admission",
            cbtScore: 92,
            submissionDate: "Jan 28, 2026"
        },
        {
            applicationNo: "HIC-ADM-2026-093",
            applicantFullName: "Favour Oluwadamilola Peters",
            gender: "Female",
            dateOfBirth: "2014-03-24",
            entryClass: "JSS 1",
            parentGuardianName: "Dr. Mrs. Grace Peters",
            parentPhone: "+234 805 112 3456",
            parentEmail: "gpeters@clinic.com",
            previousSchool: "Chrisland Schools, Ikeja",
            status: "Submitted",
            cbtScore: null,
            submissionDate: "Feb 18, 2026"
        }
    ],

    // Hostel Rooms
    hostelRooms: [
        { roomNumber: "Mandela Room 101", hallName: "Nelson Mandela Senior Hall", capacity: 4, occupied: 4, floor: "Ground Floor", prefectName: "Chinedu Okafor" },
        { roomNumber: "Mandela Room 102", hallName: "Nelson Mandela Senior Hall", capacity: 4, occupied: 3, floor: "Ground Floor", prefectName: "Adeleke Seun" },
        { roomNumber: "Amina Room 201", hallName: "Queen Amina Junior Hall", capacity: 6, occupied: 5, floor: "1st Floor", prefectName: "Amina Bello" }
    ],

    // Transport Routes
    transportRoutes: [
        { routeCode: "BUS-R01", routeName: "Lekki Phase 1 – Victoria Island – Ikoyi", busNumber: "Toyota Coaster (HIC-01-LA)", driverName: "Mr. Sunday James", driverPhone: "+234 803 111 2233", feePerTerm: 45000, pickupPoints: ["Admiralty Circle", "Oniru Estate", "1004 Housing", "Falomo Roundabout"], departureTime: "6:45 AM" },
        { routeCode: "BUS-R02", routeName: "Ajah – Chevron – VGC – Jakande", busNumber: "Toyota Coaster (HIC-02-LA)", driverName: "Mr. Musa Ibrahim", driverPhone: "+234 802 444 5566", feePerTerm: 40000, pickupPoints: ["Ajah Jubilee Bridge", "VGC Main Gate", "Chevron Roundabout"], departureTime: "6:30 AM" }
    ],

    // Digital Library
    libraryBooks: [
        { title: "New General Mathematics for Senior Secondary Schools (SSS 1-3)", author: "M.F. Macrae et al.", isbn: "978-978-081-321-4", category: "Mathematics", availableCopies: 28, totalCopies: 35, shelfLocation: "Shelf M-02" },
        { title: "Senior Secondary Physics (Revised Edition)", author: "P.N. Okeke & M.W. Anyakoha", isbn: "978-978-175-401-2", category: "Science", availableCopies: 22, totalCopies: 30, shelfLocation: "Shelf P-04" },
        { title: "Comprehensive Certificate Chemistry", author: "G.O. Ojokuku", isbn: "978-978-220-112-9", category: "Science", availableCopies: 19, totalCopies: 25, shelfLocation: "Shelf C-01" },
        { title: "The Lion and the Jewel (WAEC Literature)", author: "Wole Soyinka", isbn: "978-019-911-083-4", category: "Literature", availableCopies: 40, totalCopies: 45, shelfLocation: "Shelf L-05" }
    ],

    // Announcements
    announcements: [
        { id: 1, title: "2026/2027 Entrance Examination CBT Date", audience: "All", message: "Entrance assessment and scholarship screening for all prospective Junior & Senior candidates will take place on Saturday, March 28, 2026 at the STEM AI Hub.", date: "Today, 08:30 AM", urgent: true },
        { id: 2, title: "Inter-House Sports Competition Finals", audience: "Students & Parents", message: "The 24th Annual Inter-House Athletics & March Past will be held at the Main Sports Complex on Friday, March 13th.", date: "Yesterday", urgent: false },
        { id: 3, title: "Mid-Term Break & Terminal Result Release Notice", audience: "Parents & Staff", message: "Mid-term results for the 2nd Term 2025/2026 session have been published. Parents can access official report cards and receipts online.", date: "3 days ago", urgent: false }
    ]
};
