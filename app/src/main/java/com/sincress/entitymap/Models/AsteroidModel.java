package com.sincress.entitymap.Models;

import android.graphics.Point;

import com.sincress.entitymap.Entities.Asteroid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AsteroidModel {
    public ArrayList<InfoBoxModel> infoboxes = new ArrayList<>();

    public AsteroidModel(JSONObject json, Asteroid parent) {
        try {
            JSONArray cards = json.getJSONArray("cards");
            for (int i = 0; i < cards.length(); i++) {
                JSONObject card = cards.getJSONObject(i);
                JSONObject coordinates = card.getJSONObject("coordinates");
                InfoBoxModel infobox = new InfoBoxModel(
                        card.getString("name"),
                        card.getString("image"),
                        card.getString("text"),
                        new Point(coordinates.getInt("x"), coordinates.getInt("y")),
                        parent
                );
                infoboxes.add(infobox);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
