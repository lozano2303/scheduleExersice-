package com.sena.schedule.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.schedule.model.medication;
import com.sena.schedule.model.patient;
import com.sena.schedule.model.scheduleDose;

@Repository
public interface IscheduleDose extends JpaRepository<scheduleDose, Integer> {
    // 0=pending, 1=confirmed, 2=not taken
    List<scheduleDose> findByConfirmationStatus(Integer confirmationStatus);

    // Nuevo: para evitar duplicados
    boolean existsByPatientAndMedicationAndStartDateAndConfirmationStatus(
        patient patient, medication medication, LocalDateTime startDate, Integer confirmationStatus
    );
}