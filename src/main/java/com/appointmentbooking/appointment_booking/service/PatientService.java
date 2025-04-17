package com.appointmentbooking.appointment_booking.service;

import com.appointmentbooking.appointment_booking.dto.PatientDTO;
import com.appointmentbooking.appointment_booking.model.Patient;
import com.appointmentbooking.appointment_booking.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public Patient createPatient(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setFullName(dto.getFullName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        return repository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return repository.findAll();
    }
}