package com.appointmentbooking.appointment_booking.repository;

import com.appointmentbooking.appointment_booking.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
    Optional<Patient> findByWhatsappPhone(String whatsappPhone);
    boolean existsByWhatsappPhone(String whatsappPhone);
}
