package com.appointmentbooking.appointment_booking.controller;

import com.appointmentbooking.appointment_booking.service.DentistService;
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
@RequestMapping("/api/dentists")
@Tag(name = "Dentists", description = "Dentists CRUD")
@RequiredArgsConstructor
public class DentistController {

    private final DentistService service;

    @Operation(
            summary = "Create a dentist",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DentistCreateDTO.class),
                            examples = @ExampleObject("""
                {
                  "fullName": "Dr. Juan López",
                  "specialty": "Orthodontics"
                }
              """)
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(implementation = DentistDTO.class)))
    })
    @PostMapping
    public ResponseEntity<DentistDTO> create(
            @Valid @org.springframework.web.bind.annotation.RequestBody DentistCreateDTO dto) {

        var created = service.create(dto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Get dentist by id")
    @GetMapping("/{id}")
    public DentistDTO get(@PathVariable Long id) {
        return service.get(id);
    }

    @Operation(summary = "Search dentists (paged)",
            description = "Filter by `q` across fullName/specialty. Supports `page`, `size`, `sort`.")
    @GetMapping
    public Page<DentistDTO> search(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false, name = "q") String q) {
        return service.search(q, pageable);
    }

    @Operation(summary = "Update dentist")
    @PutMapping("/{id}")
    public DentistDTO update(@PathVariable Long id,
                             @Valid @org.springframework.web.bind.annotation.RequestBody DentistUpdateDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Delete dentist")
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