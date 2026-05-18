package com.sistemaClinico.clinicalEngine.repository;

import com.sistemaClinico.clinicalEngine.entity.Appointment;
import com.sistemaClinico.clinicalEngine.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);
    Optional<Appointment> findByDoctorIdAndDateTime(Long doctorId, LocalDateTime dateTime);
    Page<Appointment> findByDoctorIdAndDateTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);
    boolean existsByDoctorIdAndStatusIn(Long doctorId, List<AppointmentStatus> statuses);
}