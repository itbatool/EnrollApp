package com.enrollapp.service;

import com.enrollapp.model.*;
import com.enrollapp.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public void createStudent(Student student) {
        if (studentRepository.findStudentByEmail(student.getEmail()).isPresent()) {
            throw new IllegalStateException("Email is taken");
        }
        studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> findStudentById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    public void updateStudent(Student student) {
        Student existing = studentRepository.findById(student.getId())
                .orElseThrow(() -> new IllegalStateException("Student not found"));

        studentRepository.findStudentByEmail(student.getEmail())
                .filter(s -> !s.getId().equals(student.getId()))
                .ifPresent(s -> { throw new IllegalStateException("Email already taken"); });

        existing.setName(student.getName());
        existing.setNationalId(student.getNationalId());
        existing.setEmail(student.getEmail());
        existing.setGender(student.getGender());
        existing.setBirthDate(student.getBirthDate());
        existing.setMobileNo(student.getMobileNo());
        existing.setSchoolAvg(student.getSchoolAvg());
        existing.setNationality(student.getNationality());
        existing.setFaculty(student.getFaculty());
        existing.setMajor(student.getMajor());

        studentRepository.save(existing);
    }

    public void deleteStudent(Student student) {
        if (!studentRepository.existsById(student.getId())) {
            throw new IllegalStateException("Student not found");
        }
        if (enrollmentRepository.existsByStudent(student)) {
            enrollmentRepository.deleteByStudent(student);
        }
        studentRepository.deleteById(student.getId());
    }
}
