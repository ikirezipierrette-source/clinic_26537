package com.example.clinic.service;

import com.example.clinic.entity.Appointment;
import com.example.clinic.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAll() { return appointmentRepository.findAll(); }

    public Appointment getById(Long id) { return appointmentRepository.findById(id).orElse(null); }

    public Appointment save(Appointment a) { return appointmentRepository.save(a); }

    public void delete(Long id) { appointmentRepository.deleteById(id); }

    public List<Appointment> findByStatus(String status) {
        return appointmentRepository.findByStatusOrderByDateAsc(status);
    }

    public List<Appointment> findBetween(LocalDate start, LocalDate end) {
        return appointmentRepository.findByDateBetweenOrderByDateAsc(start, end);
    }

    public boolean isDoctorBooked(Long doctorId, LocalDate date) {
        return appointmentRepository.existsByDoctorDoctorIdAndDateAndStatusNot(doctorId, date, "CANCELLED");
    }

    public List<Object[]> countsByStatus() {
        return appointmentRepository.countAppointmentsPerStatus();
    }

    public List<Object[]> busiestOffice() {
        return appointmentRepository.findBusiestOffice();
    }

    @Transactional
    public int cancelDay(Long doctorId, LocalDate date) {
        return appointmentRepository.cancelDayForDoctor(doctorId, date);
    }

    @Transactional
    public int deleteCancelledBefore(LocalDate date) {
        return appointmentRepository.deleteCancelledBefore(date);
    }
}
