package com.example.comicmap;

import java.util.ArrayList;

public class sample_circle {
    ArrayList<circle> items = new ArrayList<>();

    public ArrayList<circle> getItems() {

        String url_a = "https://webcatalogj07.blob.core.windows.net/c96imgthm/231d6601-3a49-4854-947b-272177b772f9.png?sv=2016-05-31&sr=c&sig=c7dDgCmDizPdkm7vnTl1L3N7qV3QEuo2nZCi%2B%2FTZkB4%3D&se=2019-09-09T13%3A30%3A59Z&sp=r";
        String url_b = "https://webcatalogj07.blob.core.windows.net/c96imgthm/147d5634-cb37-4923-a682-701a9b6ab466.png?sv=2016-05-31&sr=c&sig=c7dDgCmDizPdkm7vnTl1L3N7qV3QEuo2nZCi%2B%2FTZkB4%3D&se=2019-09-09T13%3A30%3A59Z&sp=r";
        String url_c = "https://webcatalogj07.blob.core.windows.net/c96imgthm/147d5634-cb37-4923-a682-701a9b6ab466.png?sv=2016-05-31&sr=c&sig=c7dDgCmDizPdkm7vnTl1L3N7qV3QEuo2nZCi%2B%2FTZkB4%3D&se=2019-09-09T13%3A30%3A59Z&sp=r";

        circle circle1 = new circle(url_a, "柏処－かしわどころ－", "カシワ", "S12", "1", "A12b");
        circle circle2 = new circle(url_b, "ぎゃろっぷだいな", "成田るみ", "S12", "1", "A12a");
        circle circle3 = new circle(url_c, "だーくさいどるーむ", "だーく", "S12", "1", "A12");

        items.add(circle1);
        items.add(circle2);
        items.add(circle3);

        return items;
    }
}
