package com.v2v.sevasetu;

public class TimetableModel {
    private String id;
    private String title;
    private String description;
    private long date;

    public TimetableModel() {}

    public TimetableModel(String title, String description, long date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public long getDate() { return date; }
}
