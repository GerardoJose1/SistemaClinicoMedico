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
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public Appointment create(Long patientId, Long doctorId, LocalDateTime dateTime, String patientEmail, String patientName) {
        Doctor doctor = doctorService.findById(doctorId);

        // Validar que la fecha sea futura
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se pueden crear citas en fechas pasadas");
        }

        appointmentRepository.findByDoctorIdAndDateTime(doctorId, dateTime)
                .ifPresent(a -> { throw new RuntimeException("El médico ya tiene una cita en ese horario"); });

        Appointment appointment = Appointment.builder()
                .patientId(patientId)
                .patientName(patientName)
                .patientEmail(patientEmail)
                .doctor(doctor)
                .dateTime(dateTime)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        // Publish appointment confirmed event
        AppointmentEvent event = AppointmentEvent.builder()
                .appointmentId(UUID.randomUUID())
                .patientId(patientId)
                .patientEmail(patientEmail)
                .patientName(patientName)
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

        // Validar transiciones de estado válidas
        validateStatusTransition(oldStatus, status);

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

    private void validateStatusTransition(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        // No permitir cambiar estado si ya está cancelada o completada
        if (currentStatus == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("No se puede cambiar el estado de una cita cancelada");
        }
        if (currentStatus == AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("No se puede cambiar el estado de una cita completada");
        }

        // Validar transiciones permitidas
        switch (currentStatus) {
            case SCHEDULED:
                if (newStatus != AppointmentStatus.CONFIRMED && newStatus != AppointmentStatus.CANCELLED) {
                    throw new IllegalStateException(
                        "Desde SCHEDULED solo se puede cambiar a CONFIRMED o CANCELLED"
                    );
                }
                break;
            case CONFIRMED:
                if (newStatus != AppointmentStatus.IN_PROGRESS && newStatus != AppointmentStatus.CANCELLED) {
                    throw new IllegalStateException(
                        "Desde CONFIRMED solo se puede cambiar a IN_PROGRESS o CANCELLED"
                    );
                }
                break;
            case IN_PROGRESS:
                if (newStatus != AppointmentStatus.COMPLETED && newStatus != AppointmentStatus.CANCELLED) {
                    throw new IllegalStateException(
                        "Desde IN_PROGRESS solo se puede cambiar a COMPLETED o CANCELLED"
                    );
                }
                break;
            default:
                throw new IllegalStateException("Transición de estado no válida");
        }
    }
}