package com.sena.schedule.DTO;

import java.time.LocalDateTime;

public class scheduleDoseDTO {
    private Integer medicationID;
    private Integer patientID;
    private LocalDateTime startDate;
    private Integer confirmationStatus; // 0=pending, 1=confirmed, 2=not taken
    private Integer durationDays;

    public scheduleDoseDTO() {}

    public scheduleDoseDTO(Integer medicationID, Integer patientID, LocalDateTime startDate, Integer confirmationStatus, Integer durationDays) {
        this.medicationID = medicationID;
        this.patientID = patientID;
        this.startDate = startDate;
        this.confirmationStatus = confirmationStatus;
        this.durationDays = durationDays;
    }

    public Integer getMedicationID() {
        return medicationID;
    }

    public void setMedicationID(Integer medicationID) {
        this.medicationID = medicationID;
    }

    public Integer getPatientID() {
        return patientID;
    }

    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
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