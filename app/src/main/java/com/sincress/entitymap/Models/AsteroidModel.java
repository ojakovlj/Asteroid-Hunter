package com.sincress.entitymap.Models;

import android.graphics.Point;

import org.json.JSONException;
import org.json.JSONObject;

public class AsteroidModel {
    public String title;
    public String imgPath;
    public String description;
    public Point position;

    public AsteroidModel(JSONObject json) {
        try {
            this.title = json.getString("title");
            this.imgPath = json.getString("imgPath");
            this.description = json.getString("description");

            JSONObject position = json.getJSONObject("position");
            this.position = new Point(position.getInt("x"), position.getInt("y"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
