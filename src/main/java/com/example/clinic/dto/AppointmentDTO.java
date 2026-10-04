package com.example.clinic.dto;

import java.time.LocalDate;

public class AppointmentDTO {

    private String patientName;
    private String doctorName;
    private LocalDate date;

    public AppointmentDTO(String patientName, String doctorName, LocalDate date) {
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.date = date;
    }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
