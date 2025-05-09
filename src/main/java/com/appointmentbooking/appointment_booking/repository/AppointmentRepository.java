package com.appointmentbooking.appointment_booking.repository;

import com.appointmentbooking.appointment_booking.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // You can add custom query methods here if needed
}