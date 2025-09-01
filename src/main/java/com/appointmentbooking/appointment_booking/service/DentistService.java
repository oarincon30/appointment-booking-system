package com.appointmentbooking.appointment_booking.service;

import com.appointmentbooking.appointment_booking.dto.*;
import org.springframework.data.domain.*;

public interface DentistService {
    DentistDTO create(DentistCreateDTO dto);
    DentistDTO get(Long id);
    Page<DentistDTO> search(String q, Pageable pageable);
    DentistDTO update(Long id, DentistUpdateDTO dto);
    void delete(Long id);
}