package com.appointmentbooking.appointment_booking.controller;

import com.appointmentbooking.appointment_booking.dto.PatientDTO;
import com.appointmentbooking.appointment_booking.model.Patient;
import com.appointmentbooking.appointment_booking.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    // Create a patient
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody PatientDTO dto) {
        Patient created = service.createPatient(dto);
        return ResponseEntity.ok(created);
    }

    // Get all patients
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(service.getAllPatients());
    }

    // Update a patient
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody PatientDTO updatedDto) {
        Patient updated = service.updatePatient(id, updatedDto);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a patient
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        boolean deleted = service.deletePatient(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}