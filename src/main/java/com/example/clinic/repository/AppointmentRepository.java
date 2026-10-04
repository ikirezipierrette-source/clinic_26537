package com.example.clinic.repository;

import com.example.clinic.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByStatusOrderByDateAsc(String status);

    List<Appointment> findByDateBetweenOrderByDateAsc(LocalDate start, LocalDate end);

    boolean existsByDoctorDoctorIdAndDateAndStatusNot(Long doctorId, LocalDate date, String status);

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countAppointmentsPerStatus();

    @Query("SELECT o.roomNumber, COUNT(a) FROM Appointment a JOIN a.doctor d JOIN d.office o GROUP BY o.roomNumber ORDER BY COUNT(a) DESC")
    List<Object[]> findBusiestOffice();

    @Modifying
    @Query("UPDATE Appointment a SET a.status = 'CANCELLED' WHERE a.doctor.doctorId = :doctorId AND a.date = :date AND a.status <> 'COMPLETED'")
    int cancelDayForDoctor(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);

    @Modifying
    @Query("DELETE FROM Appointment a WHERE a.status = 'CANCELLED' AND a.date < :date")
    int deleteCancelledBefore(@Param("date") LocalDate date);
}
