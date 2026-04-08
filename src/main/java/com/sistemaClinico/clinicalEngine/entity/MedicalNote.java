package com.sistemaClinico.clinicalEngine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
// import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_notes")
// Lombok removido. Getters, setters, constructores y builder manual agregados.
public class MedicalNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @NotBlank
    @Column(nullable = false)
    private String diagnosis;

    @NotBlank
    @Column(nullable = false)
    private String treatment;

    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public MedicalNote() {}

    public MedicalNote(Long id, MedicalRecord medicalRecord, String diagnosis, String treatment, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.medicalRecord = medicalRecord;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public MedicalRecord getMedicalRecord() { return medicalRecord; }
    public void setMedicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static MedicalNoteBuilder builder() { return new MedicalNoteBuilder(); }

    public static class MedicalNoteBuilder {
        private Long id;
        private MedicalRecord medicalRecord;
        private String diagnosis;
        private String treatment;
        private String notes;
        private LocalDateTime createdAt;

        public MedicalNoteBuilder id(Long id) { this.id = id; return this; }
        public MedicalNoteBuilder medicalRecord(MedicalRecord medicalRecord) { this.medicalRecord = medicalRecord; return this; }
        public MedicalNoteBuilder diagnosis(String diagnosis) { this.diagnosis = diagnosis; return this; }
        public MedicalNoteBuilder treatment(String treatment) { this.treatment = treatment; return this; }
        public MedicalNoteBuilder notes(String notes) { this.notes = notes; return this; }
        public MedicalNoteBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public MedicalNote build() {
            return new MedicalNote(id, medicalRecord, diagnosis, treatment, notes, createdAt);
        }
    }
}