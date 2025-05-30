package com.sena.schedule.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sena.schedule.DTO.patientDTO;
import com.sena.schedule.DTO.responseDTO;
import com.sena.schedule.model.patient;
import com.sena.schedule.repository.Ipatient;

@Service
public class patientService {

    @Autowired
    private Ipatient data;

    // Guardar paciente
    public responseDTO save(patientDTO patientDTO) {
        if (!validatePatient(patientDTO)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del paciente inválidos");
        }

        patient patientRegister = convertToModel(patientDTO);
        data.save(patientRegister);

        return new responseDTO(HttpStatus.OK.toString(), "Paciente guardado exitosamente");
    }

    // Obtener todos los pacientes
    public List<patient> findAll() {
        return data.findAll();
    }

    // Buscar paciente por ID
    public Optional<patient> findById(int id) {
        return data.findById(id);
    }

    // Actualizar paciente
    public responseDTO update(int id, patientDTO patientDTO) {
        Optional<patient> existingPatient = findById(id);
        if (!existingPatient.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "El paciente no existe");
        }

        if (!validatePatient(patientDTO)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del paciente inválidos");
        }

        patient patientToUpdate = existingPatient.get();
        patientToUpdate.setName(patientDTO.getName());
        patientToUpdate.setEmail(patientDTO.getEmail());
        patientToUpdate.setNotificationPermission(patientDTO.isNotificationPermission());

        data.save(patientToUpdate);

        return new responseDTO(HttpStatus.OK.toString(), "Paciente actualizado correctamente");
    }

    // Eliminar paciente
    public responseDTO deletePatient(int id) {
        Optional<patient> patient = findById(id);
        if (!patient.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "El paciente no existe");
        }

        data.delete(patient.get());

        return new responseDTO(HttpStatus.OK.toString(), "Paciente eliminado correctamente");
    }

    // Filtrar pacientes por nombre
    public List<patient> filterPatient(String name) {
        List<patient> allPatients = data.findAll();

        if (name == null || name.isEmpty()) {
            return allPatients;
        }

        return allPatients.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Validaciones
    private boolean validatePatient(patientDTO dto) {
        return dto.getName() != null && !dto.getName().isBlank()
                && dto.getEmail() != null && !dto.getEmail().isBlank();
    }

    // Conversión de DTO a modelo
    public patient convertToModel(patientDTO dto) {
        return new patient(
            0, // ID autogenerado
            dto.getName(),
            dto.getEmail(),
            dto.isNotificationPermission()
        );
    }

    // Conversión de modelo a DTO
    public patientDTO convertToDTO(patient p) {
        return new patientDTO(
            p.getName(),
            p.getEmail(),
            p.isNotificationPermission()
        );
    }
}
