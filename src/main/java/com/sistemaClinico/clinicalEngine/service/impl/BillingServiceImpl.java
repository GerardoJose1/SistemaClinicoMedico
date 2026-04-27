package com.sistemaClinico.clinicalEngine.service.impl;

import com.sistemaClinico.clinicalEngine.entity.Appointment;
import com.sistemaClinico.clinicalEngine.entity.Billing;
import com.sistemaClinico.clinicalEngine.enums.AppointmentStatus;
import com.sistemaClinico.clinicalEngine.repository.BillingRepository;
import com.sistemaClinico.clinicalEngine.service.AppointmentService;
import com.sistemaClinico.clinicalEngine.service.BillingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingServiceImpl implements BillingService {

    private final BillingRepository billingRepository;
    private final AppointmentService appointmentService;

    @Override
    @Transactional
    public Billing generateInvoiceForAppointment(Long appointmentId) {
        Appointment appointment = appointmentService.findById(appointmentId);
        
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot generate invoice for appointment that is not completed");
        }

        if (billingRepository.findByAppointmentId(appointmentId).isPresent()) {
            throw new IllegalStateException("Invoice already exists for this appointment");
        }

        BigDecimal amount = appointment.getDoctor().getConsultationFee();
        String invoiceNumber = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Billing billing = new Billing();
        billing.setAppointment(appointment);
        billing.setAmount(amount);
        billing.setBillingDate(LocalDateTime.now());
        billing.setInvoiceNumber(invoiceNumber);
        billing.setStatus(Billing.BillingStatus.PENDING);

        log.info("Generated invoice {} for appointment {}", invoiceNumber, appointmentId);
        return billingRepository.save(billing);
    }

    @Override
    public Billing getInvoiceByAppointmentId(Long appointmentId) {
        return billingRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Invoice not found for appointment: " + appointmentId));
    }

    @Override
    public Page<Billing> getInvoicesByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return billingRepository.findByDateRange(startDate, endDate, pageable);
    }

    @Override
    public Page<Billing> getInvoicesByStatus(Billing.BillingStatus status, Pageable pageable) {
        return billingRepository.findByStatus(status, pageable);
    }
}
