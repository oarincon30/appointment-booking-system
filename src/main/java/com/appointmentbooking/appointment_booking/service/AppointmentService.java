package com.appointmentbooking.appointment_booking.service;

import com.appointmentbooking.appointment_booking.domain.AppointmentStatus;
import com.appointmentbooking.appointment_booking.dto.AppointmentCancelDTO;
import com.appointmentbooking.appointment_booking.dto.AppointmentCreateDTO;
import com.appointmentbooking.appointment_booking.dto.AppointmentDTO;
import com.appointmentbooking.appointment_booking.dto.AppointmentRescheduleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

public interface AppointmentService {
    AppointmentDTO create(AppointmentCreateDTO dto);
    Page<AppointmentDTO> search(Long patientId, Long dentistId, AppointmentStatus status,
                                OffsetDateTime from, OffsetDateTime to, Pageable pageable);
    List<AppointmentDTO> listActiveFutureByPatient(Long patientId);
    AppointmentDTO get(Long id);
    AppointmentDTO updateStatus(Long id, AppointmentStatus status);
    AppointmentDTO cancel(Long id, AppointmentCancelDTO dto);
    AppointmentDTO reschedule(Long id, AppointmentRescheduleDTO dto);
}
