package com.appointmentbooking.appointment_booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dentist read model")
public record DentistDTO(
        @Schema(example = "1") Long id,
        @Schema(example = "Dr. Juan López") String fullName,
        @Schema(example = "Orthodontics") String specialty
) {}
