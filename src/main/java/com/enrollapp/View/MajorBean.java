package com.enrollapp.View;

import com.enrollapp.model.Major;
import com.enrollapp.service.MajorService;
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

@Named("majorBean")
@ViewScoped
@Getter
@Setter
public class MajorBean implements Serializable {

    private Major major = new Major();

    @Inject
    private FacesContext facesContext;

    @Inject
    private MajorService majorService;

    public List<Major> getAllMajors() {
        try {
            return majorService.findAll();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load majors: " + e.getMessage()));
            return new ArrayList<>();
        }
    }

    public void clear() {
        major = new Major();
    }

    public void addMajor() {
        try {
            majorService.createMajor(major);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Major added successfully"));
            clear();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add major: " + e.getMessage()));
        }
    }

    public void deleteMajor(Major major) {
        try {
            majorService.deleteMajor(major);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Major deleted successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete major: " + e.getMessage()));
        }
    }

    public void editMajor(Major major) {
        this.major = Major.builder()
                .id(major.getId())
                .name(major.getName())
                .build();
    }

    public void updateMajor() {
        try {
            majorService.updateMajor(major);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Major updated successfully"));
            major = new Major();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update major: " + e.getMessage()));
        }
    }
}
