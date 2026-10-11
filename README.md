# Clinic Management REST API

A small Spring Boot app for managing doctors, offices, patients, specializations, and appointments in a clinic.

## Stack

- Java 17
- Spring Boot 4
- Spring Data JPA + Hibernate
- PostgreSQL

## Setup

Create a PostgreSQL database called `clinic`. Then edit `src/main/resources/application.properties` with your database username and password.

## Run

    mvn spring-boot:run

The API starts on port 5051.

## Main endpoints

    GET  /api/offices
    GET  /api/doctors
    GET  /api/patients
    GET  /api/specializations
    GET  /api/appointments

Same URL with POST / PUT / DELETE works for CRUD.

## Quiz endpoints (JPA & JPQL)

### Part A - derived queries

    GET  /api/patients/by-last-name?lastName=uwase
    GET  /api/appointments/by-status?status=SCHEDULED
    GET  /api/appointments/between?start=2026-10-01&end=2026-10-31
    POST /api/appointments/save

### Part B - JPQL joins

    GET  /api/doctors/by-specialization?name=cardiology
    GET  /api/doctors/without-office
    GET  /api/specializations/unused
    GET  /api/patients/of-doctor/1

### Part C - aggregates and bulk

    GET    /api/appointments/stats/by-status
    GET    /api/patients/frequent?min=2
    GET    /api/offices/busiest
    PATCH  /api/appointments/cancel-day?doctorId=1&date=2026-11-01

### Bonus

    GET    /api/appointments/page?page=0&size=5&sort=appointmentDate,desc
    DELETE /api/appointments/cancelled-before?date=2026-11-01

## Seed data

- Specializations: Cardiology, Pediatrics, Dermatology, Neurology
- Doctors: Regis Mugisha, Prince Habimana, Pierrine Uwase
- Patients: Ikirezi Uwase, Regis Uwase, Prince Keza, Pierrine Mukamana, Alice Brown

## Testing

Import `clinic-quiz.postman_collection.json` into Postman and run the requests in order.
