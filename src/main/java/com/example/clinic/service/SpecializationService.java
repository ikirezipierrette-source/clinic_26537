package com.example.clinic.service;

import com.example.clinic.entity.Specialization;
import com.example.clinic.repository.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecializationService {

    private final SpecializationRepository specRepo;

    public SpecializationService(SpecializationRepository specRepo) {
        this.specRepo = specRepo;
    }

    public List<Specialization> findAll() {
        return specRepo.findAll();
    }

    public Specialization findById(Long id) {
        return specRepo.findById(id).orElse(null);
    }

    public Specialization save(Specialization s) {
        return specRepo.save(s);
    }

    public void delete(Long id) {
        specRepo.deleteById(id);
    }

    public List<Specialization> findUnused() {
        return specRepo.findUnused();
    }
}
