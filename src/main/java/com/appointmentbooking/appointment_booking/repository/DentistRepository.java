package com.appointmentbooking.appointment_booking.repository;

import com.appointmentbooking.appointment_booking.domain.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DentistRepository extends JpaRepository<Dentist, Long>, JpaSpecificationExecutor<Dentist> {}