package com.enrollapp.service;

import com.enrollapp.model.Semester;
import com.enrollapp.repository.EnrollmentRepository;
import com.enrollapp.repository.SemesterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SemesterService {

    private final SemesterRepository semesterRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public SemesterService(SemesterRepository semesterRepository, EnrollmentRepository enrollmentRepository) {
        this.semesterRepository = semesterRepository;
        this.enrollmentRepository=enrollmentRepository;
    }

    public void createSemester(Semester semester) {
        semesterRepository.save(semester);
    }

    public List<Semester> findAll() {
        return semesterRepository.findAll();
    }

    public Optional<Semester> findSemesterById(Long semesterId) {
        return semesterRepository.findById(semesterId);
    }

    public void updateSemester(Semester semester) {
        Semester existing = semesterRepository.findById(semester.getId())
                .orElseThrow(() -> new IllegalStateException("Semester not found"));

        existing.setName(semester.getName());
        existing.setStartDate(semester.getStartDate());
        existing.setEndDate(semester.getEndDate());
        existing.setSemesterType(semester.getSemesterType());
        existing.setIsActive(semester.getIsActive());
        semesterRepository.save(existing);
    }

    public void deleteSemester(Semester semester) {
        if (!semesterRepository.existsById(semester.getId())) {
            throw new IllegalStateException("Semester not found");
        }
        if (enrollmentRepository.existsBySemester(semester)) {
            enrollmentRepository.deleteBySemester(semester);
        }
        semesterRepository.deleteById(semester.getId());
    }
}
