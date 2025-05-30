package com.sena.schedule.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminder")
public class reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reminderID", nullable = false)
    private Integer reminderID;

    @ManyToOne
    @JoinColumn(name = "doseID", nullable = false)
    private scheduleDose doseID;

    @Column(name = "sendAt", nullable = false)
    private LocalDateTime sendAt;

    @Column(name = "status", nullable = false)
    private Boolean status;

    // Getters y Setters

    public Integer getReminderID() {
        return reminderID;
    }

    public void setReminderID(Integer reminderID) {
        this.reminderID = reminderID;
    }

    public scheduleDose getDoseID() {
        return doseID;
    }

    public void setDoseID(scheduleDose doseID) {
        this.doseID = doseID;
    }

    public LocalDateTime getSendAt() {
        return sendAt;
    }

    public void setSendAt(LocalDateTime sendAt) {
        this.sendAt = sendAt;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
