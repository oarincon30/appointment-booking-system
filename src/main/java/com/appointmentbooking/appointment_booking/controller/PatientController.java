package com.appointmentbooking.appointment_booking.controller;

import com.appointmentbooking.appointment_booking.service.PatientService;
import com.appointmentbooking.appointment_booking.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "Patients CRUD")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService service;

    @Operation(
            summary = "Create a patient",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PatientCreateDTO.class),
                            examples = @ExampleObject("""
                {
                  "fullName": "María Pérez",
                  "email": "maria.perez@example.com",
                  "phone": "+57 3001234567"
                }
              """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    public ResponseEntity<PatientDTO> create(
            @Valid @org.springframework.web.bind.annotation.RequestBody PatientCreateDTO dto) {

        var created = service.create(dto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Get patient by id")
    @GetMapping("/{id}")
    public PatientDTO get(@PathVariable Long id) {
        return service.get(id);
    }

    @Operation(summary = "Search patients (paged)",
            description = "Filter by `q` (matches name/email/phone). Supports `page`, `size`, `sort`.")
    @GetMapping
    public Page<PatientDTO> search(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false, name = "q") String q) {
        return service.search(q, pageable);
    }

    @Operation(summary = "Update patient")
    @PutMapping("/{id}")
    public PatientDTO update(@PathVariable Long id,
                             @Valid @org.springframework.web.bind.annotation.RequestBody PatientUpdateDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Delete patient")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
