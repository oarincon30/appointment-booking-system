package com.appointmentbooking.appointment_booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.OffsetDateTime;

@Schema(description = "Payload to create a new appointment")
public record AppointmentCreateDTO(
        @NotNull @Schema(example = "1", description = "Existing patient id") Long patientId,
        @Schema(example = "1", description = "Optional dentist id") Long dentistId,
        @NotNull @FutureOrPresent
        @Schema(example = "2025-09-01T14:00:00Z", description = "Start (ISO-8601)") OffsetDateTime startAt,
        @NotNull @FutureOrPresent
        @Schema(example = "2025-09-01T14:30:00Z", description = "End (ISO-8601)") OffsetDateTime endAt,
        @Size(max = 500) @Schema(example = "Routine checkup and cleaning") String reason
) {}
