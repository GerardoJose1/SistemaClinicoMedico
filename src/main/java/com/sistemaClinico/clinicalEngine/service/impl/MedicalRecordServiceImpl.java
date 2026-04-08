package com.sistemaClinico.clinicalEngine.service.impl;

import com.sistemaClinico.clinicalEngine.entity.MedicalNote;
import com.sistemaClinico.clinicalEngine.entity.MedicalRecord;
import com.sistemaClinico.clinicalEngine.repository.MedicalRecordRepository;
import com.sistemaClinico.clinicalEngine.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    @Override
    public MedicalRecord findByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId)
                .orElseGet(() -> medicalRecordRepository.save(
                        MedicalRecord.builder().patientId(patientId).build()
                ));
    }

    @Override
    public MedicalRecord addNote(Long patientId, MedicalNote note) {
        MedicalRecord record = findByPatientId(patientId);
        note.setMedicalRecord(record);
        record.getNotes().add(note);
        return medicalRecordRepository.save(record);
    }

    @Override
    public Page<MedicalRecord> findAllByPatientId(Long patientId, Pageable pageable) {
        return medicalRecordRepository.findAllByPatientId(patientId, pageable);
    }
}
