package com.example.clinic.service;

import com.example.clinic.entity.Doctor;
import com.example.clinic.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAll() { return doctorRepository.findAll(); }

    public Doctor getById(Long id) { return doctorRepository.findById(id).orElse(null); }

    public Doctor save(Doctor d) { return doctorRepository.save(d); }

    public void delete(Long id) { doctorRepository.deleteById(id); }

    public boolean existsById(Long id) { return doctorRepository.existsById(id); }

    public List<Doctor> findBySpecialization(String name) {
        return doctorRepository.findDoctorsBySpecializationName(name);
    }

    public List<Doctor> findWithoutOffice() {
        return doctorRepository.findDoctorsWithoutOffice();
    }
}
