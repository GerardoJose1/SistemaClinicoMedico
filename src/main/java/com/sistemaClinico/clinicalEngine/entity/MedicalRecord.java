package com.sistemaClinico.clinicalEngine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
// import lombok.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Entity
@Table(name = "medical_records")
// Lombok removido. Getters, setters, constructores y builder manual agregados.
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL)
    private List<MedicalNote> notes;

    public MedicalRecord() {
        this.appointments = new java.util.ArrayList<>();
        this.notes = new java.util.ArrayList<>();
    }

    public MedicalRecord(Long id, Long patientId, List<Appointment> appointments, List<MedicalNote> notes) {
        this.id = id;
        this.patientId = patientId;
        this.appointments = appointments;
        this.notes = notes;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
    public List<MedicalNote> getNotes() { return notes; }
    public void setNotes(List<MedicalNote> notes) { this.notes = notes; }

    public static MedicalRecordBuilder builder() { return new MedicalRecordBuilder(); }

    public static class MedicalRecordBuilder {
        private Long id;
        private Long patientId;
        private List<Appointment> appointments;
        private List<MedicalNote> notes;

        public MedicalRecordBuilder id(Long id) { this.id = id; return this; }
        public MedicalRecordBuilder patientId(Long patientId) { this.patientId = patientId; return this; }
        public MedicalRecordBuilder appointments(List<Appointment> appointments) { this.appointments = appointments; return this; }
        public MedicalRecordBuilder notes(List<MedicalNote> notes) { this.notes = notes; return this; }
        public MedicalRecord build() {
            return new MedicalRecord(id, patientId, appointments, notes);
        }
    }
}