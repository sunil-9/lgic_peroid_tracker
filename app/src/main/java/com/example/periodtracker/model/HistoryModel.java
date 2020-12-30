package com.example.periodtracker.model;

public class HistoryModel {
    String id,user_id,cycleStartDate,periodLength;
    Long startTime;

    public HistoryModel() {
    }

    public HistoryModel(String id, String cycleStartDate, String periodLength, String user_id, Long startTime) {
        this.id = id;
        this.user_id=user_id;
        this.cycleStartDate = cycleStartDate;
        this.periodLength = periodLength;
        this.startTime = startTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(String cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

    public String getPeriodLength() {
        return periodLength;
    }

    public void setPeriodLength(String periodLength) {
        this.periodLength = periodLength;
    }


}
