package com.sistemaClinico.clinicalEngine.controller;

import com.sistemaClinico.clinicalEngine.dto.ApiResponse;
import com.sistemaClinico.clinicalEngine.dto.CreateMedicalNoteRequest;
import com.sistemaClinico.clinicalEngine.dto.MedicalRecordResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<ApiResponse<Page<MedicalRecordResponse>>> getRecords(
            @Parameter(description = "ID del paciente", example = "1", required = true)
            @PathVariable Long patientId,
            @Parameter(description = "Número de página (default: 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamaño de página (default: 10)", example = "10")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Campo de ordenamiento (opcional)", example = "id")
            @RequestParam(required = false) String sortField,
            
            @Parameter(description = "Dirección de ordenamiento (opcional)", example = "desc")
            @RequestParam(required = false) String sortDir) {
        
        Pageable pageable = createSafePageable(page, size, sortField, sortDir);
        return ResponseEntity.ok(ApiResponse.success("Historial médico obtenido",medicalRecordService.findAllByPatientId(patientId, pageable)));
    }

    private Pageable createSafePageable(int page, int size, String sortField, String sortDir) {
        // Validar y limitar el tamaño de página
        if (size > 100) size = 100;
        if (size < 1) size = 10;
        
        // Validar página
        if (page < 0) page = 0;
        
        // Si no se especifica ordenamiento, usar ordenamiento por defecto
        if (sortField == null || sortField.trim().isEmpty()) {
            return PageRequest.of(page, size, Sort.by("id").descending());
        }
        
        // Validar que el campo de ordenamiento sea válido
        String[] validFields = {"id", "patientId"};
        boolean isValidField = false;
        for (String validField : validFields) {
            if (validField.equals(sortField.trim())) {
                isValidField = true;
                break;
            }
        }
        
        // Si el campo no es válido, usar ordenamiento por defecto
        if (!isValidField) {
            sortField = "id";
        }
        
        // Determinar dirección de ordenamiento
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDir != null && sortDir.trim().equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        
        return PageRequest.of(page, size, Sort.by(direction, sortField));
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
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> addNote(
            @Parameter(description = "ID del paciente", example = "1", required = true)
            @PathVariable Long patientId,
            
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos de la nota médica a agregar",
                required = true,
                content = @Content(schema = @Schema(implementation = CreateMedicalNoteRequest.class))
            )
            @Valid @RequestBody CreateMedicalNoteRequest note) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Nota añadida al historial",medicalRecordService.addNote(patientId, note)));
    }
}