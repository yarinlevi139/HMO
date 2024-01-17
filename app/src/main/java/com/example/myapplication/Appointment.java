package com.example.myapplication;

public class Appointment {
    private String Date;
    private String Hour;
    private String Doctor;
    private String Name;

    private String doc_type;

    // Constructors
    public Appointment(String date, String time, String doctor, String userName, String s, String type) {
        this.Date = date;
        this.Hour = time;
        this.Doctor = doctor;
        this.Name = userName;
        this.doc_type = type;
    }

    // Getters
    public String getDate() {
        return Date;
    }

    public String getHour() {
        return Hour;
    }

    public String getDoctor() {
        return Doctor;
    }

    public String getName() {
        return Name;
    }

    public String getDocType() {
        return doc_type;
    }

    // Setters
    public void setDate(String date) {
        this.Date = date;
    }

    public void setHour(String hour) {
        this.Hour = hour;
    }

    public void setDoctor(String doctor) {
        this.Doctor = doctor;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setDocType(String type) {
        this.doc_type = type;
    }
}
