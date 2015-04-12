package com.sincress.entitymap.Entities;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.sincress.entitymap.Abstract.Entity;
import com.sincress.entitymap.EntityCanvas;
import com.sincress.entitymap.Models.InfoBoxModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ImgTextEntity implements Entity, Serializable {

    private float alpha;
    public String boxTitle, imageFile, textContent;
    public int X, Y;
    private final int BOX_HEIGHT = 40;
    private ObjectAnimator fadeIn;
    private InfoBoxModel model;

    public ImgTextEntity(String title, String img, String text, Point pos, Entity parent) {
        this(new InfoBoxModel(title, img, text, pos, parent));
    }
    public ImgTextEntity(InfoBoxModel model) {
        this.model = model;
        boxTitle = model.name;
        imageFile = model.image;
        textContent = model.text;
        X = model.position.x;
        Y = model.position.y;
        setAlpha(1f);
        fadeIn = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        fadeIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                EntityCanvas.instance.postInvalidate();
            }
        });
        fadeIn.start();
    }

    @Override
    public int getRawId() {
        return 0;
    }

    @Override
    public void drawEntity(Canvas canvas) {
        Paint paint = new Paint();
        int boxWidth = calculateBoxSize();
        Point position = getPosition();

        paint.setColor(Color.BLACK);
        paint.setAlpha((int)(255 * getAlpha()));
        // Black outer box
        canvas.drawRect(position.x, position.y, position.x + boxWidth, position.y + BOX_HEIGHT, paint);

        paint.setColor(Color.GRAY);
        paint.setAlpha((int)(255 * getAlpha()));
        // Gray inner box
        canvas.drawRect(position.x+5, position.y+5, position.x + boxWidth - 5, position.y + BOX_HEIGHT-5, paint);

        // Text
        paint.setColor(Color.BLACK);
        paint.setAlpha((int)(255 * getAlpha()));
        canvas.drawText(boxTitle, position.x + 7, position.y + BOX_HEIGHT - 15, paint);
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
        return new Point(model.position.x + model.offset.x, model.position.y + model.offset.y);
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

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
