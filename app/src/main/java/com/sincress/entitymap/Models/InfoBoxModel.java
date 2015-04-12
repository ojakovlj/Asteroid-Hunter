package com.sincress.entitymap.Models;

import android.graphics.Point;

public class InfoBoxModel {
    public String name;
    public String image;
    public String text;
    public Point position;

    public InfoBoxModel(String name, String image, String text, Point position) {
        this.name = name;
        this.image = image;
        this.text = text;
        this.position = position;
    }
}
