package com.sistemaClinico.clinicalEngine.service;

import com.sistemaClinico.clinicalEngine.entity.Appointment;
import com.sistemaClinico.clinicalEngine.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface AppointmentService {
    Appointment create(Long patientId, Long doctorId, LocalDateTime dateTime);
    Appointment updateStatus(Long id, AppointmentStatus status);
    Appointment findById(Long id);
    Page<Appointment> findByDoctor(Long doctorId, Pageable pageable);
    Page<Appointment> findByDoctorAndDateRange(Long doctorId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Appointment> findByPatient(Long patientId, Pageable pageable);
}