package com.sistemaClinico.clinicalEngine.controller;

import com.sistemaClinico.clinicalEngine.dto.ApiResponse;
import com.sistemaClinico.clinicalEngine.dto.CreateDoctorRequest;
import com.sistemaClinico.clinicalEngine.dto.UpdateDoctorRequest;
import com.sistemaClinico.clinicalEngine.entity.Doctor;
import com.sistemaClinico.clinicalEngine.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors", description = "Operaciones relacionadas con doctores")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    @Operation(
        summary = "Obtener lista de doctores",
        description = "Retorna una lista paginada de doctores. Puede filtrar por especialidad."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctores obtenidos exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = com.sistemaClinico.clinicalEngine.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public ResponseEntity<ApiResponse<Page<Doctor>>> getDoctors(
            @Parameter(hidden = true) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Doctores obtenidos", doctorService.findAll(pageable)));
    }

    @GetMapping("/specialties")
    @Operation(
        summary = "Obtener especialidades",
        description = "Retorna todas las especialidades disponibles de los doctores. Puede filtrar por especialidad específica."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Especialidades obtenidas",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = com.sistemaClinico.clinicalEngine.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parámetros inválidos")
    })
    public ResponseEntity<ApiResponse<?>> getSpecialties(
            @Parameter(description = "Filtrar por especialidad específica (opcional)", 
                   example = "Cardiología",
                   name = "specialty",
                   required = false)
            @RequestParam(required = false) String specialty,
            @Parameter(hidden = true) Pageable pageable) {
        if (specialty != null) {
            return ResponseEntity.ok(ApiResponse.success("Doctores filtrados por especialidad", doctorService.findBySpecialty(specialty, pageable)));
        }
        return ResponseEntity.ok(ApiResponse.success("Especialidades obtenidas", doctorService.getSpecialties()));
    }

    @PostMapping
    @Operation(
        summary = "Crear doctor",
        description = "Crea un nuevo doctor en el sistema."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Doctor creado exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = com.sistemaClinico.clinicalEngine.dto.ApiResponse.class),
                examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                    value = "{\"success\":true,\"message\":\"Doctor añadido correctamente\",\"data\":{...doctor...}}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error al crear doctor")
    })
    public ResponseEntity<ApiResponse<Doctor>> create(
            @Valid @RequestBody CreateDoctorRequest doctorRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Doctor añadido correctamente", doctorService.create(doctorRequest)));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar doctor",
        description = "Actualiza los datos de un doctor existente."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctor actualizado exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = com.sistemaClinico.clinicalEngine.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Doctor no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error al actualizar doctor")
    })
    public ResponseEntity<ApiResponse<Doctor>> update(
            @Parameter(description = "ID del doctor a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateDoctorRequest doctorRequest) {
        return ResponseEntity.ok(ApiResponse.success("Doctor actualizado correctamente",doctorService.update(id, doctorRequest)));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar doctor",
        description = "Elimina un doctor del sistema."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctor eliminado exitosamente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = com.sistemaClinico.clinicalEngine.dto.ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Doctor no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error al eliminar doctor")
    })
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "ID del doctor a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor eliminado correctamente", null));
    }
}