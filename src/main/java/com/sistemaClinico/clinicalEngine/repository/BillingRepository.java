package com.sistemaClinico.clinicalEngine.repository;

import com.sistemaClinico.clinicalEngine.entity.Billing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

    Optional<Billing> findByAppointmentId(Long appointmentId);

    @Query("SELECT b FROM Billing b WHERE b.billingDate BETWEEN :startDate AND :endDate ORDER BY b.billingDate DESC")
    Page<Billing> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                  @Param("endDate") LocalDateTime endDate, 
                                  Pageable pageable);

    @Query("SELECT b FROM Billing b WHERE b.status = :status ORDER BY b.billingDate DESC")
    Page<Billing> findByStatus(@Param("status") Billing.BillingStatus status, Pageable pageable);
}
