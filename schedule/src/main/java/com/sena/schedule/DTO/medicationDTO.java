package com.sena.schedule.DTO;

public class medicationDTO {
    private String name;
    private String dosage;
    private int frequencyHours;

    public medicationDTO() {}

    public medicationDTO(String name, String dosage, int frequencyHours) {
        this.name = name;
        this.dosage = dosage;
        this.frequencyHours = frequencyHours;
    }

    public String getName() {
        return name;
    }

    public String getDosage() {
        return dosage;
    }

    public int getFrequencyHours() {
        return frequencyHours;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setFrequencyHours(int frequencyHours) {
        this.frequencyHours = frequencyHours;
    }
}