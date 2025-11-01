package com.enrollapp.service;

import com.enrollapp.model.*;
import com.enrollapp.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final CourseService courseService;
    private final SemesterService semesterService;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentService studentService,
                             CourseService courseService,
                             SemesterService semesterService) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentService = studentService;
        this.courseService = courseService;
        this.semesterService = semesterService;
    }

    public String createEnrollment(Long studentId, Long courseId, Long semesterId) {
        Student student = studentService.findStudentById(studentId)
                .orElseThrow(() -> new IllegalStateException("Student not found"));

        Course course = courseService.findCourseById(courseId)
                .orElseThrow(() -> new IllegalStateException("Course not found"));

        Semester semester = semesterService.findSemesterById(semesterId)
                .orElseThrow(() -> new IllegalStateException("Semester not found"));

        enrollmentRepository.findByStudentAndCourseAndSemester(student, course, semester).ifPresent(e -> {
            // If status is ENROLLED or COMPLETED student can't enroll
            EnrollmentStatus status = e.getEnrollmentStatus();
            if (status == EnrollmentStatus.ENROLLED) {
                throw new IllegalStateException("Student is already enrolled in this course");
            } else if (status == EnrollmentStatus.COMPLETED) {
                throw new IllegalStateException("Student has already completed this course");
            }
        });

        if (!semester.getIsActive()) {
            throw new IllegalStateException("Semester is not active for enrollment.");
        }

        if (!course.getIsActive()) {
            throw new IllegalStateException("Course is not active for enrollment.");
        }

        checkCourseCapacity(course);
        checkPrerequisites(student, course);

        Enrollment enrollment = Enrollment.builder()
                .enrollmentDate(LocalDate.now())
                .enrollmentStatus(EnrollmentStatus.ENROLLED)
                .student(student)
                .course(course)
                .semester(semester)
                .build();

        enrollmentRepository.save(enrollment);
        courseService.updateCourseEnrollmentCount(course);

        return "Enrolled Successfully";
    }

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public void updateEnrollmentStatus(Long enrollmentId, EnrollmentStatus status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalStateException("Enrollment not found for id: " + enrollmentId));

        EnrollmentStatus previousStatus = enrollment.getEnrollmentStatus();
        enrollment.setEnrollmentStatus(status);

        Course course = enrollment.getCourse();
        int current = course.getCurrentEnrollment() != null ? course.getCurrentEnrollment() : 0;

        if (status == EnrollmentStatus.DROPPED && previousStatus != EnrollmentStatus.DROPPED) {
            course.setCurrentEnrollment(Math.max(0, current - 1));
            courseService.updateCourse(course);
        }

        enrollmentRepository.save(enrollment);
    }

    // Exercise 2: Prerequisite Checking
    public void checkPrerequisites(Student student, Course course) {
        Set<Course> prerequisites = course.getPrerequisites();
        if (prerequisites == null || prerequisites.isEmpty()) {
            return;
        }

        List<String> missingCourses = new ArrayList<>();
        for (Course prerequisite : prerequisites) {
            if (enrollmentRepository.findByStudentAndCourseAndEnrollmentStatus(
                    student, prerequisite, EnrollmentStatus.COMPLETED).isEmpty()) {
                missingCourses.add(prerequisite.getName());
            }
        }

        if (!missingCourses.isEmpty()) {
            throw new IllegalStateException("Missing prerequisites: " + String.join(", ", missingCourses));
        }
    }

    // Exercise 3: Capacity checking
    public void checkCourseCapacity(Course course) {
        if (course.getMaxCapacity() == null || course.getMaxCapacity() <= 0) {
            throw new IllegalStateException("Course max capacity must be set and greater than 0");
        }

        if (course.getCurrentEnrollment() >= course.getMaxCapacity()) {
            throw new IllegalStateException("Cannot enroll: Course is full");
        }
    }

    // Exercise 4: GPA Calculation
    public double calculateGPA(Student student) {
        List<Enrollment> completedEnrollments = enrollmentRepository
                .findByStudentAndEnrollmentStatus(student, EnrollmentStatus.COMPLETED);

        if (completedEnrollments.isEmpty()) return 0.0;

        double totalPoints = 0.0;
        int totalCredits = 0;

        for (Enrollment e : completedEnrollments) {
            int credits = e.getCourse().getCreditHours();
            totalPoints += gradeToPoint(e.getGrade()) * credits;
            totalCredits += credits;
        }

        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    private double gradeToPoint(double grade) {
        if (grade >= 90) return 4.0;
        if (grade >= 80) return 3.0;
        if (grade >= 70) return 2.0;
        if (grade >= 60) return 1.0;
        return 0.0;
    }

    public void assignGrade(Long enrollmentId, Double grade) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));

        if (grade == null || grade < 0 || grade > 100) {
            throw new IllegalArgumentException("Grade must be between 0 and 100");
        }

        enrollment.setGrade(grade);
        enrollment.setEnrollmentStatus(grade >= 50 ? EnrollmentStatus.COMPLETED : EnrollmentStatus.FAILED);
        enrollmentRepository.save(enrollment);
    }
}