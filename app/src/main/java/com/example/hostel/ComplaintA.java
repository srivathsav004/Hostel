package com.example.hostel;

public class ComplaintA {
    private String title;
    private String complaint;

    // Default constructor (required for Firebase)
    public ComplaintA() {
    }

    public ComplaintA(String title, String complaint) {
        this.title = title;
        this.complaint = complaint;
    }

    // Getters and setters (if necessary)
    public String getTitle() {
        return title;
    }

    public String getComplaint() {
        return complaint;
    }
}

