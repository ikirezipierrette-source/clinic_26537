package com.example.clinic.repository;

import com.example.clinic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Patient findBySsNumber(String ssNumber);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY p.firstName ASC")
    List<Patient> findByFullNameContainingIgnoreCaseOrderByFullNameAsc(@Param("name") String name);

    @Query("SELECT a.patient FROM Appointment a GROUP BY a.patient HAVING COUNT(a) >= :min ORDER BY COUNT(a) DESC")
    List<Patient> findFrequentPatients(@Param("min") long min);

    @Query("SELECT DISTINCT a.patient FROM Appointment a WHERE a.doctor.doctorId = :doctorId")
    List<Patient> findPatientsByDoctorId(@Param("doctorId") Long doctorId);
}
