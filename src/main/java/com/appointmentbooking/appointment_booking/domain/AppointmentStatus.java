package com.appointmentbooking.appointment_booking.domain;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Appointment lifecycle status")
public enum AppointmentStatus {
    SCHEDULED, CONFIRMED, COMPLETED, CANCELLED, NO_SHOW
}