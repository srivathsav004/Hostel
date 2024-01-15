package com.example.hostel;

public class StudentModel {
    public String username;
    public String name;
    public String email;
    public String registrationNumber;
    public String password;
    public String hostel;

    // Default constructor required for calls to DataSnapshot.getValue(StudentModel.class)
    public StudentModel() {
    }

    public StudentModel(String username, String name, String email, String registrationNumber, String password, String hostel) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.registrationNumber = registrationNumber;
        this.password = password;
        this.hostel = hostel;
    }
}
