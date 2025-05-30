package com.sena.schedule.DTO;

import com.sena.schedule.model.patient;

public class responseDTO {

    private String status; // Estado de la respuesta (success o error)
    private String message; // Mensaje descriptivo
    private String token; // Campo opcional para incluir un token
    private patient patient; // Campo opcional para información del paciente

    // Constructor para respuestas sin token ni paciente
    public responseDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Constructor para respuestas con token
    public responseDTO(String status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
    }

    // Constructor para respuestas con token y paciente
    public responseDTO(String status, String message, String token, patient patient) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.patient = patient;
    }

    // Getters y setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public patient getPatient() {
        return patient;
    }

    public void setPatient(patient patient) {
        this.patient = patient;
    }
}