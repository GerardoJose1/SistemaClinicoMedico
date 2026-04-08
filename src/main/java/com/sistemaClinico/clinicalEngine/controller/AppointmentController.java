package com.sistemaClinico.clinicalEngine.controller;

import com.sistemaClinico.clinicalEngine.dto.ApiResponse;
import com.sistemaClinico.clinicalEngine.entity.Appointment;
import com.sistemaClinico.clinicalEngine.enums.AppointmentStatus;
import com.sistemaClinico.clinicalEngine.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Operaciones para gestionar citas médicas")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    @Operation(
        summary = "Obtener citas de un doctor",
        description = "Retorna una lista paginada de citas para un doctor específico, con opción de filtrar por rango de fechas"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Citas obtenidas exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Doctor no encontrado")
    })
    public ResponseEntity<ApiResponse<Page<Appointment>>> getByDoctor(
            @Parameter(description = "ID del doctor", example = "1", required = true)
            @RequestParam Long doctorId,
            
            @Parameter(description = "Fecha de inicio (ISO 8601 - opcional)", example = "2024-01-01T00:00:00")
            @RequestParam(required = false) LocalDateTime start,
            
            @Parameter(description = "Fecha de fin (ISO 8601 - opcional)", example = "2024-12-31T23:59:59")
            @RequestParam(required = false) LocalDateTime end,
            
            Pageable pageable) {
        if (start != null && end != null) {
            return ResponseEntity.ok(ApiResponse.success("Citas obtenidas",appointmentService.findByDoctorAndDateRange(doctorId, start, end, pageable)));
        }
        return ResponseEntity.ok(ApiResponse.success("Citas obtenidas",appointmentService.findByDoctor(doctorId, pageable)));
    }

    @PostMapping
    @Operation(
        summary = "Crear nueva cita",
        description = "Crea una nueva cita médica entre un paciente y un doctor"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Cita creada exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parámetros inválidos o conflicto de horario"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Paciente o doctor no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error al crear cita")
    })
    public ResponseEntity<ApiResponse<Appointment>> create(
            @Parameter(description = "ID del paciente", example = "1", required = true)
            @RequestParam Long patientId,
            
            @Parameter(description = "ID del doctor", example = "1", required = true)
            @RequestParam Long doctorId,
            
            @Parameter(description = "Fecha y hora de la cita (ISO 8601)", example = "2024-03-15T14:30:00", required = true)
            @RequestParam LocalDateTime dateTime) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Cita creada",appointmentService.create(patientId, doctorId, dateTime)));
    }

    @PutMapping("/{id}/status")
    @Operation(
        summary = "Actualizar estado de cita",
        description = "Cambia el estado de una cita (CONFIRMED, CANCELLED, COMPLETED, etc.)"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estado de cita actualizado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Estado inválido"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cita no encontrada"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error al actualizar cita")
    })
    public ResponseEntity<ApiResponse<Appointment>> updateStatus(
            @Parameter(description = "ID de la cita", example = "1", required = true)
            @PathVariable Long id,
            
            @Parameter(description = "Nuevo estado de la cita", example = "CONFIRMED", required = true)
            @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(ApiResponse.success("Cita actualizada",appointmentService.updateStatus(id, status)));
    }
}
