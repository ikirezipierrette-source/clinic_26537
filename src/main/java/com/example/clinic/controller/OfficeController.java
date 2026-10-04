package com.example.clinic.controller;

import com.example.clinic.entity.Office;
import com.example.clinic.repository.OfficeRepository;
import com.example.clinic.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offices")
public class OfficeController {

    private final OfficeRepository officeRepository;
    private final AppointmentService appointmentService;

    public OfficeController(OfficeRepository officeRepository, AppointmentService appointmentService) {
        this.officeRepository = officeRepository;
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public Office create(@RequestBody Office o) { return officeRepository.save(o); }

    @GetMapping
    public List<Office> getAll() { return officeRepository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Office> getById(@PathVariable Long id) {
        return officeRepository.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Office> update(@PathVariable Long id, @RequestBody Office details) {
        return officeRepository.findById(id).map(o -> {
            o.setRoomNumber(details.getRoomNumber());
            return ResponseEntity.ok(officeRepository.save(o));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        officeRepository.deleteById(id);
        return ResponseEntity.ok("Office deleted with id: " + id);
    }

    @GetMapping("/busiest")
    public ResponseEntity<?> busiest() {
        List<Object[]> rows = appointmentService.busiestOffice();
        if (rows.isEmpty()) return ResponseEntity.ok("No appointments yet");
        return ResponseEntity.ok(rows.get(0));
    }
}
