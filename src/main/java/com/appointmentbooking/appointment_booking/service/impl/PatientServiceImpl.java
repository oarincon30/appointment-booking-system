package com.appointmentbooking.appointment_booking.service.impl;

import com.appointmentbooking.appointment_booking.domain.Patient;
import com.appointmentbooking.appointment_booking.exception.ResourceNotFoundException;
import com.appointmentbooking.appointment_booking.repository.PatientRepository;
import com.appointmentbooking.appointment_booking.service.PatientService;
import com.appointmentbooking.appointment_booking.dto.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;

    @Override @Transactional
    public PatientDTO create(PatientCreateDTO dto) {
        var entity = Patient.builder()
                .fullName(dto.fullName().trim())
                .email(dto.email().trim())
                .phone(dto.phone() == null ? null : dto.phone().trim())
                .build();
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Override @Transactional(readOnly = true)
    public PatientDTO get(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient %d not found".formatted(id)));
        return toDTO(entity);
    }

    @Override @Transactional(readOnly = true)
    public Page<PatientDTO> search(String q, Pageable pageable) {
        Specification<Patient> spec = Specification.where(null);
        if (q != null && !q.isBlank()) {
            String like = "%" + q.toLowerCase() + "%";
            spec = spec.and((root, cq, cb) -> cb.or(
                    cb.like(cb.lower(root.get("fullName")), like),
                    cb.like(cb.lower(root.get("email")), like),
                    cb.like(cb.lower(root.get("phone")), like)
            ));
        }
        return repository.findAll(spec, pageable).map(this::toDTO);
    }

    @Override @Transactional
    public PatientDTO update(Long id, PatientUpdateDTO dto) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient %d not found".formatted(id)));
        entity.setFullName(dto.fullName().trim());
        entity.setEmail(dto.email().trim());
        entity.setPhone(dto.phone() == null ? null : dto.phone().trim());
        return toDTO(entity);
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Patient %d not found".formatted(id));
        }
        repository.deleteById(id);
    }

    private PatientDTO toDTO(Patient p) {
        return new PatientDTO(p.getId(), p.getFullName(), p.getEmail(), p.getPhone());
    }
}