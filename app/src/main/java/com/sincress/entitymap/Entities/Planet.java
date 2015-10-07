package com.sincress.entitymap.Entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.sincress.entitymap.Abstract.Entity;
import com.sincress.entitymap.EntityManager;
import com.sincress.entitymap.Models.InfoBoxModel;
import com.sincress.entitymap.Models.JSONReader;
import com.sincress.entitymap.Models.PlanetModel;
import com.sincress.entitymap.R;

import java.io.Serializable;
import java.util.ArrayList;

public class Planet implements Entity, Serializable {
    private Point position;
    private boolean isSelected;
    public String imageFile, textContent;
    private Bitmap bitmap;
    private PlanetModel model;

    public Planet(String img, String text, Point pos){
        this.imageFile = img;
        int resourceId = R.raw.zemlja;
        this.textContent = text;
        this.position = pos;
        this.bitmap = BitmapFactory.decodeResource(EntityManager.entityCanvas.getResources(), R.drawable.earth);
        this.model = new PlanetModel(JSONReader.getJSONObject(resourceId), this);
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
    public Point getPosition() {
        return this.position;
    }

    @Override
    public EntityType getType() {
        return EntityType.Planet;
    }

    public ArrayList<ImgTextEntity> getInfoboxes() {
        ArrayList<ImgTextEntity> imgTextEntities = new ArrayList<>();
        for (InfoBoxModel infobox : model.infoboxes)
        {
            imgTextEntities.add(new ImgTextEntity(infobox));
        }
        return imgTextEntities;
    }
}