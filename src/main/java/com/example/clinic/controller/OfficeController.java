package com.example.clinic.controller;

import com.example.clinic.entity.Office;
import com.example.clinic.service.AppointmentService;
import com.example.clinic.service.OfficeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offices")
public class OfficeController {

    private final OfficeService officeService;
    private final AppointmentService appointmentService;

    public OfficeController(OfficeService officeService, AppointmentService appointmentService) {
        this.officeService = officeService;
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<Office> getAll() {
        return officeService.findAll();
    }

    @GetMapping("/{id}")
    public Office getById(@PathVariable Long id) {
        return officeService.findById(id);
    }

    @PostMapping
    public Office create(@RequestBody Office o) {
        return officeService.save(o);
    }

    @PutMapping("/{id}")
    public Office update(@PathVariable Long id, @RequestBody Office o) {
        o.setOfficeId(id);
        return officeService.save(o);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        officeService.delete(id);
        return ResponseEntity.ok("Office " + id + " deleted");
    }

    @GetMapping("/busiest")
    public ResponseEntity<?> busiest() {
        List<Object[]> rows = appointmentService.busiestOffice();
        if (rows.isEmpty()) {
            return ResponseEntity.ok("No appointments yet");
        }
        return ResponseEntity.ok(rows.get(0));
    }
}
