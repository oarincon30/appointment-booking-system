package com.appointmentbooking.appointment_booking.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload to update an existing patient")
public record PatientUpdateDTO(
        @NotBlank @Size(max = 120) @Schema(example = "María Pérez") String fullName,
        @NotBlank @Email @Size(max = 160) @Schema(example = "maria.perez@example.com") String email,
        @Size(max = 32) @Schema(example = "+57 3001234567") String phone
) {}