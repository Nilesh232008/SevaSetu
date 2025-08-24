package com.v2v.sevasetu;

public class ReminderModel {
    private String title;
    private long time;

    public ReminderModel(String title, long time) {
        this.title = title;
        this.time = time;
    }

    public String getTitle() { return title; }
    public long getTime() { return time; }
}

