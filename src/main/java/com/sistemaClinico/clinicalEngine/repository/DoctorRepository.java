package com.sistemaClinico.clinicalEngine.repository;

import com.sistemaClinico.clinicalEngine.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Page<Doctor> findBySpecialty(String specialty, Pageable pageable);

    @Query("SELECT DISTINCT d.specialty FROM Doctor d")
    List<String> findDistinctSpecialties();
}
