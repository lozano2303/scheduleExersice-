package com.sena.schedule.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sena.schedule.DTO.responseDTO;
import com.sena.schedule.DTO.scheduleDoseDTO;
import com.sena.schedule.model.medication;
import com.sena.schedule.model.patient;
import com.sena.schedule.model.scheduleDose;
import com.sena.schedule.repository.Imedication;
import com.sena.schedule.repository.Ipatient;
import com.sena.schedule.repository.IscheduleDose;

@Service
public class scheduleDoseService {

    @Autowired
    private IscheduleDose data;

    @Autowired
    private Imedication medicationRepo;

    @Autowired
    private Ipatient patientRepo;

    // Guardar scheduleDose
    public responseDTO save(scheduleDoseDTO dto) {
        if (!validatescheduleDose(dto)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos de la dosis inválidos");
        }
        Optional<medication> medicationOpt = medicationRepo.findById(dto.getMedicationID());
        Optional<patient> patientOpt = patientRepo.findById(dto.getPatientID());
        if (!medicationOpt.isPresent() || !patientOpt.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Paciente o medicamento no existe");
        }
        scheduleDose scheduleDoseRegister = convertToModel(dto, medicationOpt.get(), patientOpt.get());
        data.save(scheduleDoseRegister);
        return new responseDTO(HttpStatus.OK.toString(), "Dosis agendada exitosamente");
    }

    // Obtener todas las dosis agendadas
    public List<scheduleDose> findAll() {
        return data.findAll();
    }

    // Buscar dosis agendada por ID
    public Optional<scheduleDose> findById(int id) {
        return data.findById(id);
    }

    // Actualizar dosis agendada
    public responseDTO update(int id, scheduleDoseDTO dto) {
        Optional<scheduleDose> existing = findById(id);
        if (!existing.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "La dosis agendada no existe");
        }
        if (!validatescheduleDose(dto)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos de la dosis inválidos");
        }
        Optional<medication> medicationOpt = medicationRepo.findById(dto.getMedicationID());
        Optional<patient> patientOpt = patientRepo.findById(dto.getPatientID());
        if (!medicationOpt.isPresent() || !patientOpt.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Paciente o medicamento no existe");
        }
        scheduleDose toUpdate = existing.get();
        toUpdate.setmedication(medicationOpt.get());
        toUpdate.setpatient(patientOpt.get());
        toUpdate.setStartDate(dto.getStartDate());
        toUpdate.setConfirmationStatus(dto.getConfirmationStatus());
        toUpdate.setDurationDays(dto.getDurationDays());
        data.save(toUpdate);
        return new responseDTO(HttpStatus.OK.toString(), "Dosis agendada actualizada correctamente");
    }

    // Eliminar dosis agendada
    public responseDTO deletescheduleDose(int id) {
        Optional<scheduleDose> obj = findById(id);
        if (!obj.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "La dosis agendada no existe");
        }
        data.delete(obj.get());
        return new responseDTO(HttpStatus.OK.toString(), "Dosis agendada eliminada correctamente");
    }

    // Validaciones
    private boolean validatescheduleDose(scheduleDoseDTO dto) {
        return dto.getMedicationID() != null && dto.getPatientID() != null &&
               dto.getStartDate() != null && dto.getDurationDays() != null && dto.getDurationDays() > 0 &&
               dto.getConfirmationStatus() != null;
    }

    // Conversión de DTO a modelo
    public scheduleDose convertToModel(scheduleDoseDTO dto, medication medication, patient patient) {
        scheduleDose sd = new scheduleDose();
        sd.setmedication(medication);
        sd.setpatient(patient);
        sd.setStartDate(dto.getStartDate());
        sd.setConfirmationStatus(dto.getConfirmationStatus());
        sd.setDurationDays(dto.getDurationDays());
        return sd;
    }

    // Conversión de modelo a DTO
    public scheduleDoseDTO convertToDTO(scheduleDose sd) {
        return new scheduleDoseDTO(
            sd.getmedication().getMedicationID(),
            sd.getpatient().getPatientID(),
            sd.getStartDate(),
            sd.getConfirmationStatus(),
            sd.getDurationDays()
        );
    }

}