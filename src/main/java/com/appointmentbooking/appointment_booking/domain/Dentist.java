package com.appointmentbooking.appointment_booking.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dentists")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Dentist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 120)
    private String fullName;

    @Column(length = 120)
    private String specialty;
}