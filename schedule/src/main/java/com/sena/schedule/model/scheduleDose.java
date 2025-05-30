package com.sena.schedule.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "scheduleDose")
public class scheduleDose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doseID", nullable = false)
    private Integer doseID;

    @ManyToOne
    @JoinColumn(name = "medicationID", nullable = false)
    private medication medication;

    @ManyToOne
    @JoinColumn(name = "patientID", nullable = false)
    private patient patient;

    @Column(name = "startDate", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "confirmationStatus", nullable = false)
    private Integer confirmationStatus; // 0=pending, 1=confirmed, 2=not taken

    @Column(name = "durationDays", nullable = false)
    private Integer durationDays;

    // Getters y Setters

    public Integer getDoseID() {
        return doseID;
    }

    public void setDoseID(Integer doseID) {
        this.doseID = doseID;
    }

    public medication getmedication() {
        return medication;
    }

    public void setmedication(medication medication) {
        this.medication = medication;
    }

    public patient getpatient() {
        return patient;
    }

    public void setpatient(patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(Integer confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }
}