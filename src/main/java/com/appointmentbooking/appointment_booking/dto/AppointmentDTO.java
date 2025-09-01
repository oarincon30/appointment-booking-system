package com.appointmentbooking.appointment_booking.dto;

import com.appointmentbooking.appointment_booking.domain.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Appointment read model")
public record AppointmentDTO(
        @Schema(example = "42") Long id,
        @Schema(example = "1") Long patientId,
        @Schema(example = "1") Long dentistId,
        @Schema(example = "2025-09-01T14:00:00Z") OffsetDateTime startAt,
        @Schema(example = "2025-09-01T14:30:00Z") OffsetDateTime endAt,
        @Schema(implementation = AppointmentStatus.class, example = "SCHEDULED") AppointmentStatus status,
        @Schema(example = "Routine checkup and cleaning") String reason
) {}