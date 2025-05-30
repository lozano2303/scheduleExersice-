package com.sena.schedule.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sena.schedule.DTO.scheduleDoseDTO;
import com.sena.schedule.DTO.responseDTO;
import com.sena.schedule.model.scheduleDose;
import com.sena.schedule.service.scheduleDoseService;

@RestController
@RequestMapping("/api/scheduledoses")
public class scheduleDoseController {

    @Autowired
    private scheduleDoseService scheduleDoseService;

    // Crear dosis agendada
    @PostMapping
    public ResponseEntity<responseDTO> createscheduleDose(@RequestBody scheduleDoseDTO dto) {
        responseDTO response = scheduleDoseService.save(dto);
        return ResponseEntity.status(response.getStatus().equals("200 OK") ? 200 : 400).body(response);
    }

    // Obtener todas las dosis agendadas
    @GetMapping
    public List<scheduleDose> getAllscheduleDoses() {
        return scheduleDoseService.findAll();
    }

    // Obtener dosis agendada por ID
    @GetMapping("/{id}")
    public ResponseEntity<scheduleDose> getscheduleDoseById(@PathVariable int id) {
        Optional<scheduleDose> obj = scheduleDoseService.findById(id);
        return obj.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar dosis agendada
    @PutMapping("/{id}")
    public ResponseEntity<responseDTO> updatescheduleDose(@PathVariable int id, @RequestBody scheduleDoseDTO dto) {
        responseDTO response = scheduleDoseService.update(id, dto);
        return ResponseEntity.status(response.getStatus().equals("200 OK") ? 200 : 400).body(response);
    }

    // Eliminar dosis agendada
    @DeleteMapping("/{id}")
    public ResponseEntity<responseDTO> deletescheduleDose(@PathVariable int id) {
        responseDTO response = scheduleDoseService.deletescheduleDose(id);
        return ResponseEntity.status(response.getStatus().equals("200 OK") ? 200 : 400).body(response);
    }
}