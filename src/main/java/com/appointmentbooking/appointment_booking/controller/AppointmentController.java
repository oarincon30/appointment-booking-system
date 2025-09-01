package com.appointmentbooking.appointment_booking.controller;


import com.appointmentbooking.appointment_booking.domain.AppointmentStatus;
import com.appointmentbooking.appointment_booking.service.AppointmentService;
import com.appointmentbooking.appointment_booking.dto.AppointmentCreateDTO;
import com.appointmentbooking.appointment_booking.dto.AppointmentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
                      "startAt": "2025-09-01T14:00:00Z",
                      "endAt": "2025-09-01T14:30:00Z",
                      "reason": "Routine checkup and cleaning"
                    }"""),
                                    @ExampleObject(name = "Without dentist", value = """
                    {
                      "patientId": 2,
                      "startAt": "2025-09-02T09:00:00Z",
                      "endAt": "2025-09-02T09:20:00Z",
                      "reason": "Tooth pain evaluation"
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
            @Parameter(example = "1") @RequestParam(required = false) Long patientId,
            @Parameter(example = "1") @RequestParam(required = false) Long dentistId,
            @Parameter(example = "CONFIRMED") @RequestParam(required = false) AppointmentStatus status,
            @Parameter(example = "2025-09-01T00:00:00Z") @RequestParam(required = false) OffsetDateTime from,
            @Parameter(example = "2025-09-30T23:59:59Z") @RequestParam(required = false) OffsetDateTime to,
            @ParameterObject Pageable pageable) {
        return service.search(patientId, dentistId, status, from, to, pageable);
    }

    @Operation(summary = "Update status", description = "SCHEDULED | CONFIRMED | COMPLETED | CANCELLED | NO_SHOW")
    @PatchMapping("/{id}/status")
    public AppointmentDTO updateStatus(@PathVariable Long id,
                                       @Parameter(example = "CONFIRMED") @RequestParam AppointmentStatus status) {
        return service.updateStatus(id, status);
    }

    @Operation(summary = "Cancel appointment", description = "Sets status=CANCELLED and stores optional reason.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Canceled"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id,
                                       @Parameter(example = "Patient requested cancellation")
                                       @RequestParam(required = false) String reason) {
        service.cancel(id, reason);
        return ResponseEntity.noContent().build();
    }
}