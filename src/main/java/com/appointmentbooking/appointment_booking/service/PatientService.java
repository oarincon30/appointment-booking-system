package com.appointmentbooking.appointment_booking.service;

import com.appointmentbooking.appointment_booking.dto.PatientDTO;
import com.appointmentbooking.appointment_booking.model.Patient;
import com.appointmentbooking.appointment_booking.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // Crear paciente
    public Patient createPatient(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setFullName(dto.getFullName());
        patient.setEmail(dto.getEmail());
        patient.setPhone(dto.getPhone());
        return patientRepository.save(patient);
    }

    // Obtener todos los pacientes
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Actualizar paciente
    public Patient updatePatient(Long id, PatientDTO dto) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            patient.setFullName(dto.getFullName());
            patient.setEmail(dto.getEmail());
            patient.setPhone(dto.getPhone());
            return patientRepository.save(patient);
        }

        return null;
    }

    // Eliminar paciente
    public boolean deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}