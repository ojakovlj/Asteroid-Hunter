package com.sincress.entitymap;


import android.graphics.Canvas;
import android.graphics.Point;

public interface Entity {
    // Draw entity to a canvas (map)
    public void drawEntity(Canvas canvas);
    // Get entity width and height
    public Point getEntityDimens();
    // Getters and setters
    public void setSelected(boolean isSelected);
    public boolean isSelected();

    public void setPosition(Point newposition);
    public Point getPosition();
}
