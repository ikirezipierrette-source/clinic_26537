package com.example.clinic.service;

import com.example.clinic.entity.Appointment;
import com.example.clinic.entity.AppointmentStatus;
import com.example.clinic.repository.AppointmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository apptRepo;

    public AppointmentService(AppointmentRepository apptRepo) {
        this.apptRepo = apptRepo;
    }

    public List<Appointment> findAll() { return apptRepo.findAll(); }

    public Appointment findById(Long id) { return apptRepo.findById(id).orElse(null); }

    public Appointment save(Appointment a) { return apptRepo.save(a); }

    public void delete(Long id) { apptRepo.deleteById(id); }

    public List<Appointment> findByStatus(AppointmentStatus status) {
        return apptRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> findBetween(LocalDate start, LocalDate end) {
        return apptRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    public boolean isDoctorBooked(Long doctorId, LocalDate date) {
        return apptRepo.existsByDoctorDoctorIdAndAppointmentDateAndStatusNot(doctorId, date, AppointmentStatus.CANCELLED);
    }

    public List<Object[]> countByStatus() { return apptRepo.countByStatus(); }

    public List<Object[]> busiestOffice() { return apptRepo.findBusiestOffice(); }

    @Transactional
    public int cancelDay(Long doctorId, LocalDate date) {
        return apptRepo.cancelDayForDoctor(doctorId, date, AppointmentStatus.CANCELLED, AppointmentStatus.COMPLETED);
    }

    @Transactional
    public int deleteCancelledBefore(LocalDate date) {
        return apptRepo.deleteCancelledBefore(date, AppointmentStatus.CANCELLED);
    }

    public Page<Appointment> findPage(Pageable pageable) { return apptRepo.findAll(pageable); }

    public List<Appointment> agenda(Long doctorId, LocalDate date) {
        return apptRepo.findByDoctorDoctorIdAndAppointmentDateAndStatusNot(doctorId, date, AppointmentStatus.CANCELLED);
    }

    public List<Appointment> upcoming(String ssNumber) {
        return apptRepo.findUpcomingByPatient(ssNumber, LocalDate.now(),
                Arrays.asList(AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED));
    }

    public long countToday() { return apptRepo.countByAppointmentDate(LocalDate.now()); }

    public long countByStatusSingle(AppointmentStatus status) { return apptRepo.countByStatus(status); }

    public String safeBook(Appointment a) {
        if (a.getAppointmentDate() == null) return "NO_DATE";
        if (a.getAppointmentDate().isBefore(LocalDate.now())) return "PAST_DATE";
        a.setStatus(AppointmentStatus.SCHEDULED);
        apptRepo.save(a);
        return "OK_BOOKED";
    }

    public String changeStatus(Long id, AppointmentStatus newStatus) {
        Appointment a = apptRepo.findById(id).orElse(null);
        if (a == null) return "NOT_FOUND:" + id;
        AppointmentStatus old = a.getStatus();
        if (old == newStatus) return "INVALID:" + old + ":" + newStatus;
        if (old == AppointmentStatus.SCHEDULED) {
            if (newStatus == AppointmentStatus.CONFIRMED || newStatus == AppointmentStatus.CANCELLED) {
                a.setStatus(newStatus);
                apptRepo.save(a);
                return "OK_UPDATED";
            }
        }
        if (old == AppointmentStatus.CONFIRMED) {
            if (newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.CANCELLED) {
                a.setStatus(newStatus);
                apptRepo.save(a);
                return "OK_UPDATED";
            }
        }
        return "INVALID:" + old + ":" + newStatus;
    }

    public String reschedule(Long id, LocalDate newDate) {
        Appointment a = apptRepo.findById(id).orElse(null);
        if (a == null) return "NOT_FOUND";
        if (a.getStatus() == AppointmentStatus.COMPLETED) return "COMPLETED";
        if (a.getStatus() == AppointmentStatus.CANCELLED) return "CANCELLED";
        if (newDate.isBefore(LocalDate.now())) return "PAST_DATE";
        Long doctorId = a.getDoctor() != null ? a.getDoctor().getDoctorId() : null;
        if (doctorId != null && isDoctorBooked(doctorId, newDate)) return "DOCTOR_BOOKED";
        a.setAppointmentDate(newDate);
        a.setStatus(AppointmentStatus.SCHEDULED);
        apptRepo.save(a);
        return "OK_RESCHEDULED";
    }
}
