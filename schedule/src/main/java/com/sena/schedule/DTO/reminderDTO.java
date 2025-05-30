package com.sena.schedule.DTO;

import java.time.LocalDateTime;

public class reminderDTO {
    private Integer doseID;
    private LocalDateTime sendAt;
    private Boolean status;

    public reminderDTO() {}

    public reminderDTO(Integer doseID, LocalDateTime sendAt, Boolean status) {
        this.doseID = doseID;
        this.sendAt = sendAt;
        this.status = status;
    }

    public Integer getDoseID() {
        return doseID;
    }

    public void setDoseID(Integer doseID) {
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