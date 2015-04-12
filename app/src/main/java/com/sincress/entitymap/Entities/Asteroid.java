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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Asteroid implements Entity, Serializable {
    private int id;
    private int rawId;
    private int drawableId;
    private Point position;
    private boolean isSelected;
    private Bitmap bitmap;
    private AsteroidModel model;

    public Asteroid(int id, Point position) {
        this.id = id;
        this.position = position;
        switch (this.id) {
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
    public int getRawId() {
        return rawId;
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

    public ArrayList<ImgTextEntity> getInfoboxes() {
        ArrayList<ImgTextEntity> entities = new ArrayList<>();
        for (InfoBoxModel infobox : model.infoboxes)
        {
            entities.add(new ImgTextEntity(infobox));
        }
        return entities;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(this.id);
        out.writeInt(this.rawId);
        out.writeInt(this.position.x);
        out.writeInt(this.position.y);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.id = in.readInt();
        this.rawId = in.readInt();
        this.position = new Point(in.readInt(), in.readInt());
        this.model = new AsteroidModel(JSONReader.getJSONObject(this.rawId), this);

        this.bitmap = BitmapFactory.decodeResource(EntityManager.entityCanvas.getResources(), this.drawableId);
    }
}