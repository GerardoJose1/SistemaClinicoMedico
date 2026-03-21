package com.sistemaClinico.clinicalEngine.repository;

import com.sistemaClinico.clinicalEngine.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);
    Optional<Appointment> findByDoctorIdAndDateTime(Long doctorId, LocalDateTime dateTime);
    Page<Appointment> findByDoctorIdAndDateTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end, Pageable pageable);
}