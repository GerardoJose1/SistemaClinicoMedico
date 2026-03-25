package com.sistemaClinico.clinicalEngine.controller;

import com.sistemaClinico.clinicalEngine.dto.ApiResponse;
import com.sistemaClinico.clinicalEngine.entity.MedicalNote;
import com.sistemaClinico.clinicalEngine.entity.MedicalRecord;
import com.sistemaClinico.clinicalEngine.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @GetMapping("/{patientId}")
    public ResponseEntity<ApiResponse<Page<MedicalRecord>>> getRecords(
            @PathVariable Long patientId,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Historial médico obtenido",medicalRecordService.findAllByPatientId(patientId, pageable)));
    }

    @PostMapping("/{patientId}/notes")
    public ResponseEntity<ApiResponse<MedicalRecord>> addNote(
            @PathVariable Long patientId,
            @Valid @RequestBody MedicalNote note) {
        return ResponseEntity.ok(ApiResponse.success("Nota añadida al historial",medicalRecordService.addNote(patientId, note)));
    }
}