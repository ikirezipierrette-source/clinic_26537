package com.example.clinic.repository;

import com.example.clinic.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    List<Prescription> findByAppointmentAppointmentId(Long appointmentId);

    @Query("SELECT p FROM Prescription p WHERE p.appointment.patient.ssNumber = :ss ORDER BY p.issuedDate DESC")
    List<Prescription> findByPatientSsNumber(@Param("ss") String ssNumber);

    boolean existsByAppointmentAppointmentId(Long appointmentId);

    @Query("SELECT p.medicineName, COUNT(p) FROM Prescription p GROUP BY p.medicineName ORDER BY COUNT(p) DESC")
    List<Object[]> topMedicines();
}
