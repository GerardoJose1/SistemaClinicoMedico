package com.sistemaClinico.clinicalEngine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
// import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "doctors")
// Lombok removido. Getters, setters, constructores y builder manual agregados.
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String specialty;

    @Column(name = "available_hours")
    private String availableHours;

    @NotNull
    @DecimalMin("0.0")
    @Column(name = "consultation_fee", nullable = false)
    private BigDecimal consultationFee;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    public Doctor() {}

    public Doctor(Long id, String name, String specialty, String availableHours, BigDecimal consultationFee, List<Appointment> appointments) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.availableHours = availableHours;
        this.consultationFee = consultationFee;
        this.appointments = appointments;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public String getAvailableHours() { return availableHours; }
    public void setAvailableHours(String availableHours) { this.availableHours = availableHours; }
    public BigDecimal getConsultationFee() { return consultationFee; }
    public void setConsultationFee(BigDecimal consultationFee) { this.consultationFee = consultationFee; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    public static DoctorBuilder builder() { return new DoctorBuilder(); }

    public static class DoctorBuilder {
        private Long id;
        private String name;
        private String specialty;
        private String availableHours;
        private BigDecimal consultationFee;
        private List<Appointment> appointments;

        public DoctorBuilder id(Long id) { this.id = id; return this; }
        public DoctorBuilder name(String name) { this.name = name; return this; }
        public DoctorBuilder specialty(String specialty) { this.specialty = specialty; return this; }
        public DoctorBuilder availableHours(String availableHours) { this.availableHours = availableHours; return this; }
        public DoctorBuilder consultationFee(BigDecimal consultationFee) { this.consultationFee = consultationFee; return this; }
        public DoctorBuilder appointments(List<Appointment> appointments) { this.appointments = appointments; return this; }
        public Doctor build() {
            return new Doctor(id, name, specialty, availableHours, consultationFee, appointments);
        }
    }
}