package com.appointmentbooking.appointment_booking.service.impl;

import com.appointmentbooking.appointment_booking.domain.*;
import com.appointmentbooking.appointment_booking.repository.*;
import com.appointmentbooking.appointment_booking.service.AppointmentService;
import com.appointmentbooking.appointment_booking.dto.AppointmentCreateDTO;
import com.appointmentbooking.appointment_booking.dto.AppointmentDTO;
import com.appointmentbooking.appointment_booking.exception.GlobalExceptionHandler; // For reference
import com.appointmentbooking.appointment_booking.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;

    @Override
    @Transactional
    public AppointmentDTO create(AppointmentCreateDTO dto) {
        if (!dto.startAt().isBefore(dto.endAt())) {
            throw new IllegalArgumentException("startAt must be before endAt");
        }

        var patient = patientRepository.findById(dto.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient %d not found".formatted(dto.patientId())));

        Dentist dentist = null;
        if (dto.dentistId() != null) {
            dentist = dentistRepository.findById(dto.dentistId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dentist %d not found".formatted(dto.dentistId())));
        }

        boolean overlap = appointmentRepository.existsOverlap(
                dto.dentistId(), dto.patientId(), dto.startAt(), dto.endAt(), null
        );
        if (overlap) {
            throw new OverlapException("There is a conflicting appointment in the selected time window");
        }

        var entity = Appointment.builder()
                .patient(patient)
                .dentist(dentist)
                .startAt(dto.startAt())
                .endAt(dto.endAt())
                .status(AppointmentStatus.SCHEDULED)
                .reason(dto.reason())
                .build();

        entity = appointmentRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDTO> search(Long patientId, Long dentistId, AppointmentStatus status,
                                       OffsetDateTime from, OffsetDateTime to, Pageable pageable) {
        Specification<Appointment> spec = Specification.where(null);

        if (patientId != null) spec = spec.and((r, q, cb) -> cb.equal(r.get("patient").get("id"), patientId));
        if (dentistId != null) spec = spec.and((r, q, cb) -> cb.equal(r.get("dentist").get("id"), dentistId));
        if (status != null) spec = spec.and((r, q, cb) -> cb.equal(r.get("status"), status));
        if (from != null) spec = spec.and((r, q, cb) -> cb.greaterThanOrEqualTo(r.get("startAt"), from));
        if (to != null) spec = spec.and((r, q, cb) -> cb.lessThanOrEqualTo(r.get("startAt"), to));

        return appointmentRepository.findAll(spec, pageable).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDTO get(Long id) {
        var entity = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment %d not found".formatted(id)));
        return toDTO(entity);
    }

    @Override
    @Transactional
    public AppointmentDTO updateStatus(Long id, AppointmentStatus status) {
        Objects.requireNonNull(status, "status must not be null");
        var entity = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment %d not found".formatted(id)));
        entity.setStatus(status);
        return toDTO(entity);
    }

    @Override
    @Transactional
    public void cancel(Long id, String reason) {
        var entity = appointmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment %d not found".formatted(id)));

        entity.setStatus(AppointmentStatus.CANCELLED);
        if (reason != null && !reason.isBlank()) {
            entity.setReason(reason);
        }
    }

    private AppointmentDTO toDTO(Appointment a) {
        return new AppointmentDTO(
                a.getId(),
                a.getPatient() != null ? a.getPatient().getId() : null,
                a.getDentist() != null ? a.getDentist().getId() : null,
                a.getStartAt(),
                a.getEndAt(),
                a.getStatus(),
                a.getReason()
        );
    }

    // Custom exception (inline to keep file short)
    public static class OverlapException extends RuntimeException {
        public OverlapException(String message) { super(message); }
    }
}