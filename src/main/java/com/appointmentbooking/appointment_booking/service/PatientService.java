package com.appointmentbooking.appointment_booking.service;

import com.appointmentbooking.appointment_booking.dto.*;
import org.springframework.data.domain.*;

public interface PatientService {
    PatientDTO create(PatientCreateDTO dto);
    PatientDTO get(Long id);
    Page<PatientDTO> search(String q, Pageable pageable);
    PatientDTO update(Long id, PatientUpdateDTO dto);
    void delete(Long id);
}