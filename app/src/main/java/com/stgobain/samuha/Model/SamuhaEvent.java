package com.stgobain.samuha.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vignesh on 15-06-2017.
 */

public class SamuhaEvent implements Parcelable {
    public String id;
    public String eventDate;
    public String eventName;
    public String eventLocation;
    public String imageUrl;
    public String shortDescribtion;
    public String latitude;
    public String locUrl;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String time;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.eventDate);
        dest.writeString(this.eventName);
        dest.writeString(this.eventLocation);
        dest.writeString(this.imageUrl);
        dest.writeString(this.shortDescribtion);
        dest.writeString(this.latitude);
        dest.writeString(this.locUrl);
        dest.writeString(this.longititude);
    }

    public SamuhaEvent() {
    }

    protected SamuhaEvent(Parcel in) {
        this.id = in.readString();
        this.eventDate = in.readString();
        this.eventName = in.readString();
        this.eventLocation = in.readString();
        this.imageUrl = in.readString();
        this.shortDescribtion = in.readString();
        this.latitude = in.readString();
        this.locUrl = in.readString();
        this.longititude = in.readString();
    }

    public static final Parcelable.Creator<SamuhaEvent> CREATOR = new Parcelable.Creator<SamuhaEvent>() {
        @Override
        public SamuhaEvent createFromParcel(Parcel source) {
            return new SamuhaEvent(source);
        }

        @Override
        public SamuhaEvent[] newArray(int size) {
            return new SamuhaEvent[size];
        }
    };
}
