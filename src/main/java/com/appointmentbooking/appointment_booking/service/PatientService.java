package com.appointmentbooking.appointment_booking.service;

import com.appointmentbooking.appointment_booking.dto.PatientDTO;
import com.appointmentbooking.appointment_booking.model.Patient;
import com.appointmentbooking.appointment_booking.repository.PatientRepository;
import com.appointmentbooking.appointment_booking.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Patient getPatientById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + id));
    }

    public Patient updatePatient(Long id, PatientDTO dto) {
        Patient patient = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot update. Patient not found with ID: " + id));

        patient.setFullName(dto.getFullName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        return repository.save(patient);
    }

    public void deletePatient(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Patient not found with ID: " + id);
        }
        repository.deleteById(id);
    }


}