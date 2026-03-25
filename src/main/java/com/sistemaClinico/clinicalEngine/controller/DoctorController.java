package com.sistemaClinico.clinicalEngine.controller;

import com.sistemaClinico.clinicalEngine.dto.ApiResponse;
import com.sistemaClinico.clinicalEngine.entity.Doctor;
import com.sistemaClinico.clinicalEngine.service.DoctorService;
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
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Doctor>>> getDoctors(
            @RequestParam(required = false) String specialty,
            Pageable pageable) {
        if (specialty != null) {
            return ResponseEntity.ok(ApiResponse.success("Doctores obtenidos", doctorService.findBySpecialty(specialty, pageable)));
        }
        return ResponseEntity.ok(ApiResponse.success("Doctores obtenidos", doctorService.findAll(pageable)));
    }

    @GetMapping("/specialties")
    public ResponseEntity<ApiResponse<?>> getSpecialties() {
        return ResponseEntity.ok(ApiResponse.success("Especialidades obtenidas", doctorService.getSpecialties()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Doctor>> create(@Valid @RequestBody Doctor doctor) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Doctor añadido correctamente", doctorService.save(doctor)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Doctor>> update(@PathVariable Long id,
                                         @Valid @RequestBody Doctor doctor) {
        return ResponseEntity.ok(ApiResponse.success("Doctor actualizado correctamente",doctorService.update(id, doctor)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor eliminado correctamente", null));
    }
}