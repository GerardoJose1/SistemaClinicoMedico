package com.sistemaClinico.clinicalEngine.service;

import com.sistemaClinico.clinicalEngine.dto.CreateMedicalNoteRequest;
import com.sistemaClinico.clinicalEngine.dto.MedicalRecordResponse;
import com.sistemaClinico.clinicalEngine.entity.MedicalNote;
import com.sistemaClinico.clinicalEngine.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicalRecordService {
    MedicalRecord findByPatientId(Long patientId);
    MedicalRecordResponse addNote(Long patientId, CreateMedicalNoteRequest note);
    Page<MedicalRecordResponse> findAllByPatientId(Long patientId, Pageable pageable);
}