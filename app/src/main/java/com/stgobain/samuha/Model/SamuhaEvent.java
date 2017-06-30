package com.stgobain.samuha.Model;

/**
 * Created by vignesh on 15-06-2017.
 */

public class SamuhaEvent {
    public String id;
    public String eventDate;
    public String eventName;
    public String eventLocation;
    public String imageUrl;
    public String shortDescribtion;
    public String latitude;
    public String locUrl;
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongititude() {
        return longititude;
    }

    public void setLongititude(String longititude) {
        this.longititude = longititude;
    }

    public String longititude;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShortDescribtion() {
        return shortDescribtion;
    }

    public void setShortDescribtion(String shortDescribtion) {
        this.shortDescribtion = shortDescribtion;
    }

    public String getLocUrl() {
        return locUrl;
    }

    public void setLocUrl(String locUrl) {
        this.locUrl = locUrl;
    }
}
