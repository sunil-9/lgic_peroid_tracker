package com.example.periodtracker.model;

public class HistoryModel {
    String id,cycleStartDate,periodCycle,PeriodCycle;

    public HistoryModel() {
    }

    public HistoryModel(String id, String cycleStartDate, String periodCycle, String periodCycle1) {
        this.id = id;
        this.cycleStartDate = cycleStartDate;
        this.periodCycle = periodCycle;
        PeriodCycle = periodCycle1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCycleStartDate() {
        return cycleStartDate;
    }

    public void setCycleStartDate(String cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
    }

    public String getPeriodCycle() {
        return periodCycle;
    }

    public void setPeriodCycle(String periodCycle) {
        this.periodCycle = periodCycle;
    }
}
