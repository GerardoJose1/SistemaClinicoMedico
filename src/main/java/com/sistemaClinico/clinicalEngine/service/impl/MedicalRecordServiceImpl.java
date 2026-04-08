package com.sistemaClinico.clinicalEngine.service.impl;

import com.sistemaClinico.clinicalEngine.dto.CreateMedicalNoteRequest;
import com.sistemaClinico.clinicalEngine.dto.MedicalRecordMapper;
import com.sistemaClinico.clinicalEngine.dto.MedicalRecordResponse;
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
    public MedicalRecordResponse addNote(Long patientId, CreateMedicalNoteRequest noteRequest) {
        MedicalRecord record = findByPatientId(patientId);
        
        // Inicializar la lista de notas si es nula
        if (record.getNotes() == null) {
            record.setNotes(new java.util.ArrayList<>());
        }
        
        // Crear MedicalNote a partir del DTO
        MedicalNote note = MedicalNote.builder()
                .diagnosis(noteRequest.getDiagnosis())
                .treatment(noteRequest.getTreatment())
                .notes(noteRequest.getNotes())
                .build();
        
        note.setMedicalRecord(record);
        record.getNotes().add(note);
        
        MedicalRecord savedRecord = medicalRecordRepository.save(record);
        return MedicalRecordMapper.toResponse(savedRecord);
    }

    @Override
    public Page<MedicalRecordResponse> findAllByPatientId(Long patientId, Pageable pageable) {
        Page<MedicalRecord> medicalRecords = medicalRecordRepository.findAllByPatientId(patientId, pageable);
        return medicalRecords.map(MedicalRecordMapper::toResponse);
    }
}
