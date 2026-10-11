package com.example.clinic.service;

import com.example.clinic.entity.Appointment;
import com.example.clinic.entity.Doctor;
import com.example.clinic.repository.AppointmentRepository;
import com.example.clinic.repository.DoctorRepository;
import com.example.clinic.repository.OfficeRepository;
import com.example.clinic.repository.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepo;
    private final OfficeRepository officeRepo;
    private final SpecializationRepository specRepo;
    private final AppointmentRepository apptRepo;

    public DoctorService(DoctorRepository doctorRepo, OfficeRepository officeRepo,
                         SpecializationRepository specRepo, AppointmentRepository apptRepo) {
        this.doctorRepo = doctorRepo;
        this.officeRepo = officeRepo;
        this.specRepo = specRepo;
        this.apptRepo = apptRepo;
    }

    public List<Doctor> findAll() { return doctorRepo.findAll(); }

    public Doctor findById(Long id) { return doctorRepo.findById(id).orElse(null); }

    public Doctor save(Doctor d) { return doctorRepo.save(d); }

    public void delete(Long id) { doctorRepo.deleteById(id); }

    public boolean exists(Long id) { return doctorRepo.existsById(id); }

    public List<Doctor> findBySpecialization(String name) { return doctorRepo.findBySpecializationName(name); }

    public List<Doctor> findWithoutOffice() { return doctorRepo.findDoctorsWithoutOffice(); }

    public List<Doctor> search(String name) { return doctorRepo.searchByName(name); }

    public List<Appointment> getAppointmentsFor(Long doctorId, LocalDate date) {
        return apptRepo.findByDoctorDoctorIdAndAppointmentDateAndStatusNot(doctorId, date, com.example.clinic.entity.AppointmentStatus.CANCELLED);
    }

    public List<Object[]> workload(LocalDate start, LocalDate end) {
        return doctorRepo.workload(start, end);
    }

    public String addSpecialization(Long doctorId, Long specId) {
        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
        if (doctor == null) return "DOCTOR_NOT_FOUND";
        com.example.clinic.entity.Specialization spec = specRepo.findById(specId).orElse(null);
        if (spec == null) return "SPEC_NOT_FOUND";
        if (doctor.getSpecializations().contains(spec)) return "ALREADY_HAS";
        doctor.getSpecializations().add(spec);
        doctorRepo.save(doctor);
        return "OK_ADDED";
    }

    public String removeSpecialization(Long doctorId, Long specId) {
        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
        if (doctor == null) return "DOCTOR_NOT_FOUND";
        com.example.clinic.entity.Specialization spec = specRepo.findById(specId).orElse(null);
        if (spec == null) return "SPEC_NOT_FOUND";
        if (!doctor.getSpecializations().contains(spec)) return "DOES_NOT_HAVE";
        doctor.getSpecializations().remove(spec);
        doctorRepo.save(doctor);
        return "OK_REMOVED";
    }

    public String changeOffice(Long doctorId, String officeNumber) {
        Doctor doctor = doctorRepo.findById(doctorId).orElse(null);
        if (doctor == null) return "DOCTOR_NOT_FOUND";
        com.example.clinic.entity.Office newOffice = officeRepo.findAll().stream()
                .filter(o -> o.getRoomNumber().equals(officeNumber))
                .findFirst().orElse(null);
        if (newOffice == null) return "OFFICE_NOT_FOUND";
        if (newOffice.equals(doctor.getOffice())) return "OK_CHANGED";
        if (doctorRepo.existsByOfficeOfficeId(newOffice.getOfficeId())) return "OFFICE_TAKEN";
        doctor.setOffice(newOffice);
        doctorRepo.save(doctor);
        return "OK_CHANGED";
    }
}
