package com.enrollapp.View;

import com.enrollapp.model.Nationality;
import com.enrollapp.service.NationalityService;
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

@Named("nationalityBean")
@ViewScoped
@Getter
@Setter
public class NationalityBean implements Serializable {

    private Nationality nationality = new Nationality();

    @Inject
    private NationalityService nationalityService;

    @Inject
    private FacesContext facesContext;

    public void clear() {
        nationality = new Nationality();
    }

    public void addNationality() {
        try {
            nationalityService.createNationality(nationality);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Nationality added successfully"));
            clear();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to add nationality: " + e.getMessage()));
        }
    }

    public void deleteNationality(Nationality n) {
        try {
            nationalityService.deleteNationality(n);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Nationality deleted successfully"));
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to delete nationality: " + e.getMessage()));
        }
    }

    public List<Nationality> getAllNationalities() {
        try {
            return nationalityService.findAll();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load nationalities: " + e.getMessage()));
            return new ArrayList<>();
        }
    }

    public void editNationality(Nationality n) {
        this.nationality = Nationality.builder()
                .id(n.getId())
                .name(n.getName())
                .build();
    }

    public void updateNationality() {
        try {
            nationalityService.updateNationality(nationality);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Nationality updated successfully"));
            nationality = new Nationality();
        } catch (Exception e) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update nationality: " + e.getMessage()));
        }
    }
}
