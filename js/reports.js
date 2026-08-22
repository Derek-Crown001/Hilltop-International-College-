/**
 * Hilltop International College Portal - Report Card Printing Module
 * Renders authentic Nigerian secondary school terminal report cards with CA (30%) + Exam (70%) breakdown,
 * WAEC / NECO grading key, affective and psychomotor domains, and principal's signature stamp.
 */

window.ReportCardEngine = {
    openReportCard: function(studentId) {
        const report = HilltopData.reportCards.find(r => r.studentId === studentId) || HilltopData.reportCards[0];
        if (!report) return;

        const container = document.getElementById('printableReportSheet');
        if (!container) return;

        let rowsHtml = '';
        report.scores.forEach(s => {
            rowsHtml += `
                <tr>
                    <td class="sub-name">${s.subjectName}</td>
                    <td>${s.ca1.toFixed(1)}</td>
                    <td>${s.ca2.toFixed(1)}</td>
                    <td>${s.testScore.toFixed(1)}</td>
                    <td><strong>${(s.ca1 + s.ca2 + s.testScore).toFixed(1)}</strong></td>
                    <td>${s.examScore.toFixed(1)}</td>
                    <td><strong>${s.totalScore.toFixed(1)}</strong></td>
                    <td><strong>${s.grade}</strong></td>
                    <td>${s.remark}</td>
                </tr>
            `;
        });

        let psychomotorHtml = '';
        report.psychomotor.forEach(p => {
            psychomotorHtml += `<tr><td>${p.skill}</td><td><strong>${p.rating}</strong></td></tr>`;
        });

        let affectiveHtml = '';
        report.affective.forEach(a => {
            affectiveHtml += `<tr><td>${a.trait}</td><td><strong>${a.rating}</strong></td></tr>`;
        });

        container.innerHTML = `
            <div class="report-crest-header">
                <div style="font-size:2.2rem; color:var(--primary);"><i class="fas fa-graduation-cap"></i></div>
                <h1 class="report-crest-title">HILLTOP INTERNATIONAL COLLEGE</h1>
                <p style="font-size:0.85rem; font-weight:700; color:var(--text-muted);">
                    14–18 Hilltop Crest Avenue, Lekki Phase 1, Lagos, Nigeria &bull; Tel: +234 803 456 7890
                </p>
                <h2 style="font-size:1.1rem; font-weight:800; text-decoration:underline; margin-top:8px;">
                    STUDENT'S CONTINUOUS ASSESSMENT & TERMINAL EXAMINATION REPORT SHEET
                </h2>
                <p style="font-size:0.85rem; font-weight:600;">
                    ${report.term.toUpperCase()} &bull; ${report.session} ACADEMIC SESSION
                </p>
            </div>

            <!-- Student Biodata Meta Grid -->
            <div class="report-meta-grid">
                <div><strong>STUDENT FULL NAME:</strong> ${report.studentName.toUpperCase()}</div>
                <div><strong>ADMISSION NO:</strong> ${report.admissionNo}</div>
                <div><strong>CLASS / STREAM:</strong> ${report.className}</div>
                <div><strong>POSITION IN CLASS:</strong> ${report.classPosition} of ${report.totalStudentsInClass} Students</div>
                <div><strong>TERM AVERAGE SCORE:</strong> ${report.averageScore.toFixed(1)}%</div>
                <div><strong>CUMULATIVE GPA:</strong> ${report.gpa.toFixed(2)} / 5.0 (Distinction)</div>
                <div><strong>ATTENDANCE:</strong> ${report.attendancePresent} / ${report.attendanceTotalDays} Days Present</div>
                <div><strong>NEXT TERM RESUMPTION:</strong> ${report.nextTermResumption}</div>
            </div>

            <!-- Subject Scores Table -->
            <table class="report-table">
                <thead>
                    <tr>
                        <th>SUBJECT</th>
                        <th>1st CA (10)</th>
                        <th>2nd CA (10)</th>
                        <th>TEST (10)</th>
                        <th>TOTAL CA (30)</th>
                        <th>EXAM (70)</th>
                        <th>TOTAL (100)</th>
                        <th>GRADE</th>
                        <th>REMARK</th>
                    </tr>
                </thead>
                <tbody>
                    ${rowsHtml}
                </tbody>
            </table>

            <!-- Psychomotor & Affective Domains Assessment -->
            <div style="display:grid; grid-template-columns:1fr 1fr; gap:16px; margin-bottom:16px;">
                <div>
                    <h4 style="font-size:0.85rem; font-weight:800; border-bottom:1px solid #000; padding-bottom:3px; margin-bottom:6px;">
                        AFFECTIVE TRAITS (RATING KEY: A-Excellent, B-Good, C-Fair)
                    </h4>
                    <table class="report-table">
                        <tbody>${affectiveHtml}</tbody>
                    </table>
                </div>
                <div>
                    <h4 style="font-size:0.85rem; font-weight:800; border-bottom:1px solid #000; padding-bottom:3px; margin-bottom:6px;">
                        PSYCHOMOTOR SKILLS
                    </h4>
                    <table class="report-table">
                        <tbody>${psychomotorHtml}</tbody>
                    </table>
                </div>
            </div>

            <!-- Comments & Remarks -->
            <div style="border:1px solid #000; padding:10px; font-size:0.82rem; margin-bottom:16px;">
                <p style="margin-bottom:6px;"><strong>Form Teacher's Remarks:</strong> "${report.formTeacherRemarks}"</p>
                <p style="margin-bottom:6px;"><strong>House Master's Remarks:</strong> "${report.houseMasterRemarks}"</p>
                <p><strong>Principal's Overall Remark:</strong> "${report.principalRemarks}"</p>
            </div>

            <!-- Signatures & Stamp -->
            <div class="report-signatures">
                <div class="sig-box">
                    <strong>Mr. Babatunde Adeyemi (B.Sc, PGDE)</strong><br>
                    <span style="font-size:0.75rem; color:#555;">Form Teacher & HOD Sciences</span>
                </div>
                <div class="sig-box">
                    <strong style="color:var(--primary);">Dr. Emmanuel Okafor (Ph.D, FSTAN)</strong><br>
                    <span style="font-size:0.75rem; color:#555;">Principal & Governing Board Seal [VERIFIED]</span>
                </div>
            </div>
        `;

        document.getElementById('reportCardModal').classList.add('active');
    },

    closeReportCard: function() {
        document.getElementById('reportCardModal').classList.remove('active');
    }
};

window.openReportCardModal = function(studentId) {
    ReportCardEngine.openReportCard(studentId);
};

window.closeReportCardModal = function() {
    ReportCardEngine.closeReportCard();
};
