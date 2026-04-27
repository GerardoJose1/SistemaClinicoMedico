package com.sistemaClinico.clinicalEngine.event;

import lombok.Getter;

@Getter
public class AppointmentCompletedEvent {
    private final Long appointmentId;

    public AppointmentCompletedEvent(Long appointmentId) {
        this.appointmentId = appointmentId;
    }
}
