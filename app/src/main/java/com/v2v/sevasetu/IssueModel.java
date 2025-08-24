package com.v2v.sevasetu;

public class IssueModel {
    private String id;
    private String category;
    private String description;
    private String status;
    private long timestamp;

    public IssueModel() {} // Required for Firebase

    public IssueModel(String category, String description, String status, long timestamp) {
        this.category = category;
        this.description = description;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public long getTimestamp() { return timestamp; }
}

