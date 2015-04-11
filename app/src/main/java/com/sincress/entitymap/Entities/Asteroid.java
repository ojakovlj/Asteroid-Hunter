package com.sincress.entitymap.Entities;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.sincress.entitymap.Abstract.Entity;

public class Asteroid implements Entity{
    private String id;
    private Point position;
    private boolean isSelected;

    public Asteroid(String id, Point position) {
        this.id = id;
        this.position = position;
    }

    private Bitmap getBitmap() {
        return null;
    }

    @Override
    public void drawEntity(Canvas canvas) {
        Paint paint = new Paint();

        canvas.drawBitmap(getBitmap(), (float)position.x, (float)position.y, paint);
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
