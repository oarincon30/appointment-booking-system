package com.appointmentbooking.appointment_booking.service;

import com.appointmentbooking.appointment_booking.dto.AppointmentDTO;
import com.appointmentbooking.appointment_booking.exception.ResourceNotFoundException;
import com.appointmentbooking.appointment_booking.model.Appointment;
import com.appointmentbooking.appointment_booking.model.Patient;
import com.appointmentbooking.appointment_booking.repository.AppointmentRepository;
import com.appointmentbooking.appointment_booking.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    public Appointment createAppointment(AppointmentDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + dto.getPatientId()));

        Appointment appointment = new Appointment();
        appointment.setDateTime(dto.getDateTime());
        appointment.setReason(dto.getReason());
        appointment.setPatient(patient);

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
    }

    public Appointment updateAppointment(Long id, AppointmentDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + dto.getPatientId()));

        appointment.setDateTime(dto.getDateTime());
        appointment.setReason(dto.getReason());
        appointment.setPatient(patient);

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found with ID: " + id);
        }
        appointmentRepository.deleteById(id);
    }
}