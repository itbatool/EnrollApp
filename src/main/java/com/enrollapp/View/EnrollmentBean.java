package com.enrollapp.View;

import com.enrollapp.model.*;
import com.enrollapp.service.CourseService;
import com.enrollapp.service.EnrollmentService;
import com.enrollapp.service.SemesterService;
import com.enrollapp.service.StudentService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named("enrollmentBean")
@ViewScoped
@Getter
@Setter
public class EnrollmentBean implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long studentId;
    private Long courseId;
    private Long semesterId;

    private Map<Long, BigDecimal> gradeInputs = new HashMap<>();
    private Long selectedStudentId;
    private Double calculatedGPA;

    @Inject
    private EnrollmentService enrollmentService;

    @Inject
    private StudentService studentService;

    @Inject
    private CourseService courseService;

    @Inject
    private SemesterService semesterService;

    @Inject
    private FacesContext facesContext;

    public void enrollStudent() {
        try {
            String result = enrollmentService.createEnrollment(studentId, courseId, semesterId);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", result));
            clear();
        } catch (IllegalStateException e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "An unexpected error occurred: " + e.getMessage()));
        }
    }

    public List<Student> getAllStudents() {
        try {
            return studentService.findAll();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load students: " + e.getMessage()));
            return List.of();
        }
    }

    public List<Enrollment> getAllEnrollments() {
        try {
            return enrollmentService.findAll();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load enrollments: " + e.getMessage()));
            return List.of();
        }
    }

    public List<Enrollment> getActiveEnrollments() {
        return getAllEnrollments().stream()
                .filter(e -> e.getEnrollmentStatus() == EnrollmentStatus.ENROLLED)
                .collect(Collectors.toList());
    }

    public List<Enrollment> getCompletedEnrollments() {
        return getAllEnrollments().stream()
                .filter(e -> e.getEnrollmentStatus() == EnrollmentStatus.COMPLETED)
                .collect(Collectors.toList());
    }

    public void completeEnrollment(Long enrollmentId) {
        try {
            BigDecimal gradeDecimal = gradeInputs.get(enrollmentId);
            if (gradeDecimal == null) {
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please enter a grade"));
                return;
            }

            double grade = gradeDecimal.doubleValue();
            if (grade < 0 || grade > 100) {
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid grade"));
                return;
            }

            enrollmentService.assignGrade(enrollmentId, grade);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Enrollment COMPLETED successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void dropEnrollment(Enrollment enrollment) {
        try {
            enrollmentService.updateEnrollmentStatus(enrollment.getId(), EnrollmentStatus.DROPPED);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Enrollment dropped successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public List<Course> getAllCourses() {
        try {
            return courseService.findAll();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load courses: " + e.getMessage()));
            return List.of();
        }
    }

    public List<Semester> getAllSemesters() {
        try {
            return semesterService.findAll();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load semesters: " + e.getMessage()));
            return List.of();
        }
    }

    public EnrollmentStatus[] getEnrollmentStatuses() {
        return EnrollmentStatus.values();
    }

    public void calculateGPA() {
        try {
            if (selectedStudentId == null) {
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Please select a student."));
                return;
            }

            Student student = studentService.findStudentById(selectedStudentId)
                    .orElseThrow(() -> new IllegalStateException("Student not found"));

            calculatedGPA = enrollmentService.calculateGPA(student);

            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "GPA Calculated", "GPA for " + student.getName() + " is " + calculatedGPA));

        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void clear() {
        studentId = null;
        courseId = null;
        semesterId = null;
    }
}
