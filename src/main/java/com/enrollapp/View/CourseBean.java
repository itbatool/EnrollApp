package com.enrollapp.View;

import com.enrollapp.model.Course;
import com.enrollapp.model.Faculty;
import com.enrollapp.service.CourseService;
import com.enrollapp.service.FacultyService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Named("courseBean")
@ViewScoped
@Getter
@Setter
public class CourseBean implements Serializable {

    private Course course = new Course();
    private List<Long> selectedPrerequisiteIds = new ArrayList<>();

    @Inject
    private FacesContext facesContext;

    @Inject
    private CourseService courseService;

    @Inject
    private FacultyService facultyService;

    public void addCourse() {
        try {
            Set<Course> prerequisites = new HashSet<>();
            for (Long id : selectedPrerequisiteIds) {
                courseService.findCourseById(id).ifPresent(prerequisites::add);
            }
            course.setPrerequisites(prerequisites);
            courseService.createCourse(course);

            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Course added successfully"));

            course = new Course();
            selectedPrerequisiteIds.clear();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add course: " + e.getMessage()));
        }
    }

    public List<Course> getAllCourses() {
        try {
            return courseService.findAll();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load courses: " + e.getMessage()));
            return new ArrayList<>();
        }
    }

    public void deleteCourse(Course course) {
        try {
            courseService.deleteCourse(course);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Course deleted successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete course: " + e.getMessage()));
        }
    }

    public List<Faculty> getFaculties() {
        try {
            return facultyService.findAll();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load faculties: " + e.getMessage()));
            return new ArrayList<>();
        }
    }

    public void clear() {
        course = new Course();
        selectedPrerequisiteIds.clear();
    }

    public Converter getFacultyConverter() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value == null || value.isEmpty()) return null;
                return getFaculties().stream()
                        .filter(f -> String.valueOf(f.getId()).equals(value))
                        .findFirst().orElse(null);
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                return value != null ? String.valueOf(((Faculty) value).getId()) : "";
            }
        };
    }

    public void loadCourseForEdit(Course course) {
        this.course = Course.builder()
                .id(course.getId())
                .name(course.getName())
                .creditHours(course.getCreditHours())
                .isActive(course.getIsActive())
                .description(course.getDescription())
                .faculty(course.getFaculty())
                .maxCapacity(course.getMaxCapacity())
                .currentEnrollment(course.getCurrentEnrollment())
                .prerequisites(course.getPrerequisites())
                .build();

        selectedPrerequisiteIds = course.getPrerequisites() != null
                ? course.getPrerequisites().stream().map(Course::getId).collect(Collectors.toList())
                : new ArrayList<>();
    }

    public void updateCourse() {
        try {
            Set<Course> prerequisites = new HashSet<>();
            for (Long id : selectedPrerequisiteIds) {
                courseService.findCourseById(id).ifPresent(prerequisites::add);
            }
            course.setPrerequisites(prerequisites);
            courseService.updateCourse(course);

            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Course updated successfully"));

            course = new Course();
            selectedPrerequisiteIds.clear();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update course: " + e.getMessage()));
        }
    }
}
