-- ==========================================================
-- HILLTOP INTERNATIONAL COLLEGE PORTAL - DATABASE SCHEMA
-- Target Database: MySQL 8.0+ / MariaDB 10.5+
-- Nigerian Junior & Senior Secondary School Management System
-- ==========================================================

CREATE DATABASE IF NOT EXISTS `hilltop_college_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `hilltop_college_db`;

-- 1. USERS & AUTHENTICATION TABLE
CREATE TABLE IF NOT EXISTS `users` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(100) NOT NULL UNIQUE,
    `email` VARCHAR(150) NOT NULL UNIQUE,
    `password_hash` VARCHAR(255) NOT NULL,
    `role` ENUM('admin', 'teacher', 'student', 'parent') NOT NULL,
    `full_name` VARCHAR(150) NOT NULL,
    `phone` VARCHAR(25),
    `status` ENUM('active', 'suspended', 'pending') DEFAULT 'active',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. ACADEMIC SESSIONS & TERMS
CREATE TABLE IF NOT EXISTS `academic_sessions` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `session_name` VARCHAR(50) NOT NULL, -- e.g. "2025/2026"
    `current_term` ENUM('1st Term', '2nd Term', '3rd Term') NOT NULL,
    `is_active` BOOLEAN DEFAULT TRUE,
    `term_start_date` DATE,
    `term_end_date` DATE
) ENGINE=InnoDB;

-- 3. CLASSES (Junior & Senior Secondary)
CREATE TABLE IF NOT EXISTS `classes` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `class_name` VARCHAR(50) NOT NULL, -- e.g. "SSS 3 Science A", "JSS 1 Blue"
    `division` ENUM('Junior Secondary', 'Senior Secondary') NOT NULL,
    `arm` VARCHAR(10) DEFAULT 'A',
    `class_teacher_id` INT,
    `capacity` INT DEFAULT 40,
    FOREIGN KEY (`class_teacher_id`) REFERENCES `users`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 4. SUBJECTS
CREATE TABLE IF NOT EXISTS `subjects` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `subject_code` VARCHAR(20) NOT NULL UNIQUE,
    `subject_name` VARCHAR(100) NOT NULL,
    `category` ENUM('Core', 'Science', 'Arts', 'Commercial', 'General') NOT NULL
) ENGINE=InnoDB;

-- 5. STUDENTS
CREATE TABLE IF NOT EXISTS `students` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT UNIQUE,
    `admission_no` VARCHAR(50) NOT NULL UNIQUE,
    `class_id` INT NOT NULL,
    `parent_id` INT,
    `date_of_birth` DATE,
    `gender` ENUM('Male', 'Female') NOT NULL,
    `state_of_origin` VARCHAR(50),
    `house` VARCHAR(50) DEFAULT 'Nelson Mandela Hall',
    `is_boarder` BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`class_id`) REFERENCES `classes`(`id`) ON DELETE RESTRICT,
    FOREIGN KEY (`parent_id`) REFERENCES `users`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 6. CONTINUOUS ASSESSMENT & TERMINAL RESULTS
CREATE TABLE IF NOT EXISTS `student_results` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `student_id` INT NOT NULL,
    `subject_id` INT NOT NULL,
    `session_id` INT NOT NULL,
    `term` ENUM('1st Term', '2nd Term', '3rd Term') NOT NULL,
    `ca1_score` DECIMAL(5,2) DEFAULT 0.00, -- Max 10%
    `ca2_score` DECIMAL(5,2) DEFAULT 0.00, -- Max 10%
    `test_score` DECIMAL(5,2) DEFAULT 0.00, -- Max 10%
    `exam_score` DECIMAL(5,2) DEFAULT 0.00, -- Max 70%
    `total_score` DECIMAL(5,2) GENERATED ALWAYS AS (`ca1_score` + `ca2_score` + `test_score` + `exam_score`) STORED,
    `grade` VARCHAR(5), -- A1, B2, B3, C4, C5, C6, D7, E8, F9
    `teacher_comment` VARCHAR(255),
    UNIQUE KEY `unique_student_term_subject` (`student_id`, `subject_id`, `session_id`, `term`),
    FOREIGN KEY (`student_id`) REFERENCES `students`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`subject_id`) REFERENCES `subjects`(`id`) ON DELETE RESTRICT,
    FOREIGN KEY (`session_id`) REFERENCES `academic_sessions`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 7. FEE INVOICES & LEVIES
CREATE TABLE IF NOT EXISTS `fee_invoices` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `invoice_number` VARCHAR(50) NOT NULL UNIQUE,
    `student_id` INT NOT NULL,
    `session_id` INT NOT NULL,
    `term` VARCHAR(20) NOT NULL,
    `title` VARCHAR(150) NOT NULL,
    `total_amount` DECIMAL(12,2) NOT NULL,
    `amount_paid` DECIMAL(12,2) DEFAULT 0.00,
    `balance_due` DECIMAL(12,2) GENERATED ALWAYS AS (`total_amount` - `amount_paid`) STORED,
    `status` ENUM('UNPAID', 'PARTIAL', 'PAID') DEFAULT 'UNPAID',
    `due_date` DATE,
    FOREIGN KEY (`student_id`) REFERENCES `students`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 8. PAYMENT TRANSACTIONS (Paystack / Flutterwave)
CREATE TABLE IF NOT EXISTS `payment_transactions` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `transaction_ref` VARCHAR(100) NOT NULL UNIQUE,
    `invoice_id` INT NOT NULL,
    `amount_paid` DECIMAL(12,2) NOT NULL,
    `payment_gateway` ENUM('Paystack', 'Flutterwave', 'Direct Bank Transfer') NOT NULL,
    `channel` VARCHAR(50) DEFAULT 'card',
    `payer_name` VARCHAR(150) NOT NULL,
    `payer_email` VARCHAR(150),
    `status` ENUM('success', 'failed', 'pending') DEFAULT 'success',
    `payment_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`invoice_id`) REFERENCES `fee_invoices`(`id`) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 9. CBT EXAMINATIONS
CREATE TABLE IF NOT EXISTS `cbt_exams` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `subject_id` INT NOT NULL,
    `target_class_id` INT NOT NULL,
    `duration_minutes` INT NOT NULL DEFAULT 30,
    `pass_mark_percentage` INT DEFAULT 50,
    `is_active` BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (`subject_id`) REFERENCES `subjects`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`target_class_id`) REFERENCES `classes`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 10. CBT QUESTIONS & ANSWERS
CREATE TABLE IF NOT EXISTS `cbt_questions` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `exam_id` INT NOT NULL,
    `question_text` TEXT NOT NULL,
    `option_a` TEXT NOT NULL,
    `option_b` TEXT NOT NULL,
    `option_c` TEXT NOT NULL,
    `option_d` TEXT NOT NULL,
    `correct_option` ENUM('A', 'B', 'C', 'D') NOT NULL,
    `explanation` TEXT,
    FOREIGN KEY (`exam_id`) REFERENCES `cbt_exams`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 11. CBT STUDENT SUBMISSIONS
CREATE TABLE IF NOT EXISTS `cbt_submissions` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `exam_id` INT NOT NULL,
    `student_id` INT NOT NULL,
    `score` INT NOT NULL,
    `total_questions` INT NOT NULL,
    `percentage` DECIMAL(5,2) NOT NULL,
    `passed` BOOLEAN NOT NULL,
    `completed_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`exam_id`) REFERENCES `cbt_exams`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`student_id`) REFERENCES `students`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 12. ONLINE ADMISSIONS
CREATE TABLE IF NOT EXISTS `admissions` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `application_no` VARCHAR(50) NOT NULL UNIQUE,
    `applicant_name` VARCHAR(150) NOT NULL,
    `gender` ENUM('Male', 'Female') NOT NULL,
    `date_of_birth` DATE NOT NULL,
    `entry_class` VARCHAR(50) NOT NULL,
    `parent_name` VARCHAR(150) NOT NULL,
    `parent_phone` VARCHAR(30) NOT NULL,
    `parent_email` VARCHAR(150),
    `previous_school` VARCHAR(150),
    `status` ENUM('Submitted', 'Shortlisted for CBT', 'Offered Admission', 'Rejected') DEFAULT 'Submitted',
    `cbt_score` INT DEFAULT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
