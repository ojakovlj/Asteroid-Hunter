package com.sincress.entitymap.Entities;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.sincress.entitymap.Abstract.Entity;

public class Asteroid implements Entity{
    private int id;
    private Point position;
    private boolean isSelected;
    private Bitmap bitmap;

    public Asteroid(int id, Point position) {
        this.id = id;
        this.position = position;
    }

    private Bitmap getBitmap() {
        if (bitmap == null)
            bitmap = BitmapFactory.decodeFile("drawable/asteroid" + id);
        return bitmap;
    }

    @Override
    public void drawEntity(Canvas canvas) {
        Paint paint = new Paint();

        canvas.drawBitmap(getBitmap(), (float)position.x, (float)position.y, paint);
    }

    @Override
    public Point getEntityDimens() {
        switch (id) {
            case 1:
                return new Point(220, 223);
            default:
                return new Point(100, 100);
        }
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

    @Override
    public EntityType getType() {
        return EntityType.Asteroid;
    }

    public int GetId()
    {
        return id;
    }
}
