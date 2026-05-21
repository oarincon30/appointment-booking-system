package com.appointmentbooking.appointment_booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Patient read model")
public record PatientDTO(
        @Schema(example = "1") Long id,
        @Schema(example = "María Pérez") String fullName,
        @Schema(example = "maria.perez@example.com") String email,
        @Schema(example = "573001234567") String whatsappPhone,
        @Schema(example = "573001234567") String phone
) {}
