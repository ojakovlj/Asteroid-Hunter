package com.sincress.entitymap.Models;

import android.graphics.Point;

import com.sincress.entitymap.Abstract.Entity;

public class InfoBoxModel {
    public String name;
    public String image;
    public String text;
    public Point position;
    public Point offset;

    public InfoBoxModel(String name, String image, String text, Point offset, Entity parent) {
        this.name = name;
        this.image = image;
        this.text = text;
        this.position = new Point(
                parent.getPosition().x + parent.getEntityDimens().x / 2,
                parent.getPosition().y + parent.getEntityDimens().y / 2);
        this.offset = new Point(offset.x, offset.y);
    }
}
