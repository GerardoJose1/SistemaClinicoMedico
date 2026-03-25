package com.sistemaClinico.clinicalEngine.controller;

import com.sistemaClinico.clinicalEngine.dto.ApiResponse;
import com.sistemaClinico.clinicalEngine.entity.Appointment;
import com.sistemaClinico.clinicalEngine.enums.AppointmentStatus;
import com.sistemaClinico.clinicalEngine.service.AppointmentService;
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
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Appointment>>> getByDoctor(
            @RequestParam Long doctorId,
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) LocalDateTime end,
            Pageable pageable) {
        if (start != null && end != null) {
            return ResponseEntity.ok(ApiResponse.success("Citas obtenidas",appointmentService.findByDoctorAndDateRange(doctorId, start, end, pageable)));
        }
        return ResponseEntity.ok(ApiResponse.success("Citas obtenidas",appointmentService.findByDoctor(doctorId, pageable)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Appointment>> create(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam LocalDateTime dateTime) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Cita creada",appointmentService.create(patientId, doctorId, dateTime)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Appointment>> updateStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(ApiResponse.success("Cita actualizada",appointmentService.updateStatus(id, status)));
    }
}