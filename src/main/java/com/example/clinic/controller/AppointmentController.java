package com.example.clinic.controller;

import com.example.clinic.entity.Appointment;
import com.example.clinic.entity.AppointmentStatus;
import com.example.clinic.service.AppointmentService;
import com.example.clinic.service.PrescriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;

    public AppointmentController(AppointmentService appointmentService, PrescriptionService prescriptionService) {
        this.appointmentService = appointmentService;
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    public List<Appointment> getAll() {
        return appointmentService.findAll();
    }

    @GetMapping("/{id}")
    public Appointment getById(@PathVariable Long id) {
        return appointmentService.findById(id);
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Appointment a) {
        String result = appointmentService.safeBook(a);
        if (result.equals("PAST_DATE")) {
            return ResponseEntity.badRequest().body("Appointment date cannot be in the past");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Appointment saved successfully");
    }

    @PostMapping
    public Appointment create(@RequestBody Appointment a) {
        return appointmentService.save(a);
    }

    @PutMapping("/{id}")
    public Appointment update(@PathVariable Long id, @RequestBody Appointment a) {
        a.setAppointmentId(id);
        return appointmentService.save(a);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            boolean has = prescriptionService.hasPrescriptions(id);
            if (has) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("That appointment has prescriptions, delete them first");
            }
            appointmentService.delete(id);
            return ResponseEntity.ok("Appointment " + id + " deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/by-status")
    public List<Appointment> byStatus(@RequestParam AppointmentStatus status) {
        return appointmentService.findByStatus(status);
    }

    @GetMapping("/between")
    public List<Appointment> between(@RequestParam String start, @RequestParam String end) {
        return appointmentService.findBetween(LocalDate.parse(start), LocalDate.parse(end));
    }

    @GetMapping("/stats/by-status")
    public List<Object[]> statsByStatus() {
        return appointmentService.countByStatus();
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam Long doctorId, @RequestParam String date) {
        int n = appointmentService.cancelDay(doctorId, LocalDate.parse(date));
        return ResponseEntity.ok(n + " appointments cancelled");
    }

    @DeleteMapping("/cancelled-before")
    public ResponseEntity<String> cancelledBefore(@RequestParam String date) {
        int n = appointmentService.deleteCancelledBefore(LocalDate.parse(date));
        return ResponseEntity.ok(n + " appointments deleted");
    }

    @GetMapping("/page")
    public Page<Appointment> page(@RequestParam int page, @RequestParam int size, @RequestParam String sort) {
        String[] parts = sort.split(",");
        Sort.Direction dir = parts[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, parts[0]));
        return appointmentService.findPage(pageable);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> changeStatus(@PathVariable Long id, @RequestParam AppointmentStatus status) {
        String result = appointmentService.changeStatus(id, status);
        if (result.startsWith("NOT_FOUND")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The appointment with that id does not exist");
        }
        if (result.startsWith("INVALID")) {
            String[] parts = result.split(":");
            return ResponseEntity.badRequest().body("Cannot change status from " + parts[1] + " to " + parts[2]);
        }
        return ResponseEntity.ok("Appointment status updated successfully");
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<String> reschedule(@PathVariable Long id, @RequestParam String newDate) {
        String result = appointmentService.reschedule(id, LocalDate.parse(newDate));
        if (result.equals("NOT_FOUND")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The appointment with that id does not exist");
        }
        if (result.equals("COMPLETED")) {
            return ResponseEntity.badRequest().body("A COMPLETED appointment cannot be rescheduled");
        }
        if (result.equals("CANCELLED")) {
            return ResponseEntity.badRequest().body("A CANCELLED appointment cannot be rescheduled");
        }
        if (result.equals("PAST_DATE")) {
            return ResponseEntity.badRequest().body("Appointment date cannot be in the past");
        }
        if (result.equals("DOCTOR_BOOKED")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor is already booked on that date");
        }
        return ResponseEntity.ok("Appointment rescheduled successfully");
    }
}
