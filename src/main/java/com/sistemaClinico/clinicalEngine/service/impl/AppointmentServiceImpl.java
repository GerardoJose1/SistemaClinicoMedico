package com.sistemaClinico.clinicalEngine.service.impl;

import com.sistemaClinico.clinicalEngine.dto.AppointmentEvent;
import com.sistemaClinico.clinicalEngine.entity.Appointment;
import com.sistemaClinico.clinicalEngine.entity.Doctor;
import com.sistemaClinico.clinicalEngine.enums.AppointmentStatus;
import com.sistemaClinico.clinicalEngine.event.AppointmentCompletedEvent;
import com.sistemaClinico.clinicalEngine.repository.AppointmentRepository;
import com.sistemaClinico.clinicalEngine.service.AppointmentEventPublisher;
import com.sistemaClinico.clinicalEngine.service.AppointmentService;
import com.sistemaClinico.clinicalEngine.service.DoctorService;
import org.springframework.context.ApplicationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final AppointmentEventPublisher eventPublisher;
    private final ApplicationEventPublisher applicationEventPublisher;

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

        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        // Publish appointment confirmed event
        AppointmentEvent event = AppointmentEvent.builder()
                .appointmentId(UUID.randomUUID())
                .patientId(patientId)
                .patientEmail("patient@example.com") // TODO: Get from patient service
                .patientName("Patient Name") // TODO: Get from patient service
                .doctorName(doctor.getName())
                .specialty(doctor.getSpecialty())
                .dateTime(dateTime)
                .status(AppointmentStatus.SCHEDULED.name())
                .build();
        
        eventPublisher.publishAppointmentConfirmed(event);
        
        return savedAppointment;
    }

    @Override
    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = findById(id);
        AppointmentStatus oldStatus = appointment.getStatus();
        appointment.setStatus(status);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        // Publish appointment status changed event
        AppointmentEvent event = AppointmentEvent.builder()
                .appointmentId(UUID.randomUUID())
                .patientId(appointment.getPatientId())
                .patientEmail("patient@example.com") // TODO: Get from patient service
                .patientName("Patient Name") // TODO: Get from patient service
                .doctorName(appointment.getDoctor().getName())
                .specialty(appointment.getDoctor().getSpecialty())
                .dateTime(appointment.getDateTime())
                .status(status.name())
                .build();
        
        eventPublisher.publishAppointmentStatusChanged(event);
        
        // If appointment is cancelled, publish order cancelled event
        if (status == AppointmentStatus.CANCELLED) {
            eventPublisher.publishOrderCancelled(event);
        }
        
        // If appointment is completed, publish event for billing generation
        if (status == AppointmentStatus.COMPLETED) {
            applicationEventPublisher.publishEvent(new AppointmentCompletedEvent(id));
            log.info("Published appointment completed event for appointment: {}", id);
        }
        
        return savedAppointment;
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

    @Override
    public Page<Appointment> findByPatient(Long patientId, Pageable pageable) {
        return appointmentRepository.findByPatientId(patientId, pageable);
    }
}