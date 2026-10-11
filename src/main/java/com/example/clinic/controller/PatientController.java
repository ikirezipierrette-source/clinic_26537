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

    @GetMapping
    public List<Patient> getAll() {
        return patientService.findAll();
    }

    @GetMapping("/{id}")
    public Patient getById(@PathVariable Long id) {
        return patientService.findById(id);
    }

    @PostMapping
    public Patient create(@RequestBody Patient p) {
        return patientService.save(p);
    }

    @PutMapping("/{id}")
    public Patient update(@PathVariable Long id, @RequestBody Patient p) {
        p.setPatientId(id);
        return patientService.save(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.ok("Patient " + id + " deleted");
    }

    @GetMapping("/by-last-name")
    public List<Patient> byLastName(@RequestParam String lastName) {
        return patientService.findByLastName(lastName);
    }

    @GetMapping("/of-doctor/{doctorId}")
    public ResponseEntity<?> ofDoctor(@PathVariable Long doctorId) {
        if (!doctorService.exists(doctorId)) {
            return ResponseEntity.status(404).body("The doctor with that id does not exist");
        }
        return ResponseEntity.ok(patientService.findPatientsOfDoctor(doctorId));
    }

    @GetMapping("/frequent")
    public List<Patient> frequent(@RequestParam long min) {
        return patientService.findFrequent(min);
    }
}
