package com.appointmentbooking.appointment_booking.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload to create a new dentist")
public record DentistCreateDTO(
        @NotBlank @Size(max = 120) @Schema(example = "Dr. Juan López") String fullName,
        @Size(max = 120) @Schema(example = "Orthodontics") String specialty
) {}