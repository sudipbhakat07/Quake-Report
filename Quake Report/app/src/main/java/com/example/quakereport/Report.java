package com.example.quakereport;

public class Report {

    Double magnitude;
    String place;
    Long date;
    String url;

    Report(Double magnitude, String place, Long date,String url) {
        this.magnitude = magnitude;
        this.place = place;
        this.date = date;
        this.url = url;

    }

    public Double getMagnitude() {
        return magnitude;
    }

    public Long getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public String getUrl() {
        return url;
    }
}
