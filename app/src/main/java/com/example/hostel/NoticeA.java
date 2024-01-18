package com.example.hostel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoticeA {
    private String title;
    private String imageUrl;
    private String dateTime;

    // Required empty constructor for Firebase
    public NoticeA() {
    }

    public NoticeA(String title, String imageUrl,String dateTime) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getDateTime() {
        return dateTime;
    }
    private String formatDate(Date dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(dateTime);
    }
}

