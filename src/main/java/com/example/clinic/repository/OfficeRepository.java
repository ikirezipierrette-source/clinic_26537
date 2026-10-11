package com.example.clinic.repository;

import com.example.clinic.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfficeRepository extends JpaRepository<Office, Long> {

    @Query("SELECT o FROM Office o WHERE NOT EXISTS (SELECT d FROM Doctor d WHERE d.office = o) ORDER BY o.roomNumber")
    List<Office> findFreeOffices();

    boolean existsByRoomNumber(String roomNumber);
}
