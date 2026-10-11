package com.example.clinic.service;

import com.example.clinic.entity.Appointment;
import com.example.clinic.entity.AppointmentStatus;
import com.example.clinic.repository.AppointmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository apptRepo;

    public AppointmentService(AppointmentRepository apptRepo) {
        this.apptRepo = apptRepo;
    }

    public List<Appointment> findAll() {
        return apptRepo.findAll();
    }

    public Appointment findById(Long id) {
        return apptRepo.findById(id).orElse(null);
    }

    public Appointment save(Appointment a) {
        return apptRepo.save(a);
    }

    public void delete(Long id) {
        apptRepo.deleteById(id);
    }

    public List<Appointment> findByStatus(AppointmentStatus status) {
        return apptRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> findBetween(LocalDate start, LocalDate end) {
        return apptRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    public boolean isDoctorBooked(Long doctorId, LocalDate date) {
        return apptRepo.existsByDoctorDoctorIdAndAppointmentDateAndStatusNot(
                doctorId, date, AppointmentStatus.CANCELLED);
    }

    public List<Object[]> countByStatus() {
        return apptRepo.countByStatus();
    }

    public List<Object[]> busiestOffice() {
        return apptRepo.findBusiestOffice();
    }

    @Transactional
    public int cancelDay(Long doctorId, LocalDate date) {
        return apptRepo.cancelDayForDoctor(
                doctorId, date, AppointmentStatus.CANCELLED, AppointmentStatus.COMPLETED);
    }

    @Transactional
    public int deleteCancelledBefore(LocalDate date) {
        return apptRepo.deleteCancelledBefore(date, AppointmentStatus.CANCELLED);
    }

    public Page<Appointment> findPage(Pageable pageable) {
        return apptRepo.findAll(pageable);
    }
}
