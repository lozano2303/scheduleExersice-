package com.sena.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.schedule.model.patient;

@Repository
public interface Ipatient extends JpaRepository<patient, Integer> {
    // Puedes agregar métodos personalizados si los necesitas (por ejemplo, buscar por email)
    // Optional<patient> findByEmail(String email);
}