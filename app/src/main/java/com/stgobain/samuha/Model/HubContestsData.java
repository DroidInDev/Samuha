package com.stgobain.samuha.Model;

/**
 * Created by vignesh on 28-06-2017.
 */

public class HubContestsData {
    private String id;
    private String name;
    private String contestDate;
    private String contestLocation;
    private String contestShortDesc;
    private String contestType;
    private String dateString;
    private String timeString;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContestDate() {
        return contestDate;
    }

    public void setContestDate(String contestDate) {
        this.contestDate = contestDate;
    }

    public String getContestLocation() {
        return contestLocation;
    }

    public void setContestLocation(String contestLocation) {
        this.contestLocation = contestLocation;
    }

    public String getContestShortDesc() {
        return contestShortDesc;
    }

    public void setContestShortDesc(String contestShortDesc) {
        this.contestShortDesc = contestShortDesc;
    }

    public String getContestType() {
        return contestType;
    }

    public void setContestType(String contestType) {
        this.contestType = contestType;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }
}
