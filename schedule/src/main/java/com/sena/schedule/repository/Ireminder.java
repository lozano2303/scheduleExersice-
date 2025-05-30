package com.sena.schedule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sena.schedule.model.reminder;

public interface Ireminder extends JpaRepository<reminder, Integer> {
    List<reminder> findByDoseID_DoseID(Integer doseId);

    // Nuevo método para encontrar el primer reminder (más antiguo) de una dosis
    Optional<reminder> findFirstByDoseID_DoseIDOrderBySendAtAsc(Integer doseId);
}