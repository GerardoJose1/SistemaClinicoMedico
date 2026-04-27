package com.sistemaClinico.clinicalEngine.service;

import com.sistemaClinico.clinicalEngine.entity.Billing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface BillingService {
    Billing generateInvoiceForAppointment(Long appointmentId);
    Billing getInvoiceByAppointmentId(Long appointmentId);
    Page<Billing> getInvoicesByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<Billing> getInvoicesByStatus(Billing.BillingStatus status, Pageable pageable);
}
