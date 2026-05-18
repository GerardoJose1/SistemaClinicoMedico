package com.sistemaClinico.clinicalEngine.controller;

import com.sistemaClinico.clinicalEngine.entity.Billing;
import com.sistemaClinico.clinicalEngine.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
@Tag(name = "Facturación", description = "Endpoints para gestión de facturación")
public class BillingController {

    private final BillingService billingService;

    @GetMapping("/{appointmentId}")
    @Operation(summary = "Obtener factura por ID de cita", description = "Recupera la factura para una cita específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura encontrada"),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    public ResponseEntity<Billing> getInvoiceByAppointmentId(
            @Parameter(description = "ID de la cita") @PathVariable Long appointmentId) {
        Billing billing = billingService.getInvoiceByAppointmentId(appointmentId);
        return ResponseEntity.ok(billing);
    }

    @PostMapping("/{appointmentId}/generate")
    @Operation(summary = "Generar factura para cita", description = "Genera una nueva factura para una cita completada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Factura generada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Cita no completada o factura ya existe")
    })
    public ResponseEntity<Billing> generateInvoice(
            @Parameter(description = "ID de la cita") @PathVariable Long appointmentId) {
        Billing billing = billingService.generateInvoiceForAppointment(appointmentId);
        return ResponseEntity.ok(billing);
    }

    @GetMapping("/reports")
    @Operation(summary = "Obtener facturas por rango de fechas", description = "Recupera facturas paginadas dentro de un rango de fechas")
    public ResponseEntity<java.util.List<Billing>> getInvoicesByDateRange(
            @Parameter(description = "Fecha de inicio (yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Fecha de fin (yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Número de página (basado en 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size) {
        
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Billing> billingPage = billingService.getInvoicesByDateRange(startDate, endDate, pageable);
        
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(billingPage.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(billingPage.getTotalPages()))
                .body(billingPage.getContent());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Obtener facturas por estado", description = "Recupera facturas paginadas por estado de facturación")
    public ResponseEntity<java.util.List<Billing>> getInvoicesByStatus(
            @Parameter(description = "Estado de facturación") @PathVariable Billing.BillingStatus status,
            @Parameter(description = "Número de página (basado en 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "10") int size) {
        
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Billing> billingPage = billingService.getInvoicesByStatus(status, pageable);
        
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(billingPage.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(billingPage.getTotalPages()))
                .body(billingPage.getContent());
    }
}
