package com.appointmentbooking.appointment_booking.service;

import com.appointmentbooking.appointment_booking.domain.AppointmentStatus;
import com.appointmentbooking.appointment_booking.dto.AppointmentCreateDTO;
import com.appointmentbooking.appointment_booking.dto.AppointmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;

public interface AppointmentService {
    AppointmentDTO create(AppointmentCreateDTO dto);
    Page<AppointmentDTO> search(Long patientId, Long dentistId, AppointmentStatus status,
                                OffsetDateTime from, OffsetDateTime to, Pageable pageable);
    AppointmentDTO get(Long id);
    AppointmentDTO updateStatus(Long id, AppointmentStatus status);
    void cancel(Long id, String reason);
}