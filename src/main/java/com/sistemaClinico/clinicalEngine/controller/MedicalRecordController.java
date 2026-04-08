package com.sistemaClinico.clinicalEngine.controller;

import com.sistemaClinico.clinicalEngine.dto.ApiResponse;
import com.sistemaClinico.clinicalEngine.entity.MedicalNote;
import com.sistemaClinico.clinicalEngine.entity.MedicalRecord;
import com.sistemaClinico.clinicalEngine.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
@Tag(name = "Medical Records", description = "Operaciones para gestionar historiales médicos")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @GetMapping("/{patientId}")
    @Operation(
        summary = "Obtener historial médico de paciente",
        description = "Retorna el historial médico completo de un paciente con paginación"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Historial médico obtenido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos")
    })
    public ResponseEntity<ApiResponse<Page<MedicalRecord>>> getRecords(
            @Parameter(description = "ID del paciente", example = "1", required = true)
            @PathVariable Long patientId,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Historial médico obtenido",medicalRecordService.findAllByPatientId(patientId, pageable)));
    }

    @PostMapping("/{patientId}/notes")
    @Operation(
        summary = "Agregar nota médica",
        description = "Agrega una nueva nota médica al historial de un paciente"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Nota agregada al historial"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de nota inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error al agregar nota")
    })
    public ResponseEntity<ApiResponse<MedicalRecord>> addNote(
            @Parameter(description = "ID del paciente", example = "1", required = true)
            @PathVariable Long patientId,
            
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos de la nota médica a agregar",
                required = true,
                content = @Content(schema = @Schema(implementation = MedicalNote.class))
            )
            @Valid @RequestBody MedicalNote note) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Nota añadida al historial",medicalRecordService.addNote(patientId, note)));
    }
}