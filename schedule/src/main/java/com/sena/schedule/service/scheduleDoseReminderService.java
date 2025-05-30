package com.sena.schedule.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sena.schedule.model.patient;
import com.sena.schedule.model.reminder;
import com.sena.schedule.model.scheduleDose;
import com.sena.schedule.repository.Ireminder;
import com.sena.schedule.repository.IscheduleDose;

@Service
public class scheduleDoseReminderService {

    @Autowired
    private IscheduleDose scheduleDoseRepository;

    @Autowired
    private Ireminder reminderRepository;

    @Autowired
    private emailService emailService;

    // Ejecuta cada 5 minutos para enviar recordatorios si corresponde
    @Scheduled(cron = "0 */5 * * * *")
    public void checkUnconfirmedDoses() {
        List<scheduleDose> unconfirmedDoses = scheduleDoseRepository.findByConfirmationStatus(0); // 0 = pendiente

        for (scheduleDose dose : unconfirmedDoses) {
            // Buscar el primer reminder de esta dosis
            Optional<reminder> primerReminderOpt = reminderRepository.findFirstByDoseID_DoseIDOrderBySendAtAsc(dose.getDoseID());
            LocalDateTime ahora = LocalDateTime.now();

            if (primerReminderOpt.isPresent()) {
                reminder primerReminder = primerReminderOpt.get();
                LocalDateTime primerEnvio = primerReminder.getSendAt();

                // Si han pasado al menos 9 minutos desde el primer reminder y sigue pendiente, marcar como "no tomado"
                if (primerEnvio != null && primerEnvio.plusMinutes(9).isBefore(ahora)) {
                    dose.setConfirmationStatus(2); // 2 = no tomado
                    scheduleDoseRepository.save(dose);
                    System.out.println("Dosis con ID " + dose.getDoseID() + " marcada como NO TOMADA (no se enviará más recordatorio).");
                    continue; // No enviar más recordatorios
                }
            }

            // Si no se ha cumplido el tiempo límite, se puede enviar el siguiente recordatorio
            patient patient = dose.getpatient();
            String email = patient.getEmail();
            if (patient.isNotificationPermission() && email != null && !email.isEmpty()) {
                emailService.sendDoseReminder(email, dose);
                System.out.println("Recordatorio enviado a: " + email + " para la dosis con ID: " + dose.getDoseID());
            }
        }
    }
}