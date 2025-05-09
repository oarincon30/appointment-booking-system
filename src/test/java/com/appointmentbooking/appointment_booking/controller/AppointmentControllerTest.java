package com.appointmentbooking.appointment_booking.controller;

import com.appointmentbooking.appointment_booking.dto.AppointmentDTO;
import com.appointmentbooking.appointment_booking.model.Appointment;
import com.appointmentbooking.appointment_booking.model.Patient;
import com.appointmentbooking.appointment_booking.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientRepository patientRepository;

    private Long testPatientId;

    @BeforeEach
    void setUp() {
        // Ensure a patient exists before testing appointments
        Patient patient = new Patient();
        patient.setFullName("Test Patient");
        patient.setEmail("test@patient.com");
        patient.setPhone("1234567890");
        testPatientId = patientRepository.save(patient).getId();
    }

    @Test
    void shouldCreateAppointment() throws Exception {
        AppointmentDTO dto = new AppointmentDTO(
                LocalDateTime.now().plusDays(1),
                "Initial Checkup",
                testPatientId
        );

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Initial Checkup"));
    }

    @Test
    void shouldGetAllAppointments() throws Exception {
        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldUpdateAppointment() throws Exception {
        // Create appointment first
        AppointmentDTO dto = new AppointmentDTO(
                LocalDateTime.now().plusDays(2),
                "Original Reason",
                testPatientId
        );

        String response = mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Appointment created = objectMapper.readValue(response, Appointment.class);

        // Update reason
        created.setReason("Updated Reason");

        AppointmentDTO updateDto = new AppointmentDTO(
                created.getDateTime(),
                "Updated Reason",
                testPatientId
        );

        mockMvc.perform(put("/api/appointments/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason").value("Updated Reason"));
    }

    @Test
    void shouldDeleteAppointment() throws Exception {
        AppointmentDTO dto = new AppointmentDTO(
                LocalDateTime.now().plusDays(3),
                "To be deleted",
                testPatientId
        );

        String response = mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Appointment created = objectMapper.readValue(response, Appointment.class);

        mockMvc.perform(delete("/api/appointments/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/appointments/" + created.getId()))
                .andExpect(status().isNotFound());
    }
}