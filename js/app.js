/**
 * Hilltop International College Portal - Master Application Controller
 * Handles UI routing, role switching, portal rendering, modals, and user actions.
 */

// Initialize Application on DOM Ready
document.addEventListener('DOMContentLoaded', () => {
    switchPortal('public');
    updateNotificationBadge();
});

// Role Switcher Navigation
function switchPortal(role) {
    HilltopData.currentRole = role;

    // Update Role Pill Display
    const avatar = document.getElementById('currentRoleAvatar');
    const label = document.getElementById('currentRoleLabel');
    const name = document.getElementById('currentRoleName');

    const roleConfig = {
        'public': { label: 'Guest / Visitor', name: 'Explore College', icon: 'fa-globe' },
        'student': { label: 'Student Portal', name: 'Chinedu Okafor (SSS 3)', icon: 'fa-user-graduate' },
        'parent': { label: 'Parent Portal', name: 'Barr. Samuel Okafor', icon: 'fa-users' },
        'teacher': { label: 'Teacher Portal', name: 'Mr. B. Adeyemi (HOD)', icon: 'fa-chalkboard-teacher' },
        'admin': { label: 'Admin Console', name: 'Principal / Bursary Desk', icon: 'fa-user-shield' }
    };

    const cfg = roleConfig[role] || roleConfig['public'];
    if (label) label.innerText = cfg.label;
    if (name) name.innerText = cfg.name;
    if (avatar) avatar.innerHTML = `<i class="fas ${cfg.icon}"></i>`;

    // Render Portal View
    switch (role) {
        case 'student':
            renderStudentPortal();
            break;
        case 'parent':
            renderParentPortal();
            break;
        case 'teacher':
            renderTeacherPortal();
            break;
        case 'admin':
            renderAdminPortal();
            break;
        default:
            renderPublicHomepage();
            break;
    }

    closeRoleModal();
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

// 1. PUBLIC HOMEPAGE VIEW
function renderPublicHomepage() {
    const container = document.getElementById('appContent');
    if (!container) return;

    container.innerHTML = `
        <!-- Hero Banner Section -->
        <section class="hero-section">
            <div class="hero-container">
                <div>
                    <span class="section-tag" style="background:rgba(255,255,255,0.18); color:#FFF;">
                        <i class="fas fa-certificate text-warning"></i> Premier Nigerian Secondary Institution
                    </span>
                    <h1 class="hero-title">Nurturing Leaders of <span>Excellence, Integrity</span> and Faith</h1>
                    <p class="hero-subtitle">
                        Empowering young minds through world-class science, arts, robotics, and Cambridge curriculum. We guarantee 100% WAEC & NECO distinction rates with holistic character development.
                    </p>
                    <div class="hero-cta-group">
                        <button class="btn btn-accent" onclick="openAdmissionModal()">
                            <i class="fas fa-file-signature"></i> Apply for 2026/2027 Admission
                        </button>
                        <button class="btn btn-outline" style="color:#FFF; border-color:rgba(255,255,255,0.4);" onclick="openRoleModal()">
                            <i class="fas fa-sign-in-alt"></i> Login to Portals
                        </button>
                    </div>
                </div>

                <div class="hero-portal-card">
                    <h3 class="hero-portal-title"><i class="fas fa-laptop-code"></i> Quick Portal Access</h3>
                    <p style="font-size:0.85rem; opacity:0.9; margin-bottom:14px;">Direct entry for students, parents, faculty, and administrative officers.</p>
                    <div class="portal-quick-grid">
                        <button class="quick-portal-btn" onclick="switchPortal('student')">
                            <i class="fas fa-user-graduate"></i>
                            <span>Student Portal</span>
                        </button>
                        <button class="quick-portal-btn" onclick="switchPortal('parent')">
                            <i class="fas fa-users"></i>
                            <span>Parent Desk</span>
                        </button>
                        <button class="quick-portal-btn" onclick="switchPortal('teacher')">
                            <i class="fas fa-chalkboard-teacher"></i>
                            <span>Teacher Portal</span>
                        </button>
                        <button class="quick-portal-btn" onclick="switchPortal('admin')">
                            <i class="fas fa-user-shield"></i>
                            <span>Admin / Bursary</span>
                        </button>
                    </div>
                </div>
            </div>
        </section>

        <!-- Stats Counter Bar -->
        <div class="stats-bar">
            <div class="stats-container">
                <div class="stat-box">
                    <div class="stat-icon"><i class="fas fa-user-graduate"></i></div>
                    <div>
                        <div class="stat-number">1,450+</div>
                        <div class="stat-label">Enrolled Scholars</div>
                    </div>
                </div>
                <div class="stat-box">
                    <div class="stat-icon"><i class="fas fa-award"></i></div>
                    <div>
                        <div class="stat-number">100%</div>
                        <div class="stat-label">WAEC & NECO Pass Rate</div>
                    </div>
                </div>
                <div class="stat-box">
                    <div class="stat-icon"><i class="fas fa-chalkboard-teacher"></i></div>
                    <div>
                        <div class="stat-number">85+</div>
                        <div class="stat-label">Certified Educators</div>
                    </div>
                </div>
                <div class="stat-box">
                    <div class="stat-icon"><i class="fas fa-microscope"></i></div>
                    <div>
                        <div class="stat-number">24</div>
                        <div class="stat-label">State-of-the-Art Labs</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Academic Divisions & Curricular Wings -->
        <section class="section-wrap" id="academics">
            <div class="section-header">
                <span class="section-tag">Academic Wings</span>
                <h2 class="section-title">Comprehensive Secondary Education</h2>
                <p class="section-desc">Tailored pathways designed to propel students into top Nigerian and global Ivy League universities.</p>
            </div>

            <div class="academic-grid">
                <div class="feature-card">
                    <div class="feature-icon-box"><i class="fas fa-book-reader"></i></div>
                    <h3 class="feature-card-title">Junior Secondary (JSS 1 - 3)</h3>
                    <p class="feature-card-desc">Strong foundation in Mathematics, Basic Science, ICT Coding, Nigerian & French Languages, and Creative Arts.</p>
                    <button class="btn btn-outline btn-sm" onclick="openAdmissionModal()">Enroll in JSS</button>
                </div>

                <div class="feature-card">
                    <div class="feature-icon-box"><i class="fas fa-atom"></i></div>
                    <h3 class="feature-card-title">Senior Science & STEM Robotics</h3>
                    <p class="feature-card-desc">Advanced Physics, Chemistry, Biology, Further Maths, and AI/Robotics laboratory simulations.</p>
                    <button class="btn btn-outline btn-sm" onclick="openAdmissionModal()">Explore Science</button>
                </div>

                <div class="feature-card">
                    <div class="feature-icon-box"><i class="fas fa-balance-scale"></i></div>
                    <h3 class="feature-card-title">Arts & Humanities Division</h3>
                    <p class="feature-card-desc">Literature in English, Government, History, Christian & Islamic Religious Studies, and Music.</p>
                    <button class="btn btn-outline btn-sm" onclick="openAdmissionModal()">Explore Arts</button>
                </div>

                <div class="feature-card">
                    <div class="feature-icon-box"><i class="fas fa-chart-line"></i></div>
                    <h3 class="feature-card-title">Commercial & Business Studies</h3>
                    <p class="feature-card-desc">Financial Accounting, Commerce, Economics, Marketing, and Entrepreneurship incubation.</p>
                    <button class="btn btn-outline btn-sm" onclick="openAdmissionModal()">Explore Commercial</button>
                </div>
            </div>
        </section>

        <!-- Admission Callout Section -->
        <section class="section-wrap" id="admissions" style="background:#FFFFFF; border-radius:var(--radius-xl); margin-bottom:40px; box-shadow:var(--shadow-sm);">
            <div style="display:grid; grid-template-columns: 1.2fr 0.8fr; gap:30px; align-items:center;">
                <div>
                    <span class="section-tag">2026/2027 Admissions Open</span>
                    <h2 style="font-family:'Outfit',sans-serif; font-size:2rem; font-weight:800; color:var(--primary-dark); margin-bottom:12px;">
                        Give Your Child the Hilltop Advantage
                    </h2>
                    <p style="color:var(--text-muted); font-size:0.95rem; line-height:1.6; margin-bottom:20px;">
                        Applications are now invited from suitably qualified candidates for entrance examinations into JSS 1, JSS 2, and SSS 1. Scholarships are awarded to top 5% entrance examination performers.
                    </p>
                    <button class="btn btn-primary" onclick="openAdmissionModal()">
                        <i class="fas fa-edit"></i> Start Online Application Form
                    </button>
                </div>
                <div style="background:var(--accent-light); padding:24px; border-radius:var(--radius-lg); border:1px solid var(--accent);">
                    <h4 style="font-weight:800; color:var(--primary-dark); margin-bottom:10px;"><i class="fas fa-calendar-alt"></i> Key Admission Dates</h4>
                    <ul style="font-size:0.85rem; display:flex; flex-direction:column; gap:8px;">
                        <li><strong>Form Closes:</strong> March 20, 2026</li>
                        <li><strong>Entrance CBT:</strong> March 28, 2026</li>
                        <li><strong>Interviews:</strong> April 4, 2026</li>
                        <li><strong>Resumption:</strong> September 2026</li>
                    </ul>
                </div>
            </div>
        </section>
    `;
}

// 2. STUDENT PORTAL VIEW
function renderStudentPortal() {
    const container = document.getElementById('appContent');
    if (!container) return;

    const student = HilltopData.students[0]; // Chinedu
    const report = HilltopData.reportCards[0];

    let scoresHtml = '';
    report.scores.forEach(s => {
        scoresHtml += `
            <tr>
                <td><strong>${s.subjectName}</strong></td>
                <td>${s.ca1.toFixed(1)}</td>
                <td>${s.ca2.toFixed(1)}</td>
                <td>${s.testScore.toFixed(1)}</td>
                <td>${s.examScore.toFixed(1)}</td>
                <td><strong class="text-primary">${s.totalScore.toFixed(1)}%</strong></td>
                <td><span class="badge badge-success">${s.grade}</span></td>
                <td>${s.remark}</td>
            </tr>
        `;
    });

    let cbtListHtml = '';
    HilltopData.cbtExams.forEach(exam => {
        cbtListHtml += `
            <div class="feature-card mb-2">
                <div style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:8px;">
                    <div>
                        <span class="badge badge-primary">${exam.subject}</span>
                        <h4 style="font-size:1rem; font-weight:700; margin-top:4px;">${exam.title}</h4>
                        <p style="font-size:0.8rem; color:var(--text-muted);">${exam.durationMinutes} Minutes &bull; ${exam.questions.length} Objective Questions &bull; Pass: ${exam.passMarkPercentage}%</p>
                    </div>
                    <button class="btn btn-primary btn-sm" onclick="CbtEngine.startExam('${exam.id}')">
                        <i class="fas fa-play"></i> Start CBT Test
                    </button>
                </div>
            </div>
        `;
    });

    let homeworkHtml = '';
    HilltopData.assignments.forEach(asn => {
        homeworkHtml += `
            <div class="feature-card mb-2">
                <div style="display:flex; justify-content:space-between; align-items:flex-start;">
                    <div>
                        <span class="badge badge-accent">${asn.subject} &bull; Due ${asn.dueDate}</span>
                        <h4 style="font-size:0.95rem; font-weight:700; margin:4px 0;">${asn.title}</h4>
                        <p style="font-size:0.82rem; color:var(--text-muted);">${asn.description}</p>
                    </div>
                    <span class="badge ${asn.isSubmitted ? 'badge-success' : 'badge-warning'}">
                        ${asn.isSubmitted ? 'SUBMITTED' : 'PENDING'}
                    </span>
                </div>
                ${asn.isSubmitted ? `
                    <div style="background:#F0FDF4; padding:10px; border-radius:var(--radius-sm); margin-top:8px; font-size:0.8rem;">
                        <strong>Your Solution:</strong> ${asn.submissionText}<br>
                        <strong>Grade:</strong> <span class="text-success font-bold">${asn.scoreAwarded} / ${asn.maxScore} pts</span> &bull; "${asn.feedback}"
                    </div>
                ` : `
                    <button class="btn btn-outline btn-sm mt-2" onclick="submitHomeworkSolution('${asn.id}')">
                        <i class="fas fa-upload"></i> Submit Homework
                    </button>
                `}
            </div>
        `;
    });

    container.innerHTML = `
        <div class="portal-container">
            <!-- Student Header Profile Banner -->
            <div class="portal-top-banner">
                <div class="portal-user-meta">
                    <div class="portal-avatar-lg">👨‍🎓</div>
                    <div>
                        <div class="portal-user-name">${student.name}</div>
                        <div class="portal-user-sub">
                            Adm No: <strong>${student.admissionNo}</strong> &bull; Class: <strong>${student.className}</strong> &bull; House: <strong>${student.house} (${student.role})</strong>
                        </div>
                    </div>
                </div>
                <div>
                    <button class="btn btn-accent btn-sm" onclick="openReportCardModal('${student.id}')">
                        <i class="fas fa-download"></i> Official Report Card PDF
                    </button>
                </div>
            </div>

            <!-- KPI Metrics -->
            <div class="metrics-row">
                <div class="metric-card success">
                    <div class="metric-label">Term Average</div>
                    <div class="metric-val text-success">${report.averageScore.toFixed(1)}%</div>
                    <div class="metric-sub">Distinction Scale</div>
                </div>
                <div class="metric-card">
                    <div class="metric-label">Class Position</div>
                    <div class="metric-val text-primary">${report.classPosition}nd / ${report.totalStudentsInClass}</div>
                    <div class="metric-sub">Top 5% Cohort</div>
                </div>
                <div class="metric-card gold">
                    <div class="metric-label">Cumulative GPA</div>
                    <div class="metric-val" style="color:var(--gold);">${report.gpa.toFixed(2)} / 5.0</div>
                    <div class="metric-sub">Senior Science A</div>
                </div>
                <div class="metric-card accent">
                    <div class="metric-label">Attendance</div>
                    <div class="metric-val text-info">${((report.attendancePresent/report.attendanceTotalDays)*100).toFixed(1)}%</div>
                    <div class="metric-sub">${report.attendancePresent} / ${report.attendanceTotalDays} Days</div>
                </div>
            </div>

            <!-- Terminal Results Table -->
            <div class="table-card">
                <div class="table-header-bar">
                    <div>
                        <h3 class="table-title">2nd Term Examination & Continuous Assessment Results</h3>
                        <p style="font-size:0.8rem; color:var(--text-muted);">Continuous Assessment (30%) + Examination (70%) = 100%</p>
                    </div>
                    <button class="btn btn-primary btn-sm" onclick="openReportCardModal('${student.id}')">
                        <i class="fas fa-print"></i> Printable Report Sheet
                    </button>
                </div>
                <div style="overflow-x:auto;">
                    <table class="custom-table">
                        <thead>
                            <tr>
                                <th>Subject</th>
                                <th>CA 1 (10)</th>
                                <th>CA 2 (10)</th>
                                <th>Test (10)</th>
                                <th>Exam (70)</th>
                                <th>Total (100)</th>
                                <th>Grade</th>
                                <th>Remark</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${scoresHtml}
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- CBT Exams & Homework Two-Column Section -->
            <div style="display:grid; grid-template-columns: 1fr 1fr; gap:20px;">
                <div>
                    <h3 style="font-family:'Outfit',sans-serif; font-size:1.2rem; font-weight:800; margin-bottom:12px;">
                        <i class="fas fa-laptop-code text-primary"></i> CBT Practice Examination Center
                    </h3>
                    ${cbtListHtml}
                </div>

                <div>
                    <h3 style="font-family:'Outfit',sans-serif; font-size:1.2rem; font-weight:800; margin-bottom:12px;">
                        <i class="fas fa-tasks text-accent"></i> Active Homework & Coursework
                    </h3>
                    ${homeworkHtml}
                </div>
            </div>
        </div>
    `;
}

// 3. PARENT PORTAL VIEW
function renderParentPortal(activeChildId = 'STU-0482') {
    const container = document.getElementById('appContent');
    if (!container) return;

    const currentChild = HilltopData.students.find(s => s.id === activeChildId) || HilltopData.students[0];
    const report = HilltopData.reportCards.find(r => r.studentId === activeChildId) || HilltopData.reportCards[0];
    const invoices = HilltopData.feeInvoices.filter(i => i.studentId === activeChildId);

    let invoicesHtml = '';
    invoices.forEach(inv => {
        invoicesHtml += `
            <div class="feature-card mb-2">
                <div style="display:flex; justify-content:space-between; align-items:center;">
                    <div>
                        <h4 style="font-size:1rem; font-weight:700;">${inv.title}</h4>
                        <p style="font-size:0.8rem; color:var(--text-muted);">Due: ${inv.dueDate}</p>
                    </div>
                    <span class="badge ${inv.status === 'PAID' ? 'badge-success' : 'badge-danger'}">${inv.status}</span>
                </div>
                <div style="display:flex; justify-content:space-between; margin-top:8px; font-size:0.85rem;">
                    <span>Total: ₦${inv.totalAmount.toLocaleString('en-NG')}</span>
                    <span>Paid: <strong class="text-success">₦${inv.amountPaid.toLocaleString('en-NG')}</strong></span>
                    <span>Balance Due: <strong class="text-danger">₦${inv.balanceDue.toLocaleString('en-NG')}</strong></span>
                </div>
                ${inv.balanceDue > 0 ? `
                    <button class="btn btn-primary btn-sm mt-3" style="width:100%;" onclick="PaymentEngine.openCheckout('${inv.id}')">
                        <i class="fas fa-credit-card"></i> Pay Outstanding ₦${inv.balanceDue.toLocaleString('en-NG')} via Paystack
                    </button>
                ` : `
                    <div class="text-success mt-2" style="font-size:0.82rem; font-weight:700;"><i class="fas fa-check-circle"></i> Fees Cleared in Full</div>
                `}
            </div>
        `;
    });

    let receiptsHtml = '';
    HilltopData.paymentReceipts.forEach(rcp => {
        receiptsHtml += `
            <div class="feature-card mb-2">
                <div style="display:flex; justify-content:space-between; align-items:center;">
                    <div>
                        <span class="badge badge-primary">${rcp.receiptNumber}</span>
                        <h5 style="font-size:0.9rem; font-weight:700; margin-top:4px;">${rcp.studentName}</h5>
                        <p style="font-size:0.78rem; color:var(--text-muted);">${rcp.paymentGateway} &bull; ${rcp.paymentDate}</p>
                    </div>
                    <div style="text-align:right;">
                        <span style="font-weight:800; color:var(--success); font-size:1.1rem;">₦${rcp.amountPaid.toLocaleString('en-NG')}</span><br>
                        <small class="text-muted">${rcp.transactionRef}</small>
                    </div>
                </div>
            </div>
        `;
    });

    container.innerHTML = `
        <div class="portal-container">
            <!-- Parent Profile Banner -->
            <div class="portal-top-banner" style="background:linear-gradient(135deg, #1565C0, #0D47A1);">
                <div class="portal-user-meta">
                    <div class="portal-avatar-lg">👨‍👩‍👧</div>
                    <div>
                        <div class="portal-user-name">Barrister Samuel Okafor</div>
                        <div class="portal-user-sub">Guardian ID: <strong>PAR-0112</strong> &bull; Registered Wards: <strong>2 Scholars</strong></div>
                    </div>
                </div>
                <!-- Ward Switcher Pills -->
                <div style="display:flex; gap:8px;">
                    <button class="btn ${activeChildId === 'STU-0482' ? 'btn-accent' : 'btn-outline'}" style="${activeChildId !== 'STU-0482' ? 'color:#FFF; border-color:#FFF;' : ''}" onclick="renderParentPortal('STU-0482')">
                        <i class="fas fa-user-graduate"></i> Chinedu (SSS 3)
                    </button>
                    <button class="btn ${activeChildId === 'STU-0621' ? 'btn-accent' : 'btn-outline'}" style="${activeChildId !== 'STU-0621' ? 'color:#FFF; border-color:#FFF;' : ''}" onclick="renderParentPortal('STU-0621')">
                        <i class="fas fa-user-graduate"></i> Amina (JSS 2)
                    </button>
                </div>
            </div>

            <!-- Ward Academic Summary -->
            <div class="metrics-row">
                <div class="metric-card success">
                    <div class="metric-label">Ward Term Average</div>
                    <div class="metric-val text-success">${report.averageScore.toFixed(1)}%</div>
                    <div class="metric-sub">Rank: #${report.classPosition} of ${report.totalStudentsInClass}</div>
                </div>
                <div class="metric-card">
                    <div class="metric-label">Class & Stream</div>
                    <div class="metric-val text-primary" style="font-size:1.3rem;">${currentChild.className}</div>
                    <div class="metric-sub">Adm: ${currentChild.admissionNo}</div>
                </div>
                <div class="metric-card gold">
                    <div class="metric-label">Conduct & Discipline</div>
                    <div class="metric-val" style="color:var(--gold); font-size:1.3rem;">Exemplary</div>
                    <div class="metric-sub">House: ${currentChild.house}</div>
                </div>
            </div>

            <div style="display:grid; grid-template-columns:1.2fr 0.8fr; gap:20px;">
                <!-- Fee Invoices & Payment Portal -->
                <div>
                    <h3 style="font-family:'Outfit',sans-serif; font-size:1.2rem; font-weight:800; margin-bottom:12px;">
                        <i class="fas fa-file-invoice-dollar text-primary"></i> School Fees & Term Invoices
                    </h3>
                    ${invoicesHtml}

                    <div class="mt-4">
                        <h3 style="font-family:'Outfit',sans-serif; font-size:1.2rem; font-weight:800; margin-bottom:12px;">
                            <i class="fas fa-receipt text-success"></i> Official Payment Receipts
                        </h3>
                        ${receiptsHtml}
                    </div>
                </div>

                <!-- Academic Report Card Download & Teacher Contact -->
                <div>
                    <div class="feature-card mb-3">
                        <h4 style="font-weight:800; color:var(--primary-dark);"><i class="fas fa-graduation-cap"></i> Terminal Performance Sheet</h4>
                        <p style="font-size:0.85rem; color:var(--text-muted); margin:6px 0 14px 0;">View verified WAEC Continuous Assessment & Examination scores with psychomotor ratings.</p>
                        <button class="btn btn-primary" style="width:100%;" onclick="openReportCardModal('${currentChild.id}')">
                            <i class="fas fa-file-pdf"></i> View & Print ${currentChild.name.split(' ')[0]}'s Report Card
                        </button>
                    </div>

                    <div class="feature-card">
                        <h4 style="font-weight:800; color:var(--primary-dark);"><i class="fas fa-comment-dots"></i> Message Class Teacher</h4>
                        <p style="font-size:0.85rem; color:var(--text-muted); margin:6px 0;">Direct line to Form Teacher (Mr. B. Adeyemi).</p>
                        <textarea class="form-control mb-2" rows="3" placeholder="Type message or request academic consultation..."></textarea>
                        <button class="btn btn-outline btn-sm" onclick="showToast('Message sent to Form Teacher!')">
                            <i class="fas fa-paper-plane"></i> Send Message
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `;
}

// 4. TEACHER PORTAL VIEW
function renderTeacherPortal() {
    const container = document.getElementById('appContent');
    if (!container) return;

    const report = HilltopData.reportCards[0]; // SSS 3 Science A

    let scoreRowsHtml = '';
    report.scores.forEach(s => {
        scoreRowsHtml += `
            <tr>
                <td><strong>${s.subjectName}</strong></td>
                <td><input type="number" class="form-control" style="width:70px; padding:4px;" value="${s.ca1}" onchange="updateTeacherScore('${s.subjectCode}', 'ca1', this.value)"></td>
                <td><input type="number" class="form-control" style="width:70px; padding:4px;" value="${s.ca2}" onchange="updateTeacherScore('${s.subjectCode}', 'ca2', this.value)"></td>
                <td><input type="number" class="form-control" style="width:70px; padding:4px;" value="${s.testScore}" onchange="updateTeacherScore('${s.subjectCode}', 'test', this.value)"></td>
                <td><input type="number" class="form-control" style="width:75px; padding:4px;" value="${s.examScore}" onchange="updateTeacherScore('${s.subjectCode}', 'exam', this.value)"></td>
                <td><strong class="text-primary">${s.totalScore.toFixed(1)}%</strong></td>
                <td><span class="badge badge-success">${s.grade}</span></td>
                <td><small>${s.remark}</small></td>
            </tr>
        `;
    });

    let attendanceHtml = '';
    HilltopData.attendanceEntries.forEach(att => {
        attendanceHtml += `
            <tr>
                <td><strong>${att.studentName}</strong></td>
                <td>${att.admissionNo}</td>
                <td>
                    <button class="btn btn-sm ${att.isPresent ? 'btn-primary' : 'btn-outline'}" onclick="toggleAttendance('${att.studentId}')">
                        <i class="fas ${att.isPresent ? 'fa-check' : 'fa-times'}"></i> ${att.isPresent ? 'PRESENT' : 'ABSENT'}
                    </button>
                </td>
                <td><small class="text-muted">${att.remarks}</small></td>
            </tr>
        `;
    });

    container.innerHTML = `
        <div class="portal-container">
            <!-- Teacher Profile Banner -->
            <div class="portal-top-banner" style="background:linear-gradient(135deg, #2E7D32, #1B5E20);">
                <div class="portal-user-meta">
                    <div class="portal-avatar-lg">👨‍🏫</div>
                    <div>
                        <div class="portal-user-name">Mr. Babatunde Adeyemi (B.Sc, PGDE)</div>
                        <div class="portal-user-sub">HOD Science & Mathematics &bull; Form Master: <strong>SSS 3 Science A</strong></div>
                    </div>
                </div>
                <div>
                    <button class="btn btn-accent btn-sm" onclick="showToast('Scores synchronized to database!')">
                        <i class="fas fa-save"></i> Save Grade Sheet
                    </button>
                </div>
            </div>

            <!-- Continuous Assessment 30% + Exam 70% Entry Table -->
            <div class="table-card">
                <div class="table-header-bar">
                    <div>
                        <h3 class="table-title">Continuous Assessment & Exam Score Entry (SSS 3 Science A)</h3>
                        <p style="font-size:0.8rem; color:var(--text-muted);">Grading Formula: 1st CA (10) + 2nd CA (10) + Test (10) + Exam (70) = 100%</p>
                    </div>
                    <span class="badge badge-primary">Student: Chinedu Okafor (HIC/2023/0482)</span>
                </div>
                <div style="overflow-x:auto;">
                    <table class="custom-table">
                        <thead>
                            <tr>
                                <th>Subject</th>
                                <th>1st CA (10)</th>
                                <th>2nd CA (10)</th>
                                <th>Test (10)</th>
                                <th>Exam (70)</th>
                                <th>Total (100)</th>
                                <th>Grade</th>
                                <th>Remark</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${scoreRowsHtml}
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Daily Class Attendance Register -->
            <div class="table-card">
                <div class="table-header-bar">
                    <h3 class="table-title">Daily Attendance Register &bull; SSS 3 Science A</h3>
                    <span class="badge badge-success">4 of 5 Present Today</span>
                </div>
                <div style="overflow-x:auto;">
                    <table class="custom-table">
                        <thead>
                            <tr>
                                <th>Student Full Name</th>
                                <th>Admission No</th>
                                <th>Status</th>
                                <th>Remarks</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${attendanceHtml}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    `;
}

// 5. ADMIN PORTAL VIEW
function renderAdminPortal() {
    const container = document.getElementById('appContent');
    if (!container) return;

    let admissionsHtml = '';
    HilltopData.admissions.forEach(adm => {
        admissionsHtml += `
            <tr>
                <td><strong>${adm.applicationNo}</strong></td>
                <td>${adm.applicantFullName}</td>
                <td>${adm.entryClass}</td>
                <td>${adm.parentGuardianName} (${adm.parentPhone})</td>
                <td><strong>${adm.cbtScore !== null ? adm.cbtScore + '%' : 'Pending'}</strong></td>
                <td>
                    <span class="badge ${adm.status === 'Offered Admission' ? 'badge-success' : 'badge-primary'}">
                        ${adm.status}
                    </span>
                </td>
                <td>
                    <button class="btn btn-outline btn-sm" onclick="grantAdmission('${adm.applicationNo}')">
                        <i class="fas fa-check"></i> Grant Admission
                    </button>
                </td>
            </tr>
        `;
    });

    let hostelHtml = '';
    HilltopData.hostelRooms.forEach(h => {
        hostelHtml += `
            <div class="feature-card mb-2">
                <div style="display:flex; justify-content:space-between;">
                    <div>
                        <h4 style="font-size:0.95rem; font-weight:700;">${h.roomNumber} (${h.floor})</h4>
                        <p style="font-size:0.8rem; color:var(--text-muted);">${h.hallName} &bull; Prefect: ${h.prefectName}</p>
                    </div>
                    <span class="badge ${h.occupied === h.capacity ? 'badge-danger' : 'badge-success'}">
                        ${h.occupied} / ${h.capacity} Beds Occupied
                    </span>
                </div>
            </div>
        `;
    });

    container.innerHTML = `
        <div class="portal-container">
            <!-- Admin Banner -->
            <div class="portal-top-banner" style="background:linear-gradient(135deg, #4A148C, #6A1B9A);">
                <div class="portal-user-meta">
                    <div class="portal-avatar-lg">🏛️</div>
                    <div>
                        <div class="portal-user-name">Central Administration & Bursary Console</div>
                        <div class="portal-user-sub">Principal: <strong>Dr. Emmanuel Okafor</strong> &bull; Governing Board Portal</div>
                    </div>
                </div>
                <div>
                    <button class="btn btn-accent btn-sm" onclick="showToast('Database backup archive generated successfully!')">
                        <i class="fas fa-database"></i> Backup System DB
                    </button>
                </div>
            </div>

            <!-- Executive KPIs -->
            <div class="metrics-row">
                <div class="metric-card">
                    <div class="metric-label">Total Enrollment</div>
                    <div class="metric-val text-primary">1,450</div>
                    <div class="metric-sub">Junior: 680 &bull; Senior: 770</div>
                </div>
                <div class="metric-card success">
                    <div class="metric-label">Revenue Collected</div>
                    <div class="metric-val text-success">₦142.5M</div>
                    <div class="metric-sub">Paystack & Bank Settlement</div>
                </div>
                <div class="metric-card warning">
                    <div class="metric-label">Outstanding Fees</div>
                    <div class="metric-val text-danger">₦18.2M</div>
                    <div class="metric-sub">124 Invoices Pending</div>
                </div>
                <div class="metric-card accent">
                    <div class="metric-label">Staff Strength</div>
                    <div class="metric-val text-info">85</div>
                    <div class="metric-sub">100% Certified Faculty</div>
                </div>
            </div>

            <!-- Admissions Desk -->
            <div class="table-card">
                <div class="table-header-bar">
                    <h3 class="table-title">2026/2027 Entrance Admission Applications Desk</h3>
                    <span class="badge badge-primary">3 New Applications</span>
                </div>
                <div style="overflow-x:auto;">
                    <table class="custom-table">
                        <thead>
                            <tr>
                                <th>App No</th>
                                <th>Applicant Full Name</th>
                                <th>Class</th>
                                <th>Parent Contact</th>
                                <th>CBT Score</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${admissionsHtml}
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Hostel Boarding & Bus Fleet Management -->
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:20px;">
                <div>
                    <h3 style="font-family:'Outfit',sans-serif; font-size:1.2rem; font-weight:800; margin-bottom:12px;">
                        <i class="fas fa-bed text-primary"></i> Boarding House & Hostel Allocation
                    </h3>
                    ${hostelHtml}
                </div>

                <div>
                    <h3 style="font-family:'Outfit',sans-serif; font-size:1.2rem; font-weight:800; margin-bottom:12px;">
                        <i class="fas fa-bullhorn text-accent"></i> Dispatch Urgent Campus Bulletin
                    </h3>
                    <div class="feature-card">
                        <input type="text" id="adminBroadcastTitle" class="form-control mb-2" placeholder="Broadcast Headline">
                        <textarea id="adminBroadcastMsg" class="form-control mb-2" rows="3" placeholder="Type notice to push across Student, Parent, and Teacher portals..."></textarea>
                        <button class="btn btn-primary" onclick="broadcastNotice()">
                            <i class="fas fa-paper-plane"></i> Broadcast Notice Now
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `;
}

// Score Update Helper for Teacher Portal
function updateTeacherScore(subjectCode, field, value) {
    const report = HilltopData.reportCards[0];
    const scoreObj = report.scores.find(s => s.subjectCode === subjectCode);
    if (!scoreObj) return;

    const num = parseFloat(value) || 0;
    if (field === 'ca1') scoreObj.ca1 = num;
    if (field === 'ca2') scoreObj.ca2 = num;
    if (field === 'test') scoreObj.testScore = num;
    if (field === 'exam') scoreObj.examScore = num;

    scoreObj.totalScore = scoreObj.ca1 + scoreObj.ca2 + scoreObj.testScore + scoreObj.examScore;

    // Recalculate Grade according to WAEC standard
    if (scoreObj.totalScore >= 80) { scoreObj.grade = "A1"; scoreObj.remark = "Excellent Distinction"; }
    else if (scoreObj.totalScore >= 75) { scoreObj.grade = "B2"; scoreObj.remark = "Very Good"; }
    else if (scoreObj.totalScore >= 70) { scoreObj.grade = "B3"; scoreObj.remark = "Good"; }
    else if (scoreObj.totalScore >= 65) { scoreObj.grade = "C4"; scoreObj.remark = "Credit (Strong)"; }
    else if (scoreObj.totalScore >= 60) { scoreObj.grade = "C5"; scoreObj.remark = "Credit"; }
    else if (scoreObj.totalScore >= 50) { scoreObj.grade = "C6"; scoreObj.remark = "Credit (Pass)"; }
    else if (scoreObj.totalScore >= 45) { scoreObj.grade = "D7"; scoreObj.remark = "Pass"; }
    else if (scoreObj.totalScore >= 40) { scoreObj.grade = "E8"; scoreObj.remark = "Weak Pass"; }
    else { scoreObj.grade = "F9"; scoreObj.remark = "Fail"; }

    showToast(`Updated ${scoreObj.subjectName} -> Total: ${scoreObj.totalScore.toFixed(1)}% (${scoreObj.grade})`);
}

// Attendance toggle helper
function toggleAttendance(studentId) {
    const entry = HilltopData.attendanceEntries.find(a => a.studentId === studentId);
    if (entry) {
        entry.isPresent = !entry.isPresent;
        renderTeacherPortal();
        showToast(`${entry.studentName} marked as ${entry.isPresent ? 'PRESENT' : 'ABSENT'}`);
    }
}

// Grant Admission Action (Admin)
function grantAdmission(appNo) {
    const adm = HilltopData.admissions.find(a => a.applicationNo === appNo);
    if (adm) {
        adm.status = "Offered Admission";
        renderAdminPortal();
        showToast(`Admission granted for ${adm.applicantFullName}! Provisional offer letter generated.`);
    }
}

// Broadcast Announcement Action (Admin)
function broadcastNotice() {
    const title = document.getElementById('adminBroadcastTitle').value;
    const msg = document.getElementById('adminBroadcastMsg').value;
    if (!title || !msg) {
        showToast('Please enter both headline and message body');
        return;
    }

    HilltopData.announcements.unshift({
        id: Date.now(),
        title: title,
        audience: "All",
        message: msg,
        date: "Just now",
        urgent: true
    });

    updateNotificationBadge();
    showToast('Campus bulletin broadcasted successfully!');
    document.getElementById('adminBroadcastTitle').value = '';
    document.getElementById('adminBroadcastMsg').value = '';
}

// Submit Homework Solution Helper (Student)
function submitHomeworkSolution(asnId) {
    const text = prompt("Enter your homework solution / answers:");
    if (text) {
        const asn = HilltopData.assignments.find(a => a.id === asnId);
        if (asn) {
            asn.isSubmitted = true;
            asn.submissionText = text;
            asn.scoreAwarded = 18;
            asn.feedback = "Good working steps submitted!";
            renderStudentPortal();
            showToast('Homework solution submitted to teacher!');
        }
    }
}

// Online Admission Form Submission
function handleAdmissionSubmit(e) {
    e.preventDefault();
    const name = document.getElementById('adm_name').value;
    const entryClass = document.getElementById('adm_class').value;
    const parentName = document.getElementById('adm_parent_name').value;
    const parentPhone = document.getElementById('adm_parent_phone').value;
    const parentEmail = document.getElementById('adm_parent_email').value;

    const newAppNo = 'HIC-ADM-2026-0' + Math.floor(100 + Math.random() * 900);

    HilltopData.admissions.unshift({
        applicationNo: newAppNo,
        applicantFullName: name,
        gender: document.getElementById('adm_gender').value,
        dateOfBirth: document.getElementById('adm_dob').value,
        entryClass: entryClass,
        parentGuardianName: parentName,
        parentPhone: parentPhone,
        parentEmail: parentEmail,
        previousSchool: document.getElementById('adm_prev_school').value,
        status: "Submitted",
        cbtScore: null,
        submissionDate: "Just now"
    });

    closeAdmissionModal();
    showToast(`Application submitted successfully! Tracking Ref: ${newAppNo}`);
}

// MODAL CONTROLS
function openRoleModal() {
    document.getElementById('roleModal').classList.add('active');
}

function closeRoleModal() {
    document.getElementById('roleModal').classList.remove('active');
}

function selectRole(role) {
    switchPortal(role);
}

function openAdmissionModal() {
    document.getElementById('admissionModal').classList.add('active');
}

function closeAdmissionModal() {
    document.getElementById('admissionModal').classList.remove('active');
}

// NOTIFICATION DRAWER
function toggleNotificationDrawer() {
    const drawer = document.getElementById('notificationDrawer');
    const overlay = document.getElementById('drawerOverlay');
    drawer.classList.toggle('active');
    overlay.classList.toggle('active');

    if (drawer.classList.contains('active')) {
        renderNotificationList();
    }
}

function updateNotificationBadge() {
    const countEl = document.getElementById('notificationCount');
    if (countEl) countEl.innerText = HilltopData.announcements.length;
}

function renderNotificationList() {
    const list = document.getElementById('notificationList');
    if (!list) return;

    let html = '';
    HilltopData.announcements.forEach(a => {
        html += `
            <div class="notification-item" onclick="showToast('${a.message}')">
                <div class="notification-title"><i class="fas fa-bullhorn text-primary"></i> ${a.title}</div>
                <div class="notification-msg">${a.message}</div>
                <div class="notification-time">${a.date} &bull; Audience: ${a.audience}</div>
            </div>
        `;
    });
    list.innerHTML = html;
}

// TOAST HELPER
function showToast(message) {
    const container = document.getElementById('toastContainer');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.innerHTML = `<i class="fas fa-info-circle text-primary"></i> <span>${message}</span>`;
    container.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, 4000);
}
