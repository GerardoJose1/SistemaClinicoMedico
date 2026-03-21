package com.sistemaClinico.clinicalEngine.service;

import com.sistemaClinico.clinicalEngine.entity.Appointment;
import com.sistemaClinico.clinicalEngine.entity.Doctor;
import com.sistemaClinico.clinicalEngine.enums.AppointmentStatus;
import com.sistemaClinico.clinicalEngine.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;

    public Appointment create(Long patientId, Long doctorId, LocalDateTime dateTime) {
        // Validar que el médico existe
        Doctor doctor = doctorService.findById(doctorId);

        // Validar disponibilidad - no duplicar horario
        appointmentRepository.findByDoctorIdAndDateTime(doctorId, dateTime)
                .ifPresent(a -> { throw new RuntimeException("El médico ya tiene una cita en ese horario"); });

        Appointment appointment = Appointment.builder()
                .patientId(patientId)
                .doctor(doctor)
                .dateTime(dateTime)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        return appointmentRepository.save(appointment);
    }

    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = findById(id);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    public Page<Appointment> findByDoctor(Long doctorId, Pageable pageable) {
        return appointmentRepository.findByDoctorId(doctorId, pageable);
    }

    public Page<Appointment> findByDoctorAndDateRange(Long doctorId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return appointmentRepository.findByDoctorIdAndDateTimeBetween(doctorId, start, end, pageable);
    }
}