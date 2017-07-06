package com.stgobain.samuha.Model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vignesh on 25-06-2017.
 */

public class ContextData implements Parcelable {

    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;
    private String code;
    private Drawable flag;

    public ContextData() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public Drawable getFlag() {
        return flag;
    }
    public String getCode() {
        return code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    protected ContextData(Parcel in) {
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ContextData> CREATOR = new Parcelable.Creator<ContextData>() {
        @Override
        public ContextData createFromParcel(Parcel source) {
            return new ContextData(source);
        }

        @Override
        public ContextData[] newArray(int size) {
            return new ContextData[size];
        }
    };
}
