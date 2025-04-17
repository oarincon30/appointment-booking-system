package com.appointmentbooking.appointment_booking.repository;

import com.appointmentbooking.appointment_booking.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}