package com.sincress.entitymap.Models;

import android.content.res.Resources;

import com.sincress.entitymap.EntityManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class JSONReader {
    public static JSONObject getJSONObject(int resource)
    {
        Resources res = EntityManager.entityCanvas.getResources();
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
}
