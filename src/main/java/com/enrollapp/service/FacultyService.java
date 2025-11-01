package com.enrollapp.service;

import com.enrollapp.model.Faculty;
import com.enrollapp.repository.FacultyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public void createFaculty(Faculty faculty) {
        facultyRepository.save(faculty);
    }

    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    public void updateFaculty(Faculty faculty) {
        Faculty existing = facultyRepository.findById(faculty.getId())
                .orElseThrow(() -> new IllegalStateException("Faculty not found"));

        existing.setName(faculty.getName());
        facultyRepository.save(existing);
    }

    public void deleteFaculty(Faculty faculty) {
        if (!facultyRepository.existsById(faculty.getId())) {
            throw new IllegalStateException("Faculty not found");
        }
        facultyRepository.deleteById(faculty.getId());
    }
}