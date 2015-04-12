package com.sincress.entitymap.Entities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.sincress.entitymap.Abstract.Entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class ImgTextEntity implements Entity, Serializable {

    public String boxTitle, imageFile, textContent;
    public int X, Y;
    private final int BOX_HEIGHT = 40;


    public ImgTextEntity(String title, String img, String text, Point pos){
        boxTitle = title;
        imageFile = img;
        textContent = text;
        X = pos.x;
        Y = pos.y;
    }

    @Override
    public void drawEntity(Canvas canvas) {
        Paint paint = new Paint();
        int boxWidth = calculateBoxSize();

        paint.setColor(Color.BLACK);
        // Black outer box
        canvas.drawRect(X, Y, X + boxWidth, Y + BOX_HEIGHT, paint);

        paint.setColor(Color.GRAY);
        // Gray inner box
        canvas.drawRect(X+5, Y+5, X + boxWidth - 5, Y + BOX_HEIGHT-5, paint);

        // Text
        paint.setColor(Color.BLACK);
        canvas.drawText(boxTitle, X + 7, Y + BOX_HEIGHT - 15, paint);
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
       return boxTitle.length()*10 + 20;
    }

    public boolean isSelected() {
        return false;
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
    public EntityType getType() {
        return EntityType.ImgText;
    }

    @Override
    public void setSelected(boolean isSelected) {

    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(this.boxTitle);
        out.writeObject(this.imageFile);
        out.writeObject(this.textContent);
        out.writeInt(this.X);
        out.writeInt(this.Y);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        boxTitle = (String) in.readObject();
        imageFile = (String) in.readObject();
        textContent = (String) in.readObject();
        X = in.readInt();
        Y = in.readInt();
    }
}
