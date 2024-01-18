package com.example.myapplication;

public class Appointment {
    private String Date;
    private String Hour;
    private String Doctor;
    private String Name;

    private String doc_type;

    private String doc_email;

    private String client_email;


    public Appointment()
    {
        this.Date = "";
        this.Hour = "";
        this.Doctor = "";
        this.Name = "";
        this.doc_type = "";
        this.doc_email = "";
        this.client_email = "";
    }
    // Constructors
    public Appointment(String date, String time, String doctor, String userName, String s, String type, String doc_email, String client_email ) {
        this.Date = date;
        this.Hour = time;
        this.Doctor = doctor;
        this.Name = userName;
        this.doc_type = type;
        this.doc_email = doc_email;
        this.client_email = client_email;
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

    public String getDocEmail() {
        return doc_email;
    }

    public String getClientEmail() {
        return client_email;
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

    public void setDocEmail(String docEmail) {
        this.doc_email = docEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.client_email = clientEmail;
    }
}
