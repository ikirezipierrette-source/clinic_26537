package com.example.clinic.controller;

import com.example.clinic.entity.Doctor;
import com.example.clinic.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public List<Doctor> getAll() { return doctorService.findAll(); }

    @GetMapping("/{id}")
    public Doctor getById(@PathVariable Long id) { return doctorService.findById(id); }

    @PostMapping
    public Doctor create(@RequestBody Doctor d) { return doctorService.save(d); }

    @PutMapping("/{id}")
    public Doctor update(@PathVariable Long id, @RequestBody Doctor d) {
        d.setDoctorId(id);
        return doctorService.save(d);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.ok("Doctor " + id + " deleted");
    }

    @GetMapping("/by-specialization")
    public List<Doctor> bySpecialization(@RequestParam String name) { return doctorService.findBySpecialization(name); }

    @GetMapping("/without-office")
    public List<Doctor> withoutOffice() { return doctorService.findWithoutOffice(); }

    @GetMapping("/search")
    public List<Doctor> search(@RequestParam String name) { return doctorService.search(name); }

    @GetMapping("/workload")
    public List<Object[]> workload(@RequestParam String start, @RequestParam String end) {
        return doctorService.workload(LocalDate.parse(start), LocalDate.parse(end));
    }

    @GetMapping("/{doctorId}/agenda")
    public ResponseEntity<?> agenda(@PathVariable Long doctorId, @RequestParam String date) {
        if (!doctorService.exists(doctorId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The doctor with that id does not exist");
        }
        return ResponseEntity.ok(doctorService.getAppointmentsFor(doctorId, LocalDate.parse(date)));
    }

    @PutMapping("/{doctorId}/specializations/{specId}")
    public ResponseEntity<String> addSpec(@PathVariable Long doctorId, @PathVariable Long specId) {
        String r = doctorService.addSpecialization(doctorId, specId);
        if (r.equals("DOCTOR_NOT_FOUND")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The doctor with that id does not exist");
        if (r.equals("SPEC_NOT_FOUND")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("That specialization does not exist");
        if (r.equals("ALREADY_HAS")) return ResponseEntity.status(HttpStatus.CONFLICT).body("The doctor already has that specialization");
        return ResponseEntity.ok("Specialization added to the doctor");
    }

    @DeleteMapping("/{doctorId}/specializations/{specId}")
    public ResponseEntity<String> removeSpec(@PathVariable Long doctorId, @PathVariable Long specId) {
        String r = doctorService.removeSpecialization(doctorId, specId);
        if (r.equals("DOCTOR_NOT_FOUND")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The doctor with that id does not exist");
        if (r.equals("SPEC_NOT_FOUND")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("That specialization does not exist");
        if (r.equals("DOES_NOT_HAVE")) return ResponseEntity.status(HttpStatus.CONFLICT).body("The doctor does not have that specialization");
        return ResponseEntity.ok("Specialization removed from the doctor");
    }

    @PatchMapping("/{doctorId}/office")
    public ResponseEntity<String> changeOffice(@PathVariable Long doctorId, @RequestParam String officeNumber) {
        String r = doctorService.changeOffice(doctorId, officeNumber);
        if (r.equals("DOCTOR_NOT_FOUND")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The doctor with that id does not exist");
        if (r.equals("OFFICE_NOT_FOUND")) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The office with that office number does not exist");
        if (r.equals("OFFICE_TAKEN")) return ResponseEntity.status(HttpStatus.CONFLICT).body("That office is taken by other doctor");
        return ResponseEntity.ok("Office changed successfully");
    }
}
