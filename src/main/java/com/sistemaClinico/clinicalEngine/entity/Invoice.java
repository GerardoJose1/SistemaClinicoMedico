package com.sistemaClinico.clinicalEngine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
// import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
// Lombok removido. Getters, setters, constructores y builder manual agregados.
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @NotNull
    @Column(nullable = false)
    private BigDecimal amount;

    @NotBlank
    private String service;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Invoice() {}

    public Invoice(Long id, Appointment appointment, BigDecimal amount, String service, LocalDateTime createdAt) {
        this.id = id;
        this.appointment = appointment;
        this.amount = amount;
        this.service = service;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Appointment getAppointment() { return appointment; }
    public void setAppointment(Appointment appointment) { this.appointment = appointment; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static InvoiceBuilder builder() { return new InvoiceBuilder(); }

    public static class InvoiceBuilder {
        private Long id;
        private Appointment appointment;
        private BigDecimal amount;
        private String service;
        private LocalDateTime createdAt;

        public InvoiceBuilder id(Long id) { this.id = id; return this; }
        public InvoiceBuilder appointment(Appointment appointment) { this.appointment = appointment; return this; }
        public InvoiceBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public InvoiceBuilder service(String service) { this.service = service; return this; }
        public InvoiceBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Invoice build() {
            return new Invoice(id, appointment, amount, service, createdAt);
        }
    }
}