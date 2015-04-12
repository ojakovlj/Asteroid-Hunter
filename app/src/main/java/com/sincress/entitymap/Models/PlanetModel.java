package com.sincress.entitymap.Models;

import android.graphics.Point;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlanetModel {
    public ArrayList<InfoBoxModel> infoboxes = new ArrayList<>();

    public PlanetModel(JSONObject json) {
        try {
            JSONArray cards = json.getJSONArray("cards");
            for(int i = 0; i<cards.length(); i++){
                JSONObject card = cards.getJSONObject(i);
                JSONObject coordinates = card.getJSONObject("coordinates");
                InfoBoxModel infobox = new InfoBoxModel(
                        card.getString("name"),
                        card.getString("image"),
                        card.getString("text"),
                        new Point(coordinates.getInt("x"), coordinates.getInt("y"))
                );
                infoboxes.add(infobox);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}