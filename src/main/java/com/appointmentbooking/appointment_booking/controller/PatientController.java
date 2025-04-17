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

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody PatientDTO dto) {
        Patient created = service.createPatient(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(service.getAllPatients());
    }
}