package com.example.clinic.controller;

import com.example.clinic.entity.Specialization;
import com.example.clinic.service.SpecializationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    private final SpecializationService specService;

    public SpecializationController(SpecializationService specService) {
        this.specService = specService;
    }

    @GetMapping
    public List<Specialization> getAll() {
        return specService.findAll();
    }

    @GetMapping("/{id}")
    public Specialization getById(@PathVariable Long id) {
        return specService.findById(id);
    }

    @PostMapping
    public Specialization create(@RequestBody Specialization s) {
        return specService.save(s);
    }

    @PutMapping("/{id}")
    public Specialization update(@PathVariable Long id, @RequestBody Specialization s) {
        s.setSpecializationId(id);
        return specService.save(s);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        specService.delete(id);
        return ResponseEntity.ok("Specialization " + id + " deleted");
    }

    @GetMapping("/unused")
    public List<Specialization> unused() {
        return specService.findUnused();
    }
}
