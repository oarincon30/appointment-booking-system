package com.appointmentbooking.appointment_booking.controller;

import com.appointmentbooking.appointment_booking.model.Patient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateNewPatient() throws Exception {
        Patient patient = new Patient(null, "John Doe", "john.doe@example.com", "1234567890");

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("1234567890"));
    }

    @Test
    void shouldReturnPatientById() throws Exception {
        // Create a patient
        Patient patient = new Patient(null, "Alice Johnson", "alice@example.com", "9876543210");

        String response = mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Patient createdPatient = objectMapper.readValue(response, Patient.class);

        // Get the patient by ID
        mockMvc.perform(get("/api/patients/" + createdPatient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Alice Johnson"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.phone").value("9876543210"));
    }

    @Test
    void shouldReturnPatientsList() throws Exception {
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldUpdateExistingPatient() throws Exception {
        // Create a patient to update
        Patient patient = new Patient();
        patient.setFullName("John Doe");
        patient.setEmail("john@example.com");
        patient.setPhone("123456789");

        String response = mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Patient createdPatient = objectMapper.readValue(response, Patient.class);

        // Update the patient's name
        createdPatient.setFullName("Jane Doe");

        mockMvc.perform(put("/api/patients/" + createdPatient.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Jane Doe"));
    }

    @Test
    void shouldDeletePatient() throws Exception {
        // Create a patient to delete
        Patient patient = new Patient();
        patient.setFullName("To Delete");
        patient.setEmail("delete@example.com");
        patient.setPhone("999999999");

        String response = mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Patient createdPatient = objectMapper.readValue(response, Patient.class);

        // Delete the patient
        mockMvc.perform(delete("/api/patients/" + createdPatient.getId()))
                .andExpect(status().isNoContent());

        // Confirm the patient no longer exists
        mockMvc.perform(get("/api/patients/" + createdPatient.getId()))
                .andExpect(status().isNotFound());
    }
}