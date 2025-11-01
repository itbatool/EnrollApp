package com.enrollapp.service;

import com.enrollapp.model.Course;
import com.enrollapp.repository.CourseRepository;
import com.enrollapp.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public void createCourse(Course course) {
        courseRepository.save(course);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> findCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public void updateCourse(Course course) {
        Course existing = courseRepository.findById(course.getId())
                .orElseThrow(() -> new IllegalStateException("Course not found"));

        existing.setName(course.getName());
        existing.setCreditHours(course.getCreditHours());
        existing.setIsActive(course.getIsActive());
        existing.setDescription(course.getDescription());
        existing.setFaculty(course.getFaculty());
        existing.setMaxCapacity(course.getMaxCapacity());
        existing.setCurrentEnrollment(course.getCurrentEnrollment());
        existing.setPrerequisites(course.getPrerequisites());

        courseRepository.save(existing);
    }

    public void updateCourseEnrollmentCount(Course course) {
        if (course.getCurrentEnrollment() == null) {
            course.setCurrentEnrollment(0);
        }
        course.setCurrentEnrollment(course.getCurrentEnrollment() + 1);
        courseRepository.save(course);
    }

    public void deleteCourse(Course course) {
        if (!courseRepository.existsById(course.getId())) {
            throw new IllegalStateException("Course not found");
        }
        if (enrollmentRepository.existsByCourse(course)) {
            enrollmentRepository.deleteByCourse(course);
        }
        courseRepository.delete(course);
    }
}
