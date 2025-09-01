package com.appointmentbooking.appointment_booking.repository;

import com.appointmentbooking.appointment_booking.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    /**
     * Overlap check: intervals intersect if (start < existing.end) AND (end > existing.start).
     * Touching endpoints (end == start) are NOT overlap.
     * Conflicts for same dentist (if provided) OR same patient.
     */
    @Query("""
    SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END
    FROM Appointment a
    WHERE a.status IN (
      com.appointmentbooking.appointment_booking.domain.AppointmentStatus.SCHEDULED,
      com.appointmentbooking.appointment_booking.domain.AppointmentStatus.CONFIRMED
    )
      AND (:excludeId IS NULL OR a.id <> :excludeId)
      AND (
            (:dentistId IS NOT NULL AND a.dentist.id = :dentistId)
            OR a.patient.id = :patientId
          )
      AND (:startAt < a.endAt AND :endAt > a.startAt)
  """)
    boolean existsOverlap(@Param("dentistId") Long dentistId,
                          @Param("patientId") Long patientId,
                          @Param("startAt") OffsetDateTime startAt,
                          @Param("endAt") OffsetDateTime endAt,
                          @Param("excludeId") Long excludeId);
}