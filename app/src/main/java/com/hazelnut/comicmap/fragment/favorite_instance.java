package com.hazelnut.comicmap.fragment;

public class favorite_instance {
    private int isPixivUrl, isTwitterUrl, isNicoUrl, wid;
    private String name, author, genre, day, circle;
    private String pixivUrl, TwitterUrl, NicoUrl, memo;

    public favorite_instance(int wid, String name, String author, String genre, String day, String circle,
                           int isPixivUrl, int isTwitterUrl, int isNicoUrl,
                           String pixivUrl, String TwitterUrl, String NicoUrl, String memo) {
        this.wid = wid;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.day = day;
        this.circle = circle;
        this.isPixivUrl = isPixivUrl;
        this.isTwitterUrl = isTwitterUrl;
        this.isNicoUrl = isNicoUrl;
        this.pixivUrl = pixivUrl;
        this.TwitterUrl = TwitterUrl;
        this.NicoUrl = NicoUrl;
        this.memo = memo;
    }

    public String getWid() { return String.valueOf(wid); }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getgenre() {
        return genre;
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

    public String getMemo() { return memo; }
}
