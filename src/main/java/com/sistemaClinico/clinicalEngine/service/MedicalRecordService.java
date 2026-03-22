package com.sistemaClinico.clinicalEngine.service;

import com.sistemaClinico.clinicalEngine.entity.MedicalNote;
import com.sistemaClinico.clinicalEngine.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicalRecordService {
    MedicalRecord findByPatientId(Long patientId);
    MedicalRecord addNote(Long patientId, MedicalNote note);
    Page<MedicalRecord> findAllByPatientId(Long patientId, Pageable pageable);
}