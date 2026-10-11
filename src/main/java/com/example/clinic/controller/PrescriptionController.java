package com.example.clinic.controller;

import com.example.clinic.entity.Prescription;
import com.example.clinic.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    private final PrescriptionService presService;

    public PrescriptionController(PrescriptionService presService) {
        this.presService = presService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam Long appointmentId, @RequestBody Prescription p) {
        String result = presService.save(appointmentId, p);
        if (result.equals("APPT_NOT_FOUND")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The appointment with that id does not exist");
        }
        if (result.equals("NOT_COMPLETED")) {
            return ResponseEntity.badRequest().body("Prescriptions are only for COMPLETED appointments");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Prescription saved successfully");
    }

    @GetMapping("/appointment/{appointmentId}")
    public List<Prescription> byAppointment(@PathVariable Long appointmentId) {
        return presService.findByAppointment(appointmentId);
    }

    @GetMapping("/patient/{ssNumber}")
    public List<Prescription> byPatient(@PathVariable String ssNumber) {
        return presService.findByPatient(ssNumber);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Prescription p) {
        String result = presService.update(id, p);
        if (result.equals("NOT_FOUND")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The prescription with that id does not exist");
        }
        return ResponseEntity.ok("Prescription updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String result = presService.delete(id);
        if (result.equals("NOT_FOUND")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The prescription with that id does not exist");
        }
        return ResponseEntity.ok("Prescription deleted successfully");
    }

    @GetMapping("/stats/top-medicines")
    public List<Object[]> topMedicines() {
        return presService.topMedicines();
    }
}
