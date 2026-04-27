package com.sistemaClinico.clinicalEngine.listener;

import com.sistemaClinico.clinicalEngine.event.AppointmentCompletedEvent;
import com.sistemaClinico.clinicalEngine.service.BillingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BillingEventListener {

    private final BillingService billingService;

    @EventListener
    @Async
    public void handleAppointmentCompleted(AppointmentCompletedEvent event) {
        try {
            billingService.generateInvoiceForAppointment(event.getAppointmentId());
            log.info("Invoice generated for completed appointment: {}", event.getAppointmentId());
        } catch (Exception e) {
            log.error("Failed to generate invoice for appointment {}: {}", 
                    event.getAppointmentId(), e.getMessage());
            // Don't re-throw - billing failure shouldn't affect the appointment flow
        }
    }
}
