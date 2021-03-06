package com.sincress.entitymap.Abstract;


import android.graphics.Canvas;
import android.graphics.Point;

public interface Entity {
    public enum EntityType {
        ImgText,
        Asteroid,
        Planet
    }

    // Draw entity to a canvas (map)
    public void drawEntity(Canvas canvas);
    // Get entity width and height
    public Point getEntityDimens();

    public Point getPosition();

    public EntityType getType();
}
