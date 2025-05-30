package com.sena.schedule.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name="medication")
public class medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="medicationID")
    private int medicationID;

    @Column(name="name", nullable=false, length=50)
    private String name;

    @Column(name="dosage", nullable=false, length=10)
    private String dosage;

    @Column(name="frequencyHours", nullable=false)
    private int frequencyHours;

    public medication(){

    }

    public medication(int medicationID, String name, String dosage, int frequencyHours){
        this.medicationID = medicationID;
        this.name = name;
        this.dosage = dosage;
        this.frequencyHours = frequencyHours;

    }

    public int getMedicationID() {
        return medicationID;
    }

    public String getName() {
        return name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setMedicationID(int medicationID) {
        this.medicationID = medicationID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
    
    public int getFrequencyHours() {
        return frequencyHours;
    }
    public void setFrequencyHours(int frequencyHours) {
        this.frequencyHours = frequencyHours;
    }

    

}
