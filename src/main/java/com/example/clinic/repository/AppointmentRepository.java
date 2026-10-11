package com.example.clinic.repository;

import com.example.clinic.entity.Appointment;
import com.example.clinic.entity.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);

    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(
            LocalDate start, LocalDate end);

    boolean existsByDoctorDoctorIdAndAppointmentDateAndStatusNot(
            Long doctorId, LocalDate appointmentDate, AppointmentStatus status);

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countByStatus();

    @Query("SELECT o.roomNumber, COUNT(a) " +
           "FROM Appointment a " +
           "JOIN a.doctor d " +
           "JOIN d.office o " +
           "GROUP BY o.roomNumber " +
           "ORDER BY COUNT(a) DESC")
    List<Object[]> findBusiestOffice();

    @Modifying
    @Query("UPDATE Appointment a SET a.status = :cancelled " +
           "WHERE a.doctor.doctorId = :doctorId " +
           "AND a.appointmentDate = :date " +
           "AND a.status <> :completed")
    int cancelDayForDoctor(@Param("doctorId") Long doctorId,
                           @Param("date") LocalDate date,
                           @Param("cancelled") AppointmentStatus cancelled,
                           @Param("completed") AppointmentStatus completed);

    @Modifying
    @Query("DELETE FROM Appointment a " +
           "WHERE a.status = :cancelled AND a.appointmentDate < :date")
    int deleteCancelledBefore(@Param("date") LocalDate date,
                              @Param("cancelled") AppointmentStatus cancelled);

    Page<Appointment> findAll(Pageable pageable);
}
