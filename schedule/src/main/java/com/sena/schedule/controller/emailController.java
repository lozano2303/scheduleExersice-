package com.sena.schedule.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.schedule.model.reminder;
import com.sena.schedule.model.scheduleDose;
import com.sena.schedule.repository.IscheduleDose;
import com.sena.schedule.service.emailService;
import com.sena.schedule.service.reminderService;

@RestController
@RequestMapping("/email")
public class emailController {

    @Autowired
    private emailService emailService;

    @Autowired
    private IscheduleDose scheduleDoseRepository;

    @Autowired
    private reminderService reminderService;

    /**
     * Endpoint que envía el recordatorio de dosis al paciente.
     * Este es un ejemplo de uso; en producción, llama este método desde el servicio programado.
     */
    @GetMapping("/sendDoseReminder/{email}/{doseId}")
    public String sendDoseReminder(@PathVariable String email, @PathVariable Integer doseId) {
        Optional<scheduleDose> doseOpt = scheduleDoseRepository.findById(doseId);
        if (!doseOpt.isPresent()) {
            return "Dosis no encontrada";
        }
        emailService.sendDoseReminder(email, doseOpt.get());
        return "Mail sent successfully";
    }

    /**
     * Endpoint que confirma la toma de la dosis.
     * El enlace ahora recibe el reminderId como parámetro.
     * Ejemplo de confirmUrl: https://tudominio.com/email/confirmDose/456
     */
    @GetMapping("/confirmDose/{reminderId}")
    public String confirmDose(@PathVariable Integer reminderId) {
        // 1. Marcar el reminder como confirmado
        reminderService.marcarComoConfirmado(reminderId);

        // 2. También actualizar 'confirmationStatus' en la dosis relacionada
        Optional<reminder> reminderOpt = reminderService.findById(reminderId);
        if (reminderOpt.isPresent()) {
            scheduleDose dose = reminderOpt.get().getDoseID();
            dose.setConfirmationStatus(1); // 1 = confirmado
            scheduleDoseRepository.save(dose);
        }

        return "<html><body><h2>¡Gracias por confirmar tu dosis!</h2><p>No recibirás más recordatorios para esta toma.</p></body></html>";
    }
}