package com.example.clinic.service;

import com.example.clinic.entity.Patient;
import com.example.clinic.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAll() { return patientRepository.findAll(); }

    public Patient getById(Long id) { return patientRepository.findById(id).orElse(null); }

    public Patient save(Patient p) { return patientRepository.save(p); }

    public void delete(Long id) { patientRepository.deleteById(id); }

    public List<Patient> findByFullName(String name) {
        return patientRepository.findByFullNameContainingIgnoreCaseOrderByFullNameAsc(name);
    }

    public List<Patient> findFrequent(long min) {
        return patientRepository.findFrequentPatients(min);
    }

    public List<Patient> findPatientsOfDoctor(Long doctorId) {
        return patientRepository.findPatientsByDoctorId(doctorId);
    }
}
