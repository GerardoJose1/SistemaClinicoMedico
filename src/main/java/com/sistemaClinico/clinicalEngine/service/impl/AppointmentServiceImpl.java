package com.sistemaClinico.clinicalEngine.service.impl;

import com.sistemaClinico.clinicalEngine.entity.Appointment;
import com.sistemaClinico.clinicalEngine.entity.Doctor;
import com.sistemaClinico.clinicalEngine.enums.AppointmentStatus;
import com.sistemaClinico.clinicalEngine.repository.AppointmentRepository;
import com.sistemaClinico.clinicalEngine.service.AppointmentService;
import com.sistemaClinico.clinicalEngine.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;

    @Override
    public Appointment create(Long patientId, Long doctorId, LocalDateTime dateTime) {
        Doctor doctor = doctorService.findById(doctorId);

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

    @Override
    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = findById(id);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
    }

    @Override
    public Page<Appointment> findByDoctor(Long doctorId, Pageable pageable) {
        return appointmentRepository.findByDoctorId(doctorId, pageable);
    }

    @Override
    public Page<Appointment> findByDoctorAndDateRange(Long doctorId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return appointmentRepository.findByDoctorIdAndDateTimeBetween(doctorId, start, end, pageable);
    }
}