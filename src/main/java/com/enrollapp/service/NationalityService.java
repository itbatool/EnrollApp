package com.enrollapp.service;

import com.enrollapp.model.Nationality;
import com.enrollapp.repository.NationalityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class NationalityService {

    private final NationalityRepository nationalityRepository;

    @Autowired
    public NationalityService(NationalityRepository nationalityRepository) {
        this.nationalityRepository = nationalityRepository;
    }

    public void createNationality(Nationality nationality) {
        nationalityRepository.save(nationality);
    }

    public List<Nationality> findAll() {
        return nationalityRepository.findAll();
    }

    public void updateNationality(Nationality nationality) {
        Nationality existing = nationalityRepository.findById(nationality.getId())
                .orElseThrow(() -> new IllegalStateException("Nationality not found"));

        existing.setName(nationality.getName());
        nationalityRepository.save(existing);
    }

    public void deleteNationality(Nationality nationality) {
        if (!nationalityRepository.existsById(nationality.getId())) {
            throw new IllegalStateException("Nationality not found");
        }
        nationalityRepository.deleteById(nationality.getId());
    }
}