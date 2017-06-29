package com.stgobain.samuha.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by vignesh on 15-06-2017.
 */

public class Team implements Parcelable {
    public String id;
    public String shortDescription;
    public String imageUrl;
    public String teamName;
    public String captainName;
    public String viceCaptainName;
    public String score;
    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }


    public ArrayList<String> department;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public String getViceCaptainName() {
        return viceCaptainName;
    }

    public void setViceCaptainName(String viceCaptainName) {
        this.viceCaptainName = viceCaptainName;
    }

    public ArrayList<String> getDepartment() {
        return department;
    }

    public void setDepartment(ArrayList<String> department) {
        this.department = department;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.shortDescription);
        dest.writeString(this.imageUrl);
        dest.writeString(this.teamName);
        dest.writeString(this.captainName);
        dest.writeString(this.viceCaptainName);
        dest.writeString(this.score);
        dest.writeStringList(this.department);
    }

    public Team() {
    }

    protected Team(Parcel in) {
        this.id = in.readString();
        this.shortDescription = in.readString();
        this.imageUrl = in.readString();
        this.teamName = in.readString();
        this.captainName = in.readString();
        this.viceCaptainName = in.readString();
        this.score = in.readString();
        this.department = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel source) {
            return new Team(source);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };
}
