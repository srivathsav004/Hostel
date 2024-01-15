package com.example.hostel;
public class AdminModel {
    public String username;
    public String name;
    public String password;
    public String hostel;

    // Default constructor required for calls to DataSnapshot.getValue(AdminModel.class)
    public AdminModel() {
    }
    public AdminModel(String username, String name, String password, String hostel) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.hostel = hostel;
    }
}

