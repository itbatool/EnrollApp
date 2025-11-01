package com.enrollapp.View;

import com.enrollapp.model.*;
import com.enrollapp.service.FacultyService;
import com.enrollapp.service.MajorService;
import com.enrollapp.service.NationalityService;
import com.enrollapp.service.StudentService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Named("studentBean")
@ViewScoped
@Getter
@Setter
public class StudentBean implements Serializable, Converter<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Student student = new Student();

    @Inject
    private StudentService studentService;

    @Inject
    private FacultyService facultyService;

    @Inject
    private NationalityService nationalityService;

    @Inject
    private MajorService majorService;

    @Inject
    private FacesContext facesContext;

    public List<Student> getAllStudents() {
        try {
            return studentService.findAll();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load students: " + e.getMessage()));
            return List.of();
        }
    }

    public List<Faculty> getFaculties() {
        try {
            return facultyService.findAll();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load faculties: " + e.getMessage()));
            return List.of();
        }
    }

    public List<Major> getMajors() {
        try {
            return majorService.findAll();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load majors: " + e.getMessage()));
            return List.of();
        }
    }

    public List<Nationality> getNationalities() {
        try {
            return nationalityService.findAll();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load nationalities: " + e.getMessage()));
            return List.of();
        }
    }

    public void saveStudent() {
        try {
            if (student.getId() == null) {
                studentService.createStudent(student);
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Student added successfully"));
            } else {
                studentService.updateStudent(student);
                facesContext.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Student updated successfully"));
            }
            student = new Student();
        } catch (IllegalStateException e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "An unexpected error occurred: " + e.getMessage()));
        }
    }

    public void editStudent(Student student) {
        this.student = Student.builder()
                .id(student.getId())
                .name(student.getName())
                .nationalId(student.getNationalId())
                .email(student.getEmail())
                .birthDate(student.getBirthDate())
                .gender(student.getGender())
                .mobileNo(student.getMobileNo())
                .schoolAvg(student.getSchoolAvg())
                .nationality(student.getNationality())
                .faculty(student.getFaculty())
                .major(student.getMajor())
                .build();
    }

    public void deleteStudent(Student student) {
        try {
            studentService.deleteStudent(student);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Student deleted successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete student: " + e.getMessage()));
        }
    }

    public void clear() {
        student = new Student();
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

    public Converter getMajorConverter() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value == null || value.isEmpty()) return null;
                return getMajors().stream()
                        .filter(m -> String.valueOf(m.getId()).equals(value))
                        .findFirst().orElse(null);
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                return value != null ? String.valueOf(((Major) value).getId()) : "";
            }
        };
    }

    public Converter getNationalityConverter() {
        return new Converter() {
            @Override
            public Object getAsObject(FacesContext context, UIComponent component, String value) {
                if (value == null || value.isEmpty()) return null;
                return getNationalities().stream()
                        .filter(n -> String.valueOf(n.getId()).equals(value))
                        .findFirst().orElse(null);
            }

            @Override
            public String getAsString(FacesContext context, UIComponent component, Object value) {
                return value != null ? String.valueOf(((Nationality) value).getId()) : "";
            }
        };
    }

    @Override
    public LocalDate getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return LocalDate.parse(value, FORMATTER);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDate value) {
        if (value == null) {
            return "";
        }
        return value.format(FORMATTER);
    }
}
