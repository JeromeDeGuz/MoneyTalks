package com.bugbytes.moneytalks.models;

public class Item {
    private int id;
    private String title;
    private String description;

    public Item(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
}
