package com.sistemaClinico.clinicalEngine.controller;

import com.sistemaClinico.clinicalEngine.entity.Appointment;
import com.sistemaClinico.clinicalEngine.enums.AppointmentStatus;
import com.sistemaClinico.clinicalEngine.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<Page<Appointment>> getByDoctor(
            @RequestParam Long doctorId,
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) LocalDateTime end,
            Pageable pageable) {
        if (start != null && end != null) {
            return ResponseEntity.ok(appointmentService.findByDoctorAndDateRange(doctorId, start, end, pageable));
        }
        return ResponseEntity.ok(appointmentService.findByDoctor(doctorId, pageable));
    }

    @PostMapping
    public ResponseEntity<Appointment> create(
            @RequestParam Long patientId,
            @RequestParam Long doctorId,
            @RequestParam LocalDateTime dateTime) {
        return ResponseEntity.ok(appointmentService.create(patientId, doctorId, dateTime));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }
}