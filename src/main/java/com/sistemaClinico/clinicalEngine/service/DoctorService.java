package com.sistemaClinico.clinicalEngine.service;

import com.sistemaClinico.clinicalEngine.entity.Doctor;
import com.sistemaClinico.clinicalEngine.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Page<Doctor> findBySpecialty(String specialty, Pageable pageable) {
        return doctorRepository.findBySpecialty(specialty, pageable);
    }

    public Page<Doctor> findAll(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    @Cacheable("specialties")
    public List<String> getSpecialties() {
        return doctorRepository.findDistinctSpecialties();
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
    }

    public Doctor update(Long id, Doctor doctor) {
        Doctor existing = findById(id);
        existing.setName(doctor.getName());
        existing.setSpecialty(doctor.getSpecialty());
        existing.setAvailableHours(doctor.getAvailableHours());
        existing.setConsultationFee(doctor.getConsultationFee());
        return doctorRepository.save(existing);
    }

    public void delete(Long id) {
        doctorRepository.deleteById(id);
    }
}