package com.example.clinic.service;

import com.example.clinic.entity.Doctor;
import com.example.clinic.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepo;

    public DoctorService(DoctorRepository doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    public List<Doctor> findAll() {
        return doctorRepo.findAll();
    }

    public Doctor findById(Long id) {
        return doctorRepo.findById(id).orElse(null);
    }

    public Doctor save(Doctor d) {
        return doctorRepo.save(d);
    }

    public void delete(Long id) {
        doctorRepo.deleteById(id);
    }

    public boolean exists(Long id) {
        return doctorRepo.existsById(id);
    }

    public List<Doctor> findBySpecialization(String name) {
        return doctorRepo.findBySpecializationName(name);
    }

    public List<Doctor> findWithoutOffice() {
        return doctorRepo.findDoctorsWithoutOffice();
    }
}
