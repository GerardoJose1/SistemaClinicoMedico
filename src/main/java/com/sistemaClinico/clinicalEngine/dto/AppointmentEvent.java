package com.sistemaClinico.clinicalEngine.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentEvent {
    private UUID appointmentId;
    private Long patientId;
    private String patientEmail;
    private String patientName;
    private String doctorName;
    private String specialty;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;
    
    private String status; // CONFIRMED | CANCELLED | COMPLETED
}
