package com.example.clinic.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final com.example.clinic.repository.PatientRepository patRepo;
    private final com.example.clinic.repository.DoctorRepository docRepo;
    private final com.example.clinic.repository.OfficeRepository officeRepo;
    private final com.example.clinic.repository.PrescriptionRepository presRepo;
    private final com.example.clinic.service.AppointmentService apptService;

    public DashboardService(com.example.clinic.repository.PatientRepository patRepo,
                            com.example.clinic.repository.DoctorRepository docRepo,
                            com.example.clinic.repository.OfficeRepository officeRepo,
                            com.example.clinic.repository.PrescriptionRepository presRepo,
                            com.example.clinic.service.AppointmentService apptService) {
        this.patRepo = patRepo;
        this.docRepo = docRepo;
        this.officeRepo = officeRepo;
        this.presRepo = presRepo;
        this.apptService = apptService;
    }

    public Map<String, Long> summary() {
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("patients", patRepo.count());
        map.put("doctors", docRepo.count());
        map.put("freeOffices", (long) officeRepo.findFreeOffices().size());
        map.put("appointmentsToday", apptService.countToday());
        map.put("scheduled", apptService.countByStatusSingle(com.example.clinic.entity.AppointmentStatus.SCHEDULED));
        map.put("prescriptions", presRepo.count());
        return map;
    }
}
