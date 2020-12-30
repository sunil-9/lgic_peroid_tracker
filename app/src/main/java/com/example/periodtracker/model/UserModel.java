package com.example.periodtracker.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserModel {
    String userId,username,email,cycle,length,last_date_day,last_date_month,last_date_year,imageUrl;
    Long timeInMillis;

    public UserModel() {
    }

    public UserModel(String userId, String username, String email, String cycle, String length, String last_date_day, String last_date_month, String last_date_year, String imageUrl, Long timeInMillis) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.cycle = cycle;
        this.length = length;
        this.last_date_day = last_date_day;
        this.last_date_month = last_date_month;
        this.last_date_year = last_date_year;
        this.imageUrl = imageUrl;
        this.timeInMillis =timeInMillis;
    }

    public Long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(Long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLast_date_day() {
        return last_date_day;
    }

    public void setLast_date_day(String last_date_day) {
        this.last_date_day = last_date_day;
    }

    public String getLast_date_month() {
        return last_date_month;
    }

    public void setLast_date_month(String last_date_month) {
        this.last_date_month = last_date_month;
    }

    public String getLast_date_year() {
        return last_date_year;
    }

    public void setLast_date_year(String last_date_year) {
        this.last_date_year = last_date_year;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userId", userId);
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("cycle", cycle);
        hashMap.put("length", length);
        hashMap.put("last_date_day", last_date_day);
        hashMap.put("last_date_month", last_date_month);
        hashMap.put("last_date_year", last_date_year);
        hashMap.put("imageUrl", "default");
        return hashMap;
    }
}
