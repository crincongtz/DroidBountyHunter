package edu.training.droidbountyhunter.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Fugitive implements Parcelable {
    private int id;
    private String name;
    private String status;

    public Fugitive (int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    protected Fugitive(Parcel in) {
        id = in.readInt();
        name = in.readString();
        status = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(status);
    }
}
