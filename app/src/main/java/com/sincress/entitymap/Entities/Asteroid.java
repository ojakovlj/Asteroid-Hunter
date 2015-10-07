package com.sincress.entitymap.Entities;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.sincress.entitymap.Abstract.Entity;
import com.sincress.entitymap.EntityManager;
import com.sincress.entitymap.Models.AsteroidModel;
import com.sincress.entitymap.Models.InfoBoxModel;
import com.sincress.entitymap.Models.JSONReader;
import com.sincress.entitymap.R;

import java.io.Serializable;
import java.util.ArrayList;

public class Asteroid implements Entity, Serializable {
    private int rawId;
    private Point position;
    private boolean isSelected;
    private Bitmap bitmap;
    private AsteroidModel model;

    public Asteroid(int id, Point position) {
        int id1 = id;
        this.position = position;
        int drawableId;
        switch (id1) {
            case 1:
            default:
                drawableId = R.drawable.asteroid1;
                rawId = R.raw.asteroid1;
                break;
            case 2:
                drawableId = R.drawable.asteroid2;
                rawId = R.raw.asteroid2;
                break;
            case 3:
                drawableId = R.drawable.asteroid3;
                rawId = R.raw.asteroid3;
                break;
            case 4:
                drawableId = R.drawable.asteroid4;
                break;
        }
        this.bitmap = BitmapFactory.decodeResource(EntityManager.entityCanvas.getResources(), drawableId);
        this.model = new AsteroidModel(JSONReader.getJSONObject(rawId), this);
    }

    @Override
    public void drawEntity(Canvas canvas) {
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, (float)position.x, (float)position.y, paint);
    }

    @Override
    public Point getEntityDimens() {
        return new Point(bitmap.getWidth(), bitmap.getHeight());
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public EntityType getType() {
        return EntityType.Asteroid;
    }

    public ArrayList<ImgTextEntity> getInfoboxes() {
        ArrayList<ImgTextEntity> entities = new ArrayList<>();
        for (InfoBoxModel infobox : model.infoboxes)
        {
            entities.add(new ImgTextEntity(infobox));
        }
        return entities;
    }

}