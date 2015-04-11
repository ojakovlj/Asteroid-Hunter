package com.sincress.entitymap.Entities;

import android.graphics.Canvas;
import android.graphics.Point;

import com.sincress.entitymap.Abstract.Entity;

public class Planet implements Entity {

    private Point position;
    private boolean isSelected;

    @Override
    public void drawEntity(Canvas canvas) {

    }

    @Override
    public Point getEntityDimens() {
        return null;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public boolean isSelected() {
        return this.isSelected;
    }

    @Override
    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public Point getPosition() {
        return this.position;
    }
}
