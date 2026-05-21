package com.appointmentbooking.appointment_booking.controller;

import com.appointmentbooking.appointment_booking.domain.AppointmentStatus;
import com.appointmentbooking.appointment_booking.dto.AppointmentCancelDTO;
import com.appointmentbooking.appointment_booking.dto.AppointmentCreateDTO;
import com.appointmentbooking.appointment_booking.dto.AppointmentDTO;
import com.appointmentbooking.appointment_booking.dto.AppointmentRescheduleDTO;
import com.appointmentbooking.appointment_booking.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "Dental appointments management")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService service;

    @Operation(
            summary = "Create a new appointment",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentCreateDTO.class),
                            examples = {
                                    @ExampleObject(name = "Basic", value = """
                    {
                      "patientId": 1,
                      "dentistId": 1,
                      "startAt": "2026-04-02T14:00:00Z",
                      "endAt": "2026-04-02T14:30:00Z",
                      "reason": "Control general"
                    }"""),
                                    @ExampleObject(name = "Without dentist", value = """
                    {
                      "patientId": 2,
                      "startAt": "2026-04-03T09:00:00Z",
                      "endAt": "2026-04-03T09:30:00Z",
                      "reason": "Agendado desde WhatsApp"
                    }""")
                            })
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Patient/Dentist not found"),
            @ApiResponse(responseCode = "409", description = "Appointment overlap")
    })
    @PostMapping
    public ResponseEntity<AppointmentDTO> create(@Valid @RequestBody AppointmentCreateDTO dto) {
        var created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get appointment by id")
    @GetMapping("/{id}")
    public AppointmentDTO get(@PathVariable Long id) { return service.get(id); }

    @Operation(
            summary = "Search appointments (paged)",
            description = "Filter by patientId, dentistId, status and time window [from, to]. Use `page`, `size`, `sort`."
    )
    @GetMapping
    public Page<AppointmentDTO> search(
            @Parameter(example = "4") @RequestParam(required = false) Long patientId,
            @Parameter(example = "1") @RequestParam(required = false) Long dentistId,
            @Parameter(example = "SCHEDULED") @RequestParam(required = false) AppointmentStatus status,
            @Parameter(example = "2026-04-01T00:00:00Z") @RequestParam(required = false) OffsetDateTime from,
            @Parameter(example = "2026-06-30T23:59:59Z") @RequestParam(required = false) OffsetDateTime to,
            @ParameterObject Pageable pageable) {
        return service.search(patientId, dentistId, status, from, to, pageable);
    }

    @Operation(
            summary = "Reschedule appointment",
            description = "Updates the date/time of an active appointment and optionally changes the dentist.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentRescheduleDTO.class),
                            examples = @ExampleObject(name = "Reschedule", value = """
                    {
                      "patientId": 1,
                      "dentistId": 4,
                      "startAt": "2026-05-08T14:00:00Z",
                      "endAt": "2026-05-08T14:30:00Z",
                      "reason": "Reprogramado desde WhatsApp"
                    }"""))
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rescheduled",
                    content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Appointment/Patient/Dentist not found"),
            @ApiResponse(responseCode = "409", description = "Appointment overlap")
    })
    @PatchMapping("/{id}/reschedule")
    public AppointmentDTO reschedule(@PathVariable Long id,
                                     @Valid @RequestBody AppointmentRescheduleDTO dto) {
        return service.reschedule(id, dto);
    }

    @Operation(summary = "Update status", description = "SCHEDULED | CONFIRMED | COMPLETED | CANCELLED | NO_SHOW")
    @PatchMapping("/{id}/status")
    public AppointmentDTO updateStatus(@PathVariable Long id,
                                       @Parameter(example = "CONFIRMED") @RequestParam AppointmentStatus status) {
        return service.updateStatus(id, status);
    }

    @Operation(summary = "Cancel appointment", description = "Sets status=CANCELLED and stores cancellation trace data.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Canceled",
                    content = @Content(schema = @Schema(implementation = AppointmentDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PatchMapping("/{id}/cancel")
    public AppointmentDTO cancel(@PathVariable Long id,
                                 @Valid @RequestBody(required = false) AppointmentCancelDTO dto) {
        return service.cancel(id, dto);
    }
}
