package com.sistemaClinico.clinicalEngine.entity;

import com.sistemaClinico.clinicalEngine.enums.AppointmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
// import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
// Lombok removido. Getters, setters, constructores y builder manual agregados.
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @NotNull
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    public Appointment() {}

    public Appointment(Long id, Long patientId, Doctor doctor, LocalDateTime dateTime, AppointmentStatus status, MedicalRecord medicalRecord) {
        this.id = id;
        this.patientId = patientId;
        this.doctor = doctor;
        this.dateTime = dateTime;
        this.status = status;
        this.medicalRecord = medicalRecord;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    public void setMedicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; }

    public static AppointmentBuilder builder() { return new AppointmentBuilder(); }

    public static class AppointmentBuilder {
        private Long id;
        private Long patientId;
        private Doctor doctor;
        private LocalDateTime dateTime;
        private AppointmentStatus status;
        private MedicalRecord medicalRecord;

        public AppointmentBuilder id(Long id) { this.id = id; return this; }
        public AppointmentBuilder patientId(Long patientId) { this.patientId = patientId; return this; }
        public AppointmentBuilder doctor(Doctor doctor) { this.doctor = doctor; return this; }
        public AppointmentBuilder dateTime(LocalDateTime dateTime) { this.dateTime = dateTime; return this; }
        public AppointmentBuilder status(AppointmentStatus status) { this.status = status; return this; }
        public AppointmentBuilder medicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; return this; }
        public Appointment build() {
            return new Appointment(id, patientId, doctor, dateTime, status, medicalRecord);
        }
    }
}