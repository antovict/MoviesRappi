package com.example.moviesrappi.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
public class Videos implements Parcelable {
    int id;
    ArrayList<Video> results;

    public Videos(int id, ArrayList<Video> results) {
        this.id = id;
        this.results = results;
    }

    public Videos() {
        this.id = 0;
        this.results = new ArrayList<Video>();
    }

    protected Videos(Parcel in) {
        id = in.readInt();
        results = in.createTypedArrayList(Video.CREATOR);
    }

    public static final Creator<Videos> CREATOR = new Creator<Videos>() {
        @Override
        public Videos createFromParcel(Parcel in) {
            return new Videos(in);
        }

        @Override
        public Videos[] newArray(int size) {
            return new Videos[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setPage(int id) {
        this.id = id;
    }

    public ArrayList<Video> getResults() {
        return results;
    }

    public void setResults(ArrayList<Video> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeTypedList(results);
    }
}
