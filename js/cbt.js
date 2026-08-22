/**
 * Hilltop International College Portal - CBT Exam Engine
 * Handles full-screen Computer Based Tests, countdown timer, question palette, and instant grading.
 */

window.CbtEngine = {
    activeExam: null,
    currentQuestionIndex: 0,
    answersMap: {}, // questionId -> optionIndex
    timerInterval: null,
    remainingSeconds: 0,

    startExam: function(examId) {
        const exam = HilltopData.cbtExams.find(e => e.id === examId);
        if (!exam) return;

        this.activeExam = exam;
        this.currentQuestionIndex = 0;
        this.answersMap = {};
        this.remainingSeconds = exam.durationMinutes * 60;

        // Render CBT Exam Runner View in Main Content
        this.renderExamView();
        this.startTimer();
    },

    startTimer: function() {
        if (this.timerInterval) clearInterval(this.timerInterval);

        this.timerInterval = setInterval(() => {
            this.remainingSeconds--;
            const timerDisplay = document.getElementById('cbtTimerDisplay');
            if (timerDisplay) {
                const mins = Math.floor(this.remainingSeconds / 60);
                const secs = this.remainingSeconds % 60;
                timerDisplay.innerText = `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
            }

            if (this.remainingSeconds <= 0) {
                clearInterval(this.timerInterval);
                this.submitExam();
            }
        }, 1000);
    },

    selectAnswer: function(qId, optionIdx) {
        this.answersMap[qId] = optionIdx;
        this.renderExamView();
    },

    jumpToQuestion: function(index) {
        this.currentQuestionIndex = index;
        this.renderExamView();
    },

    nextQuestion: function() {
        if (this.currentQuestionIndex < this.activeExam.questions.length - 1) {
            this.currentQuestionIndex++;
            this.renderExamView();
        }
    },

    prevQuestion: function() {
        if (this.currentQuestionIndex > 0) {
            this.currentQuestionIndex--;
            this.renderExamView();
        }
    },

    submitExam: function() {
        if (this.timerInterval) clearInterval(this.timerInterval);

        let correctCount = 0;
        this.activeExam.questions.forEach(q => {
            const chosen = this.answersMap[q.id];
            if (chosen !== undefined && chosen === q.correctIndex) {
                correctCount++;
            }
        });

        const total = this.activeExam.questions.length;
        const percentage = (correctCount / total) * 100;
        const passed = percentage >= this.activeExam.passMarkPercentage;

        // Record in results history
        HilltopData.cbtResults.unshift({
            examId: this.activeExam.id,
            examTitle: this.activeExam.title,
            score: correctCount,
            totalQuestions: total,
            percentage: percentage,
            passed: passed,
            completedAt: "Just now",
            answersMap: { ...this.answersMap }
        });

        this.renderResultsView(correctCount, total, percentage, passed);
    },

    renderExamView: function() {
        const container = document.getElementById('appContent');
        if (!container || !this.activeExam) return;

        const exam = this.activeExam;
        const q = exam.questions[this.currentQuestionIndex];
        const mins = Math.floor(this.remainingSeconds / 60);
        const secs = this.remainingSeconds % 60;

        let paletteHtml = '';
        exam.questions.forEach((question, idx) => {
            const isAnswered = this.answersMap[question.id] !== undefined;
            const isCurrent = this.currentQuestionIndex === idx;
            const statusClass = isCurrent ? 'current' : (isAnswered ? 'answered' : '');
            paletteHtml += `<button class="palette-btn ${statusClass}" onclick="CbtEngine.jumpToQuestion(${idx})">${idx + 1}</button>`;
        });

        let optionsHtml = '';
        q.options.forEach((optText, optIdx) => {
            const isSelected = this.answersMap[q.id] === optIdx;
            optionsHtml += `
                <div class="cbt-option ${isSelected ? 'selected' : ''}" onclick="CbtEngine.selectAnswer(${q.id}, ${optIdx})">
                    <input type="radio" name="opt_${q.id}" ${isSelected ? 'checked' : ''}>
                    <span>${optText}</span>
                </div>
            `;
        });

        container.innerHTML = `
            <div class="portal-container">
                <div class="cbt-wrapper">
                    <div class="cbt-header-bar">
                        <div>
                            <h3 style="font-size:1.1rem; font-weight:800;">${exam.title}</h3>
                            <p style="font-size:0.8rem; opacity:0.9;">Question ${this.currentQuestionIndex + 1} of ${exam.questions.length} &bull; ${exam.subject}</p>
                        </div>
                        <div class="cbt-timer">
                            <i class="fas fa-stopwatch"></i>
                            <span id="cbtTimerDisplay">${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}</span>
                        </div>
                    </div>

                    <!-- Question Navigation Palette -->
                    <div class="question-palette">
                        ${paletteHtml}
                    </div>

                    <!-- Active Question Box -->
                    <div class="question-box">
                        <div class="question-num">QUESTION ${this.currentQuestionIndex + 1}</div>
                        <div class="question-text">${q.question}</div>
                    </div>

                    <!-- Options List -->
                    <div class="cbt-options-list">
                        ${optionsHtml}
                    </div>

                    <!-- CBT Action Controls -->
                    <div style="display:flex; justify-content:space-between; align-items:center; flex-wrap:wrap; gap:10px;">
                        <div>
                            <button class="btn btn-outline" onclick="CbtEngine.prevQuestion()" ${this.currentQuestionIndex === 0 ? 'disabled' : ''}>
                                <i class="fas fa-chevron-left"></i> Previous
                            </button>
                            <button class="btn btn-primary" onclick="CbtEngine.nextQuestion()" ${this.currentQuestionIndex === exam.questions.length - 1 ? 'disabled' : ''}>
                                Next <i class="fas fa-chevron-right"></i>
                            </button>
                        </div>
                        <button class="btn" style="background:#2E7D32; color:#fff;" onclick="CbtEngine.submitExam()">
                            <i class="fas fa-check-circle"></i> Submit Test Assessment
                        </button>
                    </div>
                </div>
            </div>
        `;
    },

    renderResultsView: function(score, total, percentage, passed) {
        const container = document.getElementById('appContent');
        if (!container || !this.activeExam) return;

        const exam = this.activeExam;

        let reviewHtml = '';
        exam.questions.forEach((q, idx) => {
            const chosen = this.answersMap[q.id];
            const isCorrect = chosen === q.correctIndex;
            reviewHtml += `
                <div class="feature-card mb-2" style="border-left: 4px solid ${isCorrect ? 'var(--success)' : 'var(--danger)'};">
                    <p style="font-weight:700; font-size:0.9rem;">Q${idx + 1}: ${q.question}</p>
                    <p style="font-size:0.82rem; margin: 4px 0;">
                        <strong>Your Answer:</strong> <span style="color:${isCorrect ? 'var(--success)' : 'var(--danger)'}">${chosen !== undefined ? q.options[chosen] : 'Unanswered'}</span>
                    </p>
                    <p style="font-size:0.82rem; color:var(--success); font-weight:700;">
                        <strong>Correct Answer:</strong> ${q.options[q.correctIndex]}
                    </p>
                    <p style="font-size:0.78rem; color:var(--text-muted); margin-top:4px;">
                        <em>Explanation: ${q.explanation}</em>
                    </p>
                </div>
            `;
        });

        container.innerHTML = `
            <div class="portal-container">
                <div class="cbt-wrapper text-center">
                    <div style="font-size:3.5rem; color:${passed ? 'var(--success)' : 'var(--danger)'}; margin-bottom:12px;">
                        <i class="fas ${passed ? 'fa-award' : 'fa-times-circle'}"></i>
                    </div>
                    <h2 style="font-family:'Outfit',sans-serif; font-size:1.8rem; font-weight:800; color:${passed ? 'var(--success)' : 'var(--danger)'}">
                        ${passed ? 'Congratulations! You Passed!' : 'Assessment Completed'}
                    </h2>
                    <p style="font-size:0.95rem; color:var(--text-muted);">${exam.title}</p>

                    <div style="background:var(--bg-main); border-radius:var(--radius-lg); padding:20px; max-width:400px; margin:20px auto;">
                        <div style="font-size:2.4rem; font-weight:900; color:var(--primary); font-family:'Outfit',sans-serif;">${score} / ${total}</div>
                        <div style="font-size:1rem; font-weight:700; color:${passed ? 'var(--success)' : 'var(--danger)'};">
                            Score: ${percentage.toFixed(1)}% (Pass mark: ${exam.passMarkPercentage}%)
                        </div>
                    </div>

                    <h4 style="text-align:left; font-size:1.1rem; font-weight:800; margin:24px 0 12px 0;">Question Review & Explanations</h4>
                    <div style="text-align:left;">
                        ${reviewHtml}
                    </div>

                    <button class="btn btn-primary mt-4" onclick="switchPortal('student')">
                        <i class="fas fa-arrow-left"></i> Return to Student Portal
                    </button>
                </div>
            </div>
        `;
    }
};
