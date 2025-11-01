package com.enrollapp.View;

import com.enrollapp.model.Semester;
import com.enrollapp.model.SemesterType;
import com.enrollapp.service.SemesterService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.convert.Converter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Named("semesterBean")
@ViewScoped
@Getter
@Setter
public class SemesterBean implements Serializable, Converter<LocalDate> {

    private Semester semester = new Semester();
    private SemesterType[] semesterTypes = SemesterType.values();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Inject
    private FacesContext facesContext;

    @Inject
    private SemesterService semesterService;

    public void addSemester() {
        try {
            semesterService.createSemester(semester);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Semester added successfully"));
            clear();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add semester: " + e.getMessage()));
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

    public void updateSemester() {
        try {
            semesterService.updateSemester(semester);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Semester updated successfully"));
            semester = new Semester();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update semester: " + e.getMessage()));
        }
    }

    public void deleteSemester(Semester s) {
        try {
            semesterService.deleteSemester(s);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Semester deleted successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to deleteSemester semester: " + e.getMessage()));
        }
    }

    public void clear() {
        semester = new Semester();
    }

    public void editSemester(Semester s) {
        this.semester = Semester.builder()
                .id(s.getId())
                .name(s.getName())
                .startDate(s.getStartDate())
                .endDate(s.getEndDate())
                .semesterType(s.getSemesterType())
                .isActive(s.getIsActive())
                .build();
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
