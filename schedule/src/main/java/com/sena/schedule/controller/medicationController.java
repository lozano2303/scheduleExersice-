package com.sena.schedule.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sena.schedule.DTO.medicationDTO;
import com.sena.schedule.DTO.responseDTO;
import com.sena.schedule.model.medication;
import com.sena.schedule.service.medicationService;

@RestController
@RequestMapping("/api/medications")
public class medicationController {

    @Autowired
    private medicationService medicationService;

    // Crear medicationo
    @PostMapping
    public ResponseEntity<responseDTO> createMedication(@RequestBody medicationDTO medicationDTO) {
        responseDTO response = medicationService.save(medicationDTO);
        return ResponseEntity.status(response.getStatus().equals("200 OK") ? 200 : 400).body(response);
    }

    // Obtener todos los medicationos
    @GetMapping
    public List<medication> getAllMedications() {
        return medicationService.findAll();
    }

    // Obtener medicationo por ID
    @GetMapping("/{id}")
    public ResponseEntity<medication> getMedicationById(@PathVariable int id) {
        Optional<medication> medication = medicationService.findById(id);
        return medication.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar medicationo
    @PutMapping("/{id}")
    public ResponseEntity<responseDTO> updateMedication(@PathVariable int id, @RequestBody medicationDTO medicationDTO) {
        responseDTO response = medicationService.update(id, medicationDTO);
        return ResponseEntity.status(response.getStatus().equals("200 OK") ? 200 : 400).body(response);
    }

    // Eliminar medicationo
    @DeleteMapping("/{id}")
    public ResponseEntity<responseDTO> deleteMedication(@PathVariable int id) {
        responseDTO response = medicationService.deleteMedication(id);
        return ResponseEntity.status(response.getStatus().equals("200 OK") ? 200 : 400).body(response);
    }

    // Filtrar medicationos por nombre
    @GetMapping("/filter")
    public List<medication> filterMedications(@RequestParam(required = false) String name) {
        return medicationService.filterMedication(name);
    }
}