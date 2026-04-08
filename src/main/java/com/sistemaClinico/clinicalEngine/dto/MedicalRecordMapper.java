package com.sistemaClinico.clinicalEngine.dto;

import com.sistemaClinico.clinicalEngine.entity.MedicalNote;
import com.sistemaClinico.clinicalEngine.entity.MedicalRecord;
import java.util.List;
import java.util.stream.Collectors;

public class MedicalRecordMapper {
    
    public static MedicalRecordResponse toResponse(MedicalRecord entity) {
        if (entity == null) return null;
        
        List<MedicalNoteResponse> notesResponses = entity.getNotes() != null 
            ? entity.getNotes().stream()
                .map(MedicalRecordMapper::toNoteResponse)
                .collect(Collectors.toList())
            : List.of();
        
        return new MedicalRecordResponse(
            entity.getId(),
            entity.getPatientId(),
            notesResponses,
            null
        );
    }
    
    public static MedicalNoteResponse toNoteResponse(MedicalNote entity) {
        if (entity == null) return null;
        
        return new MedicalNoteResponse(
            entity.getId(),
            entity.getDiagnosis(),
            entity.getTreatment(),
            entity.getNotes(),
            entity.getCreatedAt()
        );
    }
}
