package com.example.clinic.service;

import com.example.clinic.entity.Appointment;
import com.example.clinic.entity.AppointmentStatus;
import com.example.clinic.entity.Prescription;
import com.example.clinic.repository.AppointmentRepository;
import com.example.clinic.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PrescriptionService {

    private final PrescriptionRepository presRepo;
    private final AppointmentRepository apptRepo;

    public PrescriptionService(PrescriptionRepository presRepo, AppointmentRepository apptRepo) {
        this.presRepo = presRepo;
        this.apptRepo = apptRepo;
    }

    public String save(Long appointmentId, Prescription p) {
        Appointment appt = apptRepo.findById(appointmentId).orElse(null);
        if (appt == null) return "APPT_NOT_FOUND";
        if (appt.getStatus() != AppointmentStatus.COMPLETED) return "NOT_COMPLETED";
        p.setId(UUID.randomUUID());
        p.setIssuedDate(LocalDate.now());
        p.setAppointment(appt);
        presRepo.save(p);
        return "OK_SAVED";
    }

    public List<Prescription> findByAppointment(Long appointmentId) {
        return presRepo.findByAppointmentAppointmentId(appointmentId);
    }

    public List<Prescription> findByPatient(String ssNumber) {
        return presRepo.findByPatientSsNumber(ssNumber);
    }

    public String update(UUID id, Prescription details) {
        Prescription p = presRepo.findById(id).orElse(null);
        if (p == null) return "NOT_FOUND";
        p.setMedicineName(details.getMedicineName());
        p.setDosage(details.getDosage());
        p.setDurationDays(details.getDurationDays());
        presRepo.save(p);
        return "OK_UPDATED";
    }

    public String delete(UUID id) {
        if (!presRepo.existsById(id)) return "NOT_FOUND";
        presRepo.deleteById(id);
        return "OK_DELETED";
    }

    public boolean hasPrescriptions(Long appointmentId) {
        return presRepo.existsByAppointmentAppointmentId(appointmentId);
    }

    public List<Object[]> topMedicines() {
        return presRepo.topMedicines();
    }
}
