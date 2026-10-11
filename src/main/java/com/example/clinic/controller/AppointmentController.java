package com.example.clinic.controller;

import com.example.clinic.entity.Appointment;
import com.example.clinic.entity.AppointmentStatus;
import com.example.clinic.service.AppointmentService;
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

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
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
    public ResponseEntity<?> save(@RequestBody Appointment a) {
        if (a.getDoctor() != null && a.getDoctor().getDoctorId() != null
                && a.getAppointmentDate() != null) {
            if (appointmentService.isDoctorBooked(
                    a.getDoctor().getDoctorId(), a.getAppointmentDate())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Doctor is already booked on that date");
            }
        }
        return new ResponseEntity<>(appointmentService.save(a), HttpStatus.CREATED);
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
        appointmentService.delete(id);
        return ResponseEntity.ok("Appointment " + id + " deleted");
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
    public ResponseEntity<String> cancelDay(@RequestParam Long doctorId,
                                            @RequestParam String date) {
        int n = appointmentService.cancelDay(doctorId, LocalDate.parse(date));
        return ResponseEntity.ok(n + " appointments cancelled");
    }

    @DeleteMapping("/cancelled-before")
    public ResponseEntity<String> cancelledBefore(@RequestParam String date) {
        int n = appointmentService.deleteCancelledBefore(LocalDate.parse(date));
        return ResponseEntity.ok(n + " appointments deleted");
    }

    @GetMapping("/page")
    public Page<Appointment> page(@RequestParam int page,
                                  @RequestParam int size,
                                  @RequestParam String sort) {
        String[] parts = sort.split(",");
        Sort.Direction dir = parts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, parts[0]));
        return appointmentService.findPage(pageable);
    }
}
