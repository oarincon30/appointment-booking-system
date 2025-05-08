package com.appointmentbooking.appointment_booking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Constructors
    public Appointment() {}

    public Appointment(LocalDateTime dateTime, String reason, Patient patient) {
        this.dateTime = dateTime;
        this.reason = reason;
        this.patient = patient;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
}