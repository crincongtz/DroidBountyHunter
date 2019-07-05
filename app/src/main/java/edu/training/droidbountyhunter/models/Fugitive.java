package edu.training.droidbountyhunter.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Fugitive implements Parcelable {
    private int id;
    private String name;
    private String status;
    private String photo;
    private double latitude;
    private double longitude;

    public Fugitive (int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.photo = "";
        this.latitude = 0d;
        this.longitude = 0d;
    }

    public Fugitive(int id, String name, String status, String photo) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.photo = photo;
        this.latitude = 0d;
        this.longitude = 0d;
    }

    public Fugitive(int id, String name, String status, String photo, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.photo = photo;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Fugitive(Parcel in) {
        id = in.readInt();
        name = in.readString();
        status = in.readString();
        photo = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Fugitive> CREATOR = new Creator<Fugitive>() {
        @Override
        public Fugitive createFromParcel(Parcel in) {
            return new Fugitive(in);
        }

        @Override
        public Fugitive[] newArray(int size) {
            return new Fugitive[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(status);
        dest.writeString(photo);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}

