package com.appointmentbooking.appointment_booking;

import com.appointmentbooking.appointment_booking.model.Patient;
import com.appointmentbooking.appointment_booking.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppointmentBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentBookingApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(PatientRepository patientRepository) {
		return args -> {
			Patient patient = new Patient();
			patient.setFullName("John Doe");
			patient.setEmail("john@example.com");
			patient.setPhone("1234567890");

			patientRepository.save(patient);

			System.out.println("✔️ Patient saved to DB!");
		};
	}
}