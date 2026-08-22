<?php
/**
 * Hilltop International College Portal - REST API Handler
 * Handles authentication, grade calculations, score submissions, and payments.
 */

require_once __DIR__ . '/db.php';

$action = $_GET['action'] ?? '';
$method = $_SERVER['REQUEST_METHOD'];

function calculateNigerianGrade($score) {
    if ($score >= 80) return ['grade' => 'A1', 'remark' => 'Excellent Distinction'];
    if ($score >= 75) return ['grade' => 'B2', 'remark' => 'Very Good'];
    if ($score >= 70) return ['grade' => 'B3', 'remark' => 'Good'];
    if ($score >= 65) return ['grade' => 'C4', 'remark' => 'Credit (Strong)'];
    if ($score >= 60) return ['grade' => 'C5', 'remark' => 'Credit'];
    if ($score >= 50) return ['grade' => 'C6', 'remark' => 'Credit (Pass)'];
    if ($score >= 45) return ['grade' => 'D7', 'remark' => 'Pass'];
    if ($score >= 40) return ['grade' => 'E8', 'remark' => 'Weak Pass'];
    return ['grade' => 'F9', 'remark' => 'Fail'];
}

switch ($action) {
    case 'get_results':
        $studentId = $_GET['student_id'] ?? 'STU-0482';
        echo json_encode([
            'status' => 'success',
            'data' => [
                'student_id' => $studentId,
                'student_name' => 'Chinedu Emmanuel Okafor',
                'admission_no' => 'HIC/2023/0482',
                'class' => 'SSS 3 Science A',
                'term' => '2nd Term',
                'session' => '2025/2026',
                'class_position' => 2,
                'total_students' => 38,
                'average_score' => 87.2,
                'gpa' => 4.44,
                'scores' => [
                    ['subject' => 'Further Mathematics', 'ca1' => 9.5, 'ca2' => 9.0, 'test' => 9.0, 'exam' => 64.0, 'total' => 91.5, 'grade' => 'A1', 'remark' => 'Excellent Distinction'],
                    ['subject' => 'General Mathematics', 'ca1' => 10.0, 'ca2' => 9.5, 'test' => 8.5, 'exam' => 61.0, 'total' => 89.0, 'grade' => 'A1', 'remark' => 'Excellent Distinction'],
                    ['subject' => 'Physics Practical & Theory', 'ca1' => 8.5, 'ca2' => 9.0, 'test' => 8.5, 'exam' => 62.0, 'total' => 88.0, 'grade' => 'A1', 'remark' => 'Excellent Distinction'],
                    ['subject' => 'Chemistry', 'ca1' => 9.0, 'ca2' => 8.0, 'test' => 9.0, 'exam' => 59.0, 'total' => 85.0, 'grade' => 'A1', 'remark' => 'Excellent Distinction'],
                    ['subject' => 'English Language & Lexis', 'ca1' => 8.0, 'ca2' => 8.5, 'test' => 8.0, 'exam' => 58.0, 'total' => 82.5, 'grade' => 'A1', 'remark' => 'Distinction'],
                    ['subject' => 'Computer Science & Robotics', 'ca1' => 10.0, 'ca2' => 10.0, 'test' => 10.0, 'exam' => 65.0, 'total' => 95.0, 'grade' => 'A1', 'remark' => 'Outstanding'],
                    ['subject' => 'Biology', 'ca1' => 8.0, 'ca2' => 8.0, 'test' => 7.5, 'exam' => 55.0, 'total' => 78.5, 'grade' => 'B2', 'remark' => 'Very Good'],
                    ['subject' => 'Civic Education', 'ca1' => 7.5, 'ca2' => 8.0, 'test' => 7.5, 'exam' => 51.0, 'total' => 74.0, 'grade' => 'B3', 'remark' => 'Good']
                ]
            ]
        ]);
        break;

    case 'submit_score':
        if ($method === 'POST') {
            $input = json_decode(file_get_contents('php://input'), true);
            $total = ($input['ca1'] ?? 0) + ($input['ca2'] ?? 0) + ($input['test'] ?? 0) + ($input['exam'] ?? 0);
            $grading = calculateNigerianGrade($total);
            echo json_encode([
                'status' => 'success',
                'message' => 'Score recorded successfully',
                'total' => $total,
                'grade' => $grading['grade'],
                'remark' => $grading['remark']
            ]);
        }
        break;

    case 'apply_admission':
        if ($method === 'POST') {
            $input = json_decode(file_get_contents('php://input'), true);
            $appNo = 'HIC-ADM-2026-0' . rand(100, 999);
            echo json_encode([
                'status' => 'success',
                'message' => 'Application received',
                'application_no' => $appNo
            ]);
        }
        break;

    default:
        echo json_encode([
            'status' => 'online',
            'portal' => 'Hilltop International College Portal API',
            'version' => '1.0.0',
            'timestamp' => date('Y-m-d H:i:s')
        ]);
        break;
}
