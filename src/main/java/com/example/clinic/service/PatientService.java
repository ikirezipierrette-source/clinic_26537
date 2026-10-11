package com.example.clinic.service;

import com.example.clinic.entity.Patient;
import com.example.clinic.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepo;

    public PatientService(PatientRepository patientRepo) {
        this.patientRepo = patientRepo;
    }

    public List<Patient> findAll() { return patientRepo.findAll(); }

    public Patient findById(Long id) { return patientRepo.findById(id).orElse(null); }

    public Patient save(Patient p) { return patientRepo.save(p); }

    public void delete(Long id) { patientRepo.deleteById(id); }

    public Patient findBySsNumber(String ss) { return patientRepo.findBySsNumber(ss); }

    public List<Patient> findByFullName(String name) { return patientRepo.findByFullNameContainingIgnoreCaseOrderByFullNameAsc(name); }

    public List<Patient> findFrequent(long min) { return patientRepo.findFrequentPatients(min); }

    public List<Patient> findPatientsOfDoctor(Long doctorId) { return patientRepo.findPatientsByDoctorId(doctorId); }
}
