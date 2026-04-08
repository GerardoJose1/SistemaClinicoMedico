package com.sistemaClinico.clinicalEngine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDoctorRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La especialidad es obligatoria")
    private String specialty;

    private String availableHours;

    @NotNull(message = "La tarifa de consulta es obligatoria")
    private BigDecimal consultationFee;
}
