package com.sistemaClinico.clinicalEngine.service;

import com.sistemaClinico.clinicalEngine.entity.MedicalNote;
import com.sistemaClinico.clinicalEngine.entity.MedicalRecord;
import com.sistemaClinico.clinicalEngine.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecord findByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId)
                .orElseGet(() -> medicalRecordRepository.save(
                        MedicalRecord.builder().patientId(patientId).build()
                ));
    }

    public MedicalRecord addNote(Long patientId, MedicalNote note) {
        MedicalRecord record = findByPatientId(patientId);
        note.setMedicalRecord(record);
        record.getNotes().add(note);
        return medicalRecordRepository.save(record);
    }

    public Page<MedicalRecord> findAllByPatientId(Long patientId, Pageable pageable) {
        return medicalRecordRepository.findAllByPatientId(patientId, pageable);
    }
}