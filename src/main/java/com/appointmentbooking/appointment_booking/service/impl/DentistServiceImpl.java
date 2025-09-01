package com.appointmentbooking.appointment_booking.service.impl;

import com.appointmentbooking.appointment_booking.domain.Dentist;
import com.appointmentbooking.appointment_booking.exception.ResourceNotFoundException;
import com.appointmentbooking.appointment_booking.repository.DentistRepository;
import com.appointmentbooking.appointment_booking.service.DentistService;
import com.appointmentbooking.appointment_booking.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DentistServiceImpl implements DentistService {

    private final DentistRepository repository;

    @Override @Transactional
    public DentistDTO create(DentistCreateDTO dto) {
        var entity = Dentist.builder()
                .fullName(dto.fullName().trim())
                .specialty(dto.specialty() == null ? null : dto.specialty().trim())
                .build();
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Override @Transactional(readOnly = true)
    public DentistDTO get(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentist %d not found".formatted(id)));
        return toDTO(entity);
    }

    @Override @Transactional(readOnly = true)
    public Page<DentistDTO> search(String q, Pageable pageable) {
        Specification<Dentist> spec = Specification.where(null);
        if (q != null && !q.isBlank()) {
            String like = "%" + q.toLowerCase() + "%";
            spec = spec.and((root, cq, cb) -> cb.or(
                    cb.like(cb.lower(root.get("fullName")), like),
                    cb.like(cb.lower(root.get("specialty")), like)
            ));
        }
        return repository.findAll(spec, pageable).map(this::toDTO);
    }

    @Override @Transactional
    public DentistDTO update(Long id, DentistUpdateDTO dto) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentist %d not found".formatted(id)));
        entity.setFullName(dto.fullName().trim());
        entity.setSpecialty(dto.specialty() == null ? null : dto.specialty().trim());
        return toDTO(entity);
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Dentist %d not found".formatted(id));
        }
        repository.deleteById(id);
    }

    private DentistDTO toDTO(Dentist d) {
        return new DentistDTO(d.getId(), d.getFullName(), d.getSpecialty());
    }
}