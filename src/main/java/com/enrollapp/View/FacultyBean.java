package com.enrollapp.View;

import com.enrollapp.model.Faculty;
import com.enrollapp.service.FacultyService;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("facultyBean")
@ViewScoped
@Getter
@Setter
public class FacultyBean implements Serializable {

    private Faculty faculty = new Faculty();

    @Inject
    private FacultyService facultyService;

    @Inject
    private FacesContext facesContext;

    public void addFaculty() {
        try {
            facultyService.createFaculty(faculty);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Faculty added successfully"));
            faculty = new Faculty();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add faculty: " + e.getMessage()));
        }
    }

    public List<Faculty> getAllFaculties() {
        try {
            return facultyService.findAll();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load faculties: " + e.getMessage()));
            return new ArrayList<>();
        }
    }

    public void deleteFaculty(Faculty faculty) {
        try {
            facultyService.deleteFaculty(faculty);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Faculty deleted successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete faculty: " + e.getMessage()));
        }
    }

    public void clear() {
        faculty = new Faculty();
    }

    public void editFaculty(Faculty faculty) {
        this.faculty = Faculty.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .build();
    }

    public void updateFaculty() {
        try {
            facultyService.updateFaculty(faculty);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Faculty updated successfully"));
            faculty = new Faculty();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update faculty: " + e.getMessage()));
        }
    }
}
