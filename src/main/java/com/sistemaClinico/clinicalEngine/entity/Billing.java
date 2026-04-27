package com.sistemaClinico.clinicalEngine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "billing")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @NotNull
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "billing_date", nullable = false)
    private LocalDateTime billingDate;

    @Column(name = "invoice_number", unique = true)
    private String invoiceNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingStatus status;


    public enum BillingStatus {
        PENDING, PAID, CANCELLED
    }
}
