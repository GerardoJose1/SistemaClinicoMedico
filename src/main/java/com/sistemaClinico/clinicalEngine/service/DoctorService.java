package com.sistemaClinico.clinicalEngine.service;

import com.sistemaClinico.clinicalEngine.dto.UpdateDoctorRequest;
import com.sistemaClinico.clinicalEngine.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface DoctorService {
    Doctor save(Doctor doctor);
    Page<Doctor> findBySpecialty(String specialty, Pageable pageable);
    Page<Doctor> findAll(Pageable pageable);
    List<String> getSpecialties();
    Doctor findById(Long id);
    Doctor update(Long id, UpdateDoctorRequest doctorRequest);
    void delete(Long id);
}