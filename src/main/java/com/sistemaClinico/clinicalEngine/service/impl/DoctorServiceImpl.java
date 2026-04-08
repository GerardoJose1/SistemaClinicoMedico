package com.sistemaClinico.clinicalEngine.service.impl;

import com.sistemaClinico.clinicalEngine.dto.CreateDoctorRequest;
import com.sistemaClinico.clinicalEngine.dto.UpdateDoctorRequest;
import com.sistemaClinico.clinicalEngine.entity.Doctor;
import com.sistemaClinico.clinicalEngine.repository.DoctorRepository;
import com.sistemaClinico.clinicalEngine.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor create(CreateDoctorRequest doctorRequest) {
        Doctor doctor = Doctor.builder()
                .name(doctorRequest.getName())
                .specialty(doctorRequest.getSpecialty())
                .availableHours(doctorRequest.getAvailableHours())
                .consultationFee(doctorRequest.getConsultationFee())
                .build();
        return doctorRepository.save(doctor);
    }

    @Override
    public Page<Doctor> findBySpecialty(String specialty, Pageable pageable) {
        return doctorRepository.findBySpecialty(specialty, pageable);
    }

    @Override
    public Page<Doctor> findAll(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    @Override
    @Cacheable("specialties")
    public List<String> getSpecialties() {
        return doctorRepository.findDistinctSpecialties();
    }

    @Override
    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
    }

    @Override
    public Doctor update(Long id, UpdateDoctorRequest doctorRequest) {
        Doctor existing = findById(id);
        existing.setName(doctorRequest.getName());
        existing.setSpecialty(doctorRequest.getSpecialty());
        existing.setAvailableHours(doctorRequest.getAvailableHours());
        existing.setConsultationFee(doctorRequest.getConsultationFee());
        return doctorRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        doctorRepository.deleteById(id);
    }
}