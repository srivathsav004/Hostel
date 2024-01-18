package com.example.hostel;

public class StudentModel {
    public String username;
    public String name;
    public String email;
    public String registrationNumber;
    public String password;
    public String hostel;
    public String profileImageUrl;  // Added field for profile image URL
    public String roomNumber;  // Added field for room number
    public String department;


    public String getDepartment(){ return department;}

    public void setDepartment(String selectedDepartment) {
        this.department= selectedDepartment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    // Default constructor required for calls to DataSnapshot.getValue(StudentModel.class)
    public StudentModel() {
    }
    public StudentModel(String username, String name, String email, String registrationNumber, String password,String roomNumber, String hostel,String selectedDepartment) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.registrationNumber = registrationNumber;
        this.password = password;
        this.roomNumber=roomNumber;
        this.hostel = hostel;
        this.department=selectedDepartment;
    }

}
