package com.sincress.entitymap.Entities;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.sincress.entitymap.Abstract.Entity;
import com.sincress.entitymap.EntityManager;
import com.sincress.entitymap.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class Asteroid implements Entity, Serializable {
    private int id;
    private Point position;
    private boolean isSelected;
    private Bitmap bitmap;
    private JSONObject json;

    public Asteroid(int id, Point position) {
        this.id = id;
        this.position = position;

        int resource;
        switch (this.id) {
            case 1:
            default:
                resource = R.drawable.asteroid1;
                break;
            case 2:
                resource = R.drawable.asteroid2;
                break;
            case 3:
                resource = R.drawable.asteroid3;
                break;
            case 4:
                resource = R.drawable.asteroid4;
                break;
        }
        this.bitmap = BitmapFactory.decodeResource(EntityManager.entityCanvas.getResources(), resource);
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

    public ImgTextEntity getImgTextEntity() {
        if (json == null) json = readJson();
        try {
            String title = json.getString("title");

            String imgPath = json.getString("imgPath");
            String description = json.getString("description");

            JSONObject positionJson = json.getJSONObject("position");
            int x = positionJson.getInt("x");
            int y = positionJson.getInt("y");
            return new ImgTextEntity("title", "img", "text", new Point(x, y));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject readJson() {
        Resources res = EntityManager.entityCanvas.getResources();

        int resource;
        switch (id) {
            case 1:
            default:
                resource = R.raw.asteroid1;
                break;
//            case 2:
//                resource = R.raw.asteroid2;
//                break;
//            case 3:
//                resource = R.raw.asteroid3;
//                break;
//            case 4:
//                resource = R.raw.asteroid4;
//                break;
        }

        InputStream is = res.openRawResource(resource);

        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        String result = "";

        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            result = writer.toString();
            is.close();
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(this.id);
        out.writeInt(this.position.x);
        out.writeInt(this.position.y);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.id = in.readInt();
        this.position = new Point(in.readInt(), in.readInt());
        this.json = readJson();
        int resource;
        switch (this.id) {
            case 1:
            default:
                resource = R.drawable.asteroid1;
                break;
            case 2:
                resource = R.drawable.asteroid2;
                break;
            case 3:
                resource = R.drawable.asteroid3;
                break;
            case 4:
                resource = R.drawable.asteroid4;
                break;
        }
        this.bitmap = BitmapFactory.decodeResource(EntityManager.entityCanvas.getResources(), resource);
    }
}