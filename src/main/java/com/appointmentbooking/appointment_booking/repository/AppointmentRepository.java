package com.appointmentbooking.appointment_booking.repository;

import com.appointmentbooking.appointment_booking.domain.Appointment;
import com.appointmentbooking.appointment_booking.domain.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

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

    List<Appointment> findByPatientIdAndStatusInAndStartAtAfterOrderByStartAtAsc(
            Long patientId,
            Collection<AppointmentStatus> statuses,
            OffsetDateTime startAt
    );
}
