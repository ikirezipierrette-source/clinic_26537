package com.example.clinic.service;

import com.example.clinic.entity.Specialization;
import com.example.clinic.repository.SpecializationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecializationService {

    private final SpecializationRepository repo;

    public SpecializationService(SpecializationRepository repo) {
        this.repo = repo;
    }

    public List<Specialization> getAll() { return repo.findAll(); }

    public Specialization getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Specialization not found: " + id));
    }

    public Specialization create(Specialization s) { return repo.save(s); }

    public Specialization update(Long id, Specialization details) {
        Specialization s = getById(id);
        s.setName(details.getName());
        return repo.save(s);
    }

    public void delete(Long id) { repo.deleteById(id); }

    public List<Specialization> findUnused() {
        return repo.findUnusedSpecializations();
    }
}
