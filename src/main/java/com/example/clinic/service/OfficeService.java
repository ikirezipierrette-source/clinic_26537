package com.example.clinic.service;

import com.example.clinic.entity.Office;
import com.example.clinic.repository.OfficeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficeService {

    private final OfficeRepository officeRepo;

    public OfficeService(OfficeRepository officeRepo) {
        this.officeRepo = officeRepo;
    }

    public List<Office> findAll() {
        return officeRepo.findAll();
    }

    public Office findById(Long id) {
        return officeRepo.findById(id).orElse(null);
    }

    public Office save(Office o) {
        return officeRepo.save(o);
    }

    public void delete(Long id) {
        officeRepo.deleteById(id);
    }
}
