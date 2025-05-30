package com.sena.schedule.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sena.schedule.DTO.reminderDTO;
import com.sena.schedule.DTO.responseDTO;
import com.sena.schedule.model.reminder;
import com.sena.schedule.model.scheduleDose;
import com.sena.schedule.repository.Ireminder;
import com.sena.schedule.repository.IscheduleDose;

@Service
public class reminderService {

    @Autowired
    private Ireminder data;

    @Autowired
    private IscheduleDose scheduleDoseRepo;

    // Crear nuevo reminder (para historial)
    public reminder crearNuevoReminder(scheduleDose dose) {
        reminder r = new reminder();
        r.setDoseID(dose);
        r.setSendAt(LocalDateTime.now());
        r.setStatus(false); // o 0 si usas int
        return data.save(r);
    }

    // Marcar reminder como confirmado por ID
    public void marcarComoConfirmado(Integer reminderId) {
        Optional<reminder> reminderOpt = data.findById(reminderId);
        if (reminderOpt.isPresent()) {
            reminder reminder = reminderOpt.get();
            reminder.setStatus(true); // o 1 si usas int
            data.save(reminder);
        }
    }

    // Listar reminders por dosis (historial)
    public List<reminder> listarHistorialPorDoseId(Integer doseId) {
        return data.findByDoseID_DoseID(doseId);
    }

    // --- Los métodos anteriores pueden seguir igual, si los usas para otras funciones ---
    public responseDTO save(reminderDTO reminderDTO) {
        if (!validatereminder(reminderDTO)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del recordatorio inválidos");
        }
        Optional<scheduleDose> doseOpt = scheduleDoseRepo.findById(reminderDTO.getDoseID());
        if (!doseOpt.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "No existe la dosis asociada");
        }
        reminder reminderRegister = convertToModel(reminderDTO, doseOpt.get());
        data.save(reminderRegister);
        return new responseDTO(HttpStatus.OK.toString(), "Recordatorio guardado exitosamente");
    }

    public List<reminder> findAll() {
        return data.findAll();
    }

    public Optional<reminder> findById(int id) {
        return data.findById(id);
    }

    public responseDTO update(int id, reminderDTO reminderDTO) {
        Optional<reminder> existingreminder = findById(id);
        if (!existingreminder.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "El recordatorio no existe");
        }
        if (!validatereminder(reminderDTO)) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "Datos del recordatorio inválidos");
        }
        Optional<scheduleDose> doseOpt = scheduleDoseRepo.findById(reminderDTO.getDoseID());
        if (!doseOpt.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "No existe la dosis asociada");
        }
        reminder reminderToUpdate = existingreminder.get();
        reminderToUpdate.setDoseID(doseOpt.get());
        reminderToUpdate.setSendAt(reminderDTO.getSendAt());
        reminderToUpdate.setStatus(reminderDTO.getStatus());
        data.save(reminderToUpdate);
        return new responseDTO(HttpStatus.OK.toString(), "Recordatorio actualizado correctamente");
    }

    public responseDTO deletereminder(int id) {
        Optional<reminder> reminder = findById(id);
        if (!reminder.isPresent()) {
            return new responseDTO(HttpStatus.BAD_REQUEST.toString(), "El recordatorio no existe");
        }
        data.delete(reminder.get());
        return new responseDTO(HttpStatus.OK.toString(), "Recordatorio eliminado correctamente");
    }

    private boolean validatereminder(reminderDTO dto) {
        return dto.getDoseID() != null && dto.getSendAt() != null && dto.getStatus() != null;
    }

    public reminder convertToModel(reminderDTO dto, scheduleDose dose) {
        reminder r = new reminder();
        r.setDoseID(dose);
        r.setSendAt(dto.getSendAt());
        r.setStatus(dto.getStatus());
        return r;
    }

    public reminderDTO convertToDTO(reminder r) {
        return new reminderDTO(
            r.getDoseID().getDoseID(),
            r.getSendAt(),
            r.getStatus()
        );
    }
}