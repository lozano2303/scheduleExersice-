package com.sena.schedule.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sena.schedule.DTO.medicationDTO;
import com.sena.schedule.DTO.responseDTO;
import com.sena.schedule.model.medication;
import com.sena.schedule.repository.Imedication;

@Service
public class medicationService {

    @Autowired
    private Imedication data;

    // Guardar medicationo
    public responseDTO save(medicationDTO medicationDTO) {
        if (!validatemedication(medicationDTO)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del medicationo inválidos");
        }
        medication medicationRegister = convertToModel(medicationDTO);
        data.save(medicationRegister);
        return new responseDTO(HttpStatus.OK.toString(), "medicationo guardado exitosamente");
    }

    // Obtener todos los medicationos
    public List<medication> findAll() {
        return data.findAll();
    }

    // Buscar medicationo por ID
    public Optional<medication> findById(int id) {
        return data.findById(id);
    }

    // Actualizar medicationo
    public responseDTO update(int id, medicationDTO medicationDTO) {
        Optional<medication> existingmedication = findById(id);
        if (!existingmedication.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "El medicationo no existe");
        }
        if (!validatemedication(medicationDTO)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del medicationo inválidos");
        }
        medication medicationToUpdate = existingmedication.get();
        medicationToUpdate.setName(medicationDTO.getName());
        medicationToUpdate.setDosage(medicationDTO.getDosage());
        medicationToUpdate.setFrequencyHours(medicationDTO.getFrequencyHours());
        data.save(medicationToUpdate);
        return new responseDTO(HttpStatus.OK.toString(), "medicationo actualizado correctamente");
    }

    // Eliminar medicationo
    public responseDTO deleteMedication(int id) {
        Optional<medication> medication = findById(id);
        if (!medication.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "El medicationo no existe");
        }
        data.delete(medication.get());
        return new responseDTO(HttpStatus.OK.toString(), "medicationo eliminado correctamente");
    }

    // Filtrar medicationos por nombre
    public List<medication> filterMedication(String name) {
        List<medication> allmedications = data.findAll();
        if (name == null || name.isEmpty()) {
            return allmedications;
        }
        return allmedications.stream()
                .filter(m -> m.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Validaciones
    private boolean validatemedication(medicationDTO dto) {
        return dto.getName() != null && !dto.getName().isBlank() &&
               dto.getDosage() != null && !dto.getDosage().isBlank() &&
               dto.getFrequencyHours() > 0;
    }

    // Conversión de DTO a modelo
    public medication convertToModel(medicationDTO dto) {
        return new medication(
            0, // ID autogenerado
            dto.getName(),
            dto.getDosage(),
            dto.getFrequencyHours()
        );
    }

    // Conversión de modelo a DTO
    public medicationDTO convertToDTO(medication m) {
        return new medicationDTO(
            m.getName(),
            m.getDosage(),
            m.getFrequencyHours()
        );
    }
}