package com.sena.schedule.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sena.schedule.DTO.patientDTO;
import com.sena.schedule.DTO.responseDTO;
import com.sena.schedule.model.patient;
import com.sena.schedule.service.patientService;

@RestController
@RequestMapping("/api/patients")
public class patientController {

    @Autowired
    private patientService patientService;

    // Crear paciente
    @PostMapping
    public ResponseEntity<responseDTO> createPatient(@RequestBody patientDTO patientDTO) {
        responseDTO response = patientService.save(patientDTO);
        return ResponseEntity.status(response.getStatus().equals("200 OK") ? 200 : 400).body(response);
    }

    // Obtener todos los pacientes
    @GetMapping
    public List<patient> getAllPatients() {
        return patientService.findAll();
    }

    // Obtener paciente por ID
    @GetMapping("/{id}")
    public ResponseEntity<patient> getPatientById(@PathVariable int id) {
        Optional<patient> patient = patientService.findById(id);
        return patient.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar paciente
    @PutMapping("/{id}")
    public ResponseEntity<responseDTO> updatePatient(@PathVariable int id, @RequestBody patientDTO patientDTO) {
        responseDTO response = patientService.update(id, patientDTO);
        return ResponseEntity.status(response.getStatus().equals("200 OK") ? 200 : 400).body(response);
    }

    // Eliminar paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<responseDTO> deletePatient(@PathVariable int id) {
        responseDTO response = patientService.deletePatient(id);
        return ResponseEntity.status(response.getStatus().equals("200 OK") ? 200 : 400).body(response);
    }

    // Filtrar pacientes por nombre
    @GetMapping("/filter")
    public List<patient> filterPatients(@RequestParam(required = false) String name) {
        return patientService.filterPatient(name);
    }
}