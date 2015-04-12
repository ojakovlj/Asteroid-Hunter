package com.sincress.entitymap.Entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.sincress.entitymap.Abstract.Entity;
import com.sincress.entitymap.EntityManager;
import com.sincress.entitymap.R;

import java.io.Serializable;

public class Planet implements Entity, Serializable {

    private Point position;
    private boolean isSelected;
    public String imageFile, textContent;
    private Bitmap bitmap;

    public Planet(String img, String text, Point pos){
        imageFile = img;
        textContent = text;
        position = pos;
        bitmap = BitmapFactory.decodeResource(EntityManager.entityCanvas.getResources(), R.drawable.earth);
    }

    @Override
    public void drawEntity(Canvas canvas) {
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, position.x, position.y, paint);
    }

    @Override
    public Point getEntityDimens() {
        return new Point(bitmap.getWidth(), bitmap.getHeight());
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
        return EntityType.Planet;
    }
}
