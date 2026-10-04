package com.example.clinic.controller;

import com.example.clinic.entity.Patient;
import com.example.clinic.service.DoctorService;
import com.example.clinic.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final DoctorService doctorService;

    public PatientController(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @PostMapping
    public Patient create(@RequestBody Patient p) { return patientService.save(p); }

    @GetMapping
    public List<Patient> getAll() { return patientService.getAll(); }

    @GetMapping("/{id}")
    public Patient getById(@PathVariable Long id) { return patientService.getById(id); }

    @PutMapping("/{id}")
    public Patient update(@PathVariable Long id, @RequestBody Patient p) {
        p.setPatientId(id);
        return patientService.save(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.ok("Patient deleted with id: " + id);
    }

    @GetMapping("/by-last-name")
    public List<Patient> byLastName(@RequestParam String lastName) {
        return patientService.findByFullName(lastName);
    }

    @GetMapping("/frequent")
    public List<Patient> frequent(@RequestParam long min) {
        return patientService.findFrequent(min);
    }

    @GetMapping("/of-doctor/{doctorId}")
    public ResponseEntity<?> ofDoctor(@PathVariable Long doctorId) {
        if (!doctorService.existsById(doctorId)) {
            return ResponseEntity.status(404).body("The doctor with that id does not exist");
        }
        return ResponseEntity.ok(patientService.findPatientsOfDoctor(doctorId));
    }
}
