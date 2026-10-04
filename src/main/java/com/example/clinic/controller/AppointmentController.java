package com.example.clinic.controller;

import com.example.clinic.entity.Appointment;
import com.example.clinic.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody Appointment a) {
        return new ResponseEntity<>(appointmentService.save(a), HttpStatus.CREATED);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Appointment a) {
        if (a.getDoctor() != null && a.getDoctor().getDoctorId() != null && a.getDate() != null) {
            if (appointmentService.isDoctorBooked(a.getDoctor().getDoctorId(), a.getDate())) {
                return ResponseEntity.status(409).body("Doctor is already booked on that date");
            }
        }
        return new ResponseEntity<>(appointmentService.save(a), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Appointment> getAll() { return appointmentService.getAll(); }

    @GetMapping("/{id}")
    public Appointment getById(@PathVariable Long id) { return appointmentService.getById(id); }

    @PutMapping("/{id}")
    public Appointment update(@PathVariable Long id, @RequestBody Appointment a) {
        a.setAppointmentId(id);
        return appointmentService.save(a);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.ok("Appointment deleted with id: " + id);
    }

    @GetMapping("/by-status")
    public List<Appointment> byStatus(@RequestParam String status) {
        return appointmentService.findByStatus(status);
    }

    @GetMapping("/between")
    public List<Appointment> between(@RequestParam String start, @RequestParam String end) {
        return appointmentService.findBetween(LocalDate.parse(start), LocalDate.parse(end));
    }

    @GetMapping("/status/by-status")
    public List<Object[]> countsByStatus() {
        return appointmentService.countsByStatus();
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam Long doctorId, @RequestParam String date) {
        int n = appointmentService.cancelDay(doctorId, LocalDate.parse(date));
        return ResponseEntity.ok(n + " appointments cancelled");
    }

    @DeleteMapping("/cancelled-before")
    public ResponseEntity<String> deleteCancelledBefore(@RequestParam String date) {
        int n = appointmentService.deleteCancelledBefore(LocalDate.parse(date));
        return ResponseEntity.ok(n + " appointments deleted");
    }
}
