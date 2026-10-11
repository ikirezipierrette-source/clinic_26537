package com.example.clinic.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "medicine_name", nullable = false)
    private String medicineName;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "duration_days")
    private int durationDays;

    @Column(name = "issued_date")
    private LocalDate issuedDate;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    public Prescription() {
    }

    public Prescription(String medicineName, String dosage, int durationDays, LocalDate issuedDate) {
        this.id = UUID.randomUUID();
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.durationDays = durationDays;
        this.issuedDate = issuedDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDate issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}
