package com.sistemaClinico.clinicalEngine.repository;

import com.sistemaClinico.clinicalEngine.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByPatientId(Long patientId);
    Page<MedicalRecord> findAllByPatientId(Long patientId, Pageable pageable);
}