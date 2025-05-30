package com.sena.schedule.DTO;

public class patientDTO {

    private String name;
    private String email;
    private boolean notificationPermission;

    public patientDTO(String name, String email, boolean notificationPermission){
        this.name = name;
        this.email = email;
        this.notificationPermission = notificationPermission;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isNotificationPermission() {
        return notificationPermission;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNotificationPermission(boolean notificationPermission) {
        this.notificationPermission = notificationPermission;
    }


}
