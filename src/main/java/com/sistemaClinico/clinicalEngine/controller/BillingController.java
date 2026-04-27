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
@Tag(name = "Billing", description = "Billing management endpoints")
public class BillingController {

    private final BillingService billingService;

    @GetMapping("/{appointmentId}")
    @Operation(summary = "Get invoice by appointment ID", description = "Retrieves the invoice for a specific appointment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice found"),
        @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    public ResponseEntity<Billing> getInvoiceByAppointmentId(
            @Parameter(description = "Appointment ID") @PathVariable Long appointmentId) {
        Billing billing = billingService.getInvoiceByAppointmentId(appointmentId);
        return ResponseEntity.ok(billing);
    }

    @PostMapping("/{appointmentId}/generate")
    @Operation(summary = "Generate invoice for appointment", description = "Generates a new invoice for a completed appointment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice generated successfully"),
        @ApiResponse(responseCode = "400", description = "Appointment not completed or invoice already exists")
    })
    public ResponseEntity<Billing> generateInvoice(
            @Parameter(description = "Appointment ID") @PathVariable Long appointmentId) {
        Billing billing = billingService.generateInvoiceForAppointment(appointmentId);
        return ResponseEntity.ok(billing);
    }

    @GetMapping("/reports")
    @Operation(summary = "Get invoices by date range", description = "Retrieves paginated invoices within a date range")
    public ResponseEntity<java.util.List<Billing>> getInvoicesByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Billing> billingPage = billingService.getInvoicesByDateRange(startDate, endDate, pageable);
        
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(billingPage.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(billingPage.getTotalPages()))
                .body(billingPage.getContent());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get invoices by status", description = "Retrieves paginated invoices by billing status")
    public ResponseEntity<java.util.List<Billing>> getInvoicesByStatus(
            @Parameter(description = "Billing status") @PathVariable Billing.BillingStatus status,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Billing> billingPage = billingService.getInvoicesByStatus(status, pageable);
        
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(billingPage.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(billingPage.getTotalPages()))
                .body(billingPage.getContent());
    }
}
