package com.example.comicmap;

public class circle {
    private String url, name, author, hall, day, circle;

    public circle(String url, String name, String author, String hall, String day, String circle) {
        this.url = url;
        this.name = name;
        this.author = author;
        this.hall = hall;
        this.day = day;
        this.circle = circle;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getHall() {
        return hall;
    }

    public String getDay() {
        return day;
    }

    public String getCircle() { return circle; }
}
