package com.example.clinic.controller;

import com.example.clinic.entity.Specialization;
import com.example.clinic.service.SpecializationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specializations")
public class SpecializationController {

    private final SpecializationService service;

    public SpecializationController(SpecializationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Specialization> create(@RequestBody Specialization s) {
        return new ResponseEntity<>(service.create(s), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Specialization> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Specialization getById(@PathVariable Long id) { return service.getById(id); }

    @PutMapping("/{id}")
    public Specialization update(@PathVariable Long id, @RequestBody Specialization s) {
        return service.update(id, s);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unused")
    public List<Specialization> unused() { return service.findUnused(); }
}
