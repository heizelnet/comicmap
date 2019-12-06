package com.example.comicmap.fragment;

public class circle_instance {
    private int isPixivUrl, isTwitterUrl, isNicoUrl;
    private String url, name, author, hall, day, circle;
    private String pixivUrl, TwitterUrl, NicoUrl;

    public circle_instance(String url, String name, String author, String hall, String day, String circle,
                           int isPixivUrl, int isTwitterUrl, int isNicoUrl,
                           String pixivUrl, String TwitterUrl, String NicoUrl) {
        this.url = url;
        this.name = name;
        this.author = author;
        this.hall = hall;
        this.day = day;
        this.circle = circle;
        this.isPixivUrl = isPixivUrl;
        this.isTwitterUrl = isTwitterUrl;
        this.isNicoUrl = isNicoUrl;
        this.pixivUrl = pixivUrl;
        this.TwitterUrl = TwitterUrl;
        this.NicoUrl = NicoUrl;
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

    public boolean isPixivUrl() { return isPixivUrl != 0; }

    public boolean isTwitterUrl() { return isTwitterUrl != 0; }

    public boolean isNicoUrl() { return isNicoUrl != 0; }

    public String getPixivUrl() { return pixivUrl; }

    public String getTwitterUrl() { return TwitterUrl; }

    public String getNicoUrl() { return NicoUrl; }
}
