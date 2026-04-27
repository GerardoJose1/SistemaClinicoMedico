package com.sistemaClinico.clinicalEngine.service;

import com.sistemaClinico.clinicalEngine.config.RabbitMQConfig;
import com.sistemaClinico.clinicalEngine.dto.AppointmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishAppointmentConfirmed(AppointmentEvent event) {
        log.info("Publishing appointment confirmed event: {}", event.getAppointmentId());
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.APPOINTMENTS_EXCHANGE,
            RabbitMQConfig.APPOINTMENT_CONFIRMED_ROUTING_KEY,
            event
        );
    }

    public void publishAppointmentStatusChanged(AppointmentEvent event) {
        log.info("Publishing appointment status changed event: {} - {}", event.getAppointmentId(), event.getStatus());
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.APPOINTMENTS_EXCHANGE,
            RabbitMQConfig.APPOINTMENT_STATUS_CHANGED_ROUTING_KEY,
            event
        );
    }

    public void publishOrderCancelled(AppointmentEvent event) {
        log.info("Publishing order cancelled event: {}", event.getAppointmentId());
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.APPOINTMENTS_EXCHANGE,
            RabbitMQConfig.ORDER_CANCELLED_ROUTING_KEY,
            event
        );
    }
}
