package com.appointmentbooking.appointment_booking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Appointment Booking API")
                        .version("1.0.0")
                        .description("API for managing patient appointments")
                        .contact(new Contact()
                                .name("Oswaldo Rincón")
                                .email("oarincon30@ucatolica.edu.co")
                                .url("https://www.x.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}