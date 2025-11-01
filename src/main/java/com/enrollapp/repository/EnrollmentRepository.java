package com.enrollapp.repository;

import com.enrollapp.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    Optional<Enrollment> findByStudentAndCourseAndSemester(Student student, Course course, Semester semester);
    Optional<Enrollment> findByStudentAndCourseAndEnrollmentStatus(Student student, Course course, EnrollmentStatus status);
    List<Enrollment> findByStudentAndEnrollmentStatus(Student student, EnrollmentStatus status);

    boolean existsByStudent(Student student);
    void deleteByStudent(Student student);

    boolean existsByCourse(Course course);
    void deleteByCourse(Course course);

    boolean existsBySemester(Semester semester);
    void deleteBySemester(Semester semester);
}
