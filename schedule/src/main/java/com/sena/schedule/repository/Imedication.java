package com.sena.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.schedule.model.medication;

@Repository
public interface Imedication extends JpaRepository<medication, Integer> {
    // Métodos personalizados si los necesitas (por ejemplo, buscar por nombre)
    // Optional<medicament> findByName(String name);
}