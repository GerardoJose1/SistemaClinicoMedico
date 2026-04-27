package com.sistemaClinico.clinicalEngine.service;

import com.sistemaClinico.clinicalEngine.dto.AppointmentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Primary
@Profile("test")
@Slf4j
public class MockAppointmentEventPublisher extends AppointmentEventPublisher {

    public MockAppointmentEventPublisher() {
        super(null);
    }

    @Override
    public void publishAppointmentConfirmed(AppointmentEvent event) {
        log.info("📅 [MOCK] Appointment confirmed event published: {}", event.getAppointmentId());
    }

    @Override
    public void publishAppointmentStatusChanged(AppointmentEvent event) {
        log.info("🔄 [MOCK] Appointment status changed event published: {} - {}", event.getAppointmentId(), event.getStatus());
    }

    @Override
    public void publishOrderCancelled(AppointmentEvent event) {
        log.info("❌ [MOCK] Order cancelled event published: {}", event.getAppointmentId());
    }
}
