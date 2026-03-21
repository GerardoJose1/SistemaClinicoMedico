package com.sistemaClinico.clinicalEngine.controller;

import com.sistemaClinico.clinicalEngine.entity.Doctor;
import com.sistemaClinico.clinicalEngine.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<Page<Doctor>> getDoctors(
            @RequestParam(required = false) String specialty,
            Pageable pageable) {
        if (specialty != null) {
            return ResponseEntity.ok(doctorService.findBySpecialty(specialty, pageable));
        }
        return ResponseEntity.ok(doctorService.findAll(pageable));
    }

    @GetMapping("/specialties")
    public ResponseEntity<?> getSpecialties() {
        return ResponseEntity.ok(doctorService.getSpecialties());
    }

    @PostMapping
    public ResponseEntity<Doctor> create(@Valid @RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.save(doctor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> update(@PathVariable Long id,
                                         @Valid @RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.update(id, doctor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}