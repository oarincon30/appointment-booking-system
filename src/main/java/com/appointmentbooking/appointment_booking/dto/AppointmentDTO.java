package com.appointmentbooking.appointment_booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AppointmentDTO {

    @NotNull(message = "Date and time are required")
    @Future(message = "The appointment must be in the future")
    private LocalDateTime dateTime;

    @NotBlank(message = "Reason is required")
    private String reason;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    public AppointmentDTO() {}

    public AppointmentDTO(LocalDateTime dateTime, String reason, Long patientId) {
        this.dateTime = dateTime;
        this.reason = reason;
        this.patientId = patientId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}