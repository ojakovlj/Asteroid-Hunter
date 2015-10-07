package com.sincress.entitymap.Entities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.sincress.entitymap.Abstract.Entity;
import com.sincress.entitymap.EntityCanvas;
import com.sincress.entitymap.Models.InfoBoxModel;

import java.io.Serializable;

public class ImgTextEntity implements Entity, Serializable {

    private float alpha;
    public String boxTitle, imageFile, textContent;
    private final int BOX_HEIGHT = 40;
    private InfoBoxModel model;

    public ImgTextEntity(InfoBoxModel model) {
        this.model = model;
        boxTitle = model.name;
        imageFile = model.image;
        textContent = model.text;
        setAlpha(1f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        fadeIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                EntityCanvas.instance.postInvalidate();
            }
        });
        fadeIn.start();
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

    @Override
    public Point getPosition() {
        return new Point(model.position.x + model.offset.x, model.position.y + model.offset.y);
    }

    @Override
    public EntityType getType() {
        return EntityType.ImgText;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
