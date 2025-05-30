package com.sena.schedule.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sena.schedule.DTO.reminderDTO;
import com.sena.schedule.DTO.responseDTO;
import com.sena.schedule.model.reminder;
import com.sena.schedule.service.reminderService;

@RestController
@RequestMapping("/api/reminders")
public class reminderController {

    @Autowired
    private reminderService reminderService;

    // Crear recordatorio
    @PostMapping
    public ResponseEntity<responseDTO> createreminder(@RequestBody reminderDTO reminderDTO) {
        responseDTO response = reminderService.save(reminderDTO);
        return ResponseEntity.status(response.getStatus().equals("200 OK") ? 200 : 400).body(response);
    }

    // Obtener todos los recordatorios
    @GetMapping
    public List<reminder> getAllreminders() {
        return reminderService.findAll();
    }

    // Obtener recordatorio por ID
    @GetMapping("/{id}")
    public ResponseEntity<reminder> getreminderById(@PathVariable int id) {
        Optional<reminder> reminder = reminderService.findById(id);
        return reminder.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar recordatorio
    @PutMapping("/{id}")
    public ResponseEntity<responseDTO> updatereminder(@PathVariable int id, @RequestBody reminderDTO reminderDTO) {
        responseDTO response = reminderService.update(id, reminderDTO);
        return ResponseEntity.status(response.getStatus().equals("200 OK") ? 200 : 400).body(response);
    }

    // Eliminar recordatorio
    @DeleteMapping("/{id}")
    public ResponseEntity<responseDTO> deletereminder(@PathVariable int id) {
        responseDTO response = reminderService.deletereminder(id);
        return ResponseEntity.status(response.getStatus().equals("200 OK") ? 200 : 400).body(response);
    }
}