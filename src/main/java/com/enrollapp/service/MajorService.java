package com.enrollapp.service;

import com.enrollapp.model.Major;
import com.enrollapp.repository.MajorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MajorService {

    private final MajorRepository majorRepository;

    @Autowired
    public MajorService(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    public void createMajor(Major major) {
        majorRepository.save(major);
    }

    public List<Major> findAll() {
        return majorRepository.findAll();
    }

    public void updateMajor(Major major) {
        Major existing = majorRepository.findById(major.getId())
                .orElseThrow(() -> new IllegalStateException("Major not found"));

        existing.setName(major.getName());
        majorRepository.save(existing);
    }

    public void deleteMajor(Major major) {
        if (!majorRepository.existsById(major.getId())) {
            throw new IllegalStateException("Major not found");
        }
        majorRepository.deleteById(major.getId());
    }
}
