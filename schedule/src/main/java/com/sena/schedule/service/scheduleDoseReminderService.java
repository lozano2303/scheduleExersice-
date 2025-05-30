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

            // Verificar status actualizado por posible condición de carrera
            Optional<scheduleDose> refreshedDoseOpt = scheduleDoseRepository.findById(dose.getDoseID());
            if (refreshedDoseOpt.isPresent() && refreshedDoseOpt.get().getConfirmationStatus() != 0) {
                continue;
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

    // Ejecuta cada minuto para verificar si es momento de crear una nueva dosis
    @Scheduled(cron = "0 * * * * *")
    public void createNextDoseIfNeeded() {
        List<scheduleDose> confirmedDoses = scheduleDoseRepository.findByConfirmationStatus(1); // 1 = confirmada

        for (scheduleDose dose : confirmedDoses) {
            // Buscar los reminders de esta dosis
            List<reminder> reminders = reminderRepository.findByDoseID_DoseID(dose.getDoseID());
            // Buscar el último reminder CONFIRMADO (status == true)
            reminder lastConfirmedReminder = reminders.stream()
                .filter(r -> Boolean.TRUE.equals(r.getStatus()))
                .sorted((a, b) -> b.getSendAt().compareTo(a.getSendAt())) // descendente por fecha
                .findFirst()
                .orElse(null);

            if (lastConfirmedReminder == null) continue; // Solo para dosis que sí han sido confirmadas

            LocalDateTime lastConfirmation = lastConfirmedReminder.getSendAt();
            int freqHours = dose.getmedication().getFrequencyHours();
            LocalDateTime nextDoseTime = lastConfirmation.plusMinutes(freqHours);

            // EVITAR DUPLICADOS: solo crea si NO existe una dosis igual pendiente
            boolean exists = scheduleDoseRepository.existsByPatientAndMedicationAndStartDateAndConfirmationStatus(
                dose.getpatient(), dose.getmedication(), nextDoseTime, 0
            );
            if (exists) {
                continue;
            }

            if (!LocalDateTime.now().isBefore(nextDoseTime)) {
                // Crear la nueva dosis pendiente para el paciente y medicamento
                scheduleDose newDose = new scheduleDose();
                newDose.setmedication(dose.getmedication());
                newDose.setpatient(dose.getpatient());
                newDose.setStartDate(nextDoseTime);
                newDose.setConfirmationStatus(0); // pendiente
                newDose.setDurationDays(dose.getDurationDays()); // copia duración

                scheduleDoseRepository.save(newDose);
                System.out.println("Nueva dosis creada para paciente " + newDose.getpatient().getPatientID() +
                    " y medicamento " + newDose.getmedication().getName() +
                    " con fecha " + nextDoseTime);
            }
        }
    }
}