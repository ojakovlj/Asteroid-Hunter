package com.sincress.entitymap.Entities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.sincress.entitymap.Abstract.Entity;

public class DateEventEntity implements Entity {

    private String date;
    private String description;
    private int cardColor;
    private int X, Y;
    private final int BOX_HEIGHT = 40;

    private boolean isSelected = false;

    public DateEventEntity(String date, String description, int color, Point pos){
        this.date = date;
        this.description = description;
        cardColor = color;
        X = pos.x;
        Y = pos.y;
    }

    @Override
    public void drawEntity(Canvas canvas) {
        Paint paint = new Paint();
        int boxWidth = calculateBoxSize();

        paint.setColor(Color.BLACK);
        // If the user has selected the entity it is rendered semitransparent
        paint.setAlpha(isSelected ? 127 : 255);
        canvas.drawRect(X, Y, X + boxWidth, Y + BOX_HEIGHT, paint);

        paint.setColor(cardColor);
        // If the user has selected the entity it is rendered semitransparent
        paint.setAlpha(isSelected ? 127 : 255);
        canvas.drawRect(X+5, Y+5, X + boxWidth - 5, Y + BOX_HEIGHT-5, paint);

        paint.setColor(Color.BLACK);
        // If the user has selected the entity it is rendered semitransparent
        paint.setAlpha(isSelected ? 127 : 255);
        canvas.drawText(date, X + 7, Y + BOX_HEIGHT-10, paint);
        canvas.drawText(description, X+date.length()*7+20, Y+BOX_HEIGHT-15, paint);
        paint.setStrokeWidth(3.0f);
        // If the user has selected the entity it is rendered semitransparent
        paint.setAlpha(isSelected ? 127 : 255);
        canvas.drawLine(X+date.length()*7+10, Y, X+date.length()*7+10, Y+BOX_HEIGHT, paint);
    }

    @Override
    public Point getEntityDimens() {
        return new Point(calculateBoxSize(), BOX_HEIGHT);
    }

    /**
     * Calculated entity card size in pixels, according to given date and description lengths.
     * @return box size in pixels
     */
    private int calculateBoxSize(){
       return date.length()*10 + description.length()*7 + 20;
    }

    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setPosition(Point newposition) {
        this.X = newposition.x;
        this.Y = newposition.y;
    }

    @Override
    public Point getPosition() {
        return new Point(X,Y);
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
