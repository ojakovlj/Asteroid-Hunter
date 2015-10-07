package com.sincress.entitymap;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sincress.entitymap.Abstract.Entity;
import com.sincress.entitymap.Entities.Asteroid;
import com.sincress.entitymap.Entities.ImgTextEntity;
import com.sincress.entitymap.Entities.Planet;
import com.sincress.entitymap.EntityCanvas.Connector;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EntityManager {

    private static float clickX, clickY, oldClickX, oldClickY;
    public static EntityCanvas entityCanvas;
    private static Entity clickedEntity;

    public static void saveEntities(ArrayList<Entity> array, String filename){
        Context ctx = entityCanvas.getActivity().getApplicationContext();

        try {
            FileOutputStream fos = ctx.openFileOutput(filename, Context.MODE_WORLD_READABLE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(array);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setCanvasInstance(EntityCanvas instance){
        EntityManager.entityCanvas = instance;
    }

    /**
     * Iterates through the given array of entities and returns true if the given
     * coordinate is inside one of the entity rectangles.
     * @param array Array of available entities
     *
     * @param coordinate Coordinate to test
     * @return True if inside any entity, false otherwise
     */
    private static Entity isInEntity(ArrayList<Entity> array, Point coordinate){
        Point position, dimension;
        for (int i = array.size()-1; i>= 0; i--)
        {
            position = array.get(i).getPosition();
            dimension = array.get(i).getEntityDimens();
            if (coordinate.x > position.x && coordinate.x < position.x + dimension.x)
                if (coordinate.y > position.y && coordinate.y < position.y + dimension.y)
                    return array.get(i);
        }
        return null;
    }

    /**
     * Callback from fragment's OK button, handle the implementation here
     * @param fragmentview fragment's base view containing its elements.
     */
    public static void loadFragmentData(View fragmentview){
        //When Close is clicked on the fragment, canvas is brought to front and we obtain data
        ImageView image = (ImageView) fragmentview.findViewById(R.id.image);
        TextView textView = (TextView) fragmentview.findViewById(R.id.descText);



        if(clickedEntity.getType() == Entity.EntityType.ImgText) {
            if(!((ImgTextEntity)clickedEntity).imageFile.equals("")) {
                Bitmap bmp = null;
                try {
                    Context ctx = entityCanvas.getActivity().getApplicationContext();
                    InputStream is = ctx.getAssets().open(((ImgTextEntity) clickedEntity).imageFile);
                    bmp = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image.setImageBitmap(bmp);
            }
            textView.setText(((ImgTextEntity) clickedEntity).textContent);
        }
        else {
            if(!((Planet)clickedEntity).imageFile.equals("")) {
                Bitmap bmp = null;
                try {
                    Context ctx = entityCanvas.getActivity().getApplicationContext();
                    InputStream is = ctx.getAssets().open(((Planet) clickedEntity).imageFile);
                    bmp = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image.setImageBitmap(bmp);
            }
            textView.setText(((Planet) clickedEntity).textContent);
        }
    }

    public static View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        /**
         * Handle entity selection and movement, view movement, scaling,
         * adding new entities, connecting entities and etc.
         * @param v
         * @param event
         * @return
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float cameraX = entityCanvas.getCameraCoords().x;
            float cameraY = entityCanvas.getCameraCoords().y;

            ArrayList<Entity> entities = entityCanvas.getEntities();
            ArrayList<Connector> connectors = entityCanvas.getConnectors();
            // ======================== ACTION DOWN ====================================
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Mark position for use in ACTION MOVE
                oldClickX = clickX = event.getX();
                oldClickY = clickY = event.getY();
            }
            // ======================== ACTION UP ====================================
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Set flag for ship stopping sequence
                entityCanvas.actionDone = true;
                // Check if entity is clicked
                clickedEntity = isInEntity(entities,
                        new Point((int) (event.getX()/entityCanvas.ZOOM_LEVEL + cameraX), (int) (event.getY()/entityCanvas.ZOOM_LEVEL + cameraY)));

                // If drag start and end point are not same, exit
                if(clickedEntity == null || clickedEntity != isInEntity(entities,
                        new Point((int) (oldClickX/entityCanvas.ZOOM_LEVEL + cameraX), (int) (oldClickY/entityCanvas.ZOOM_LEVEL + cameraY))))
                    return true;
                // Type = ImgTextEntity
                if(clickedEntity.getType() == Entity.EntityType.ImgText){
                    // Toggle the fragment and display the relevant data
                    entityCanvas.getActivity().toggleFragment(v);
                }
                // Type = Asteroid
                else if (clickedEntity.getType() == Entity.EntityType.Asteroid){
                    entities.addAll( ((Asteroid) clickedEntity).getInfoboxes());
                    entityCanvas.postInvalidate();
                }
                // Type = Earth
                else{
                    // Toggle the fragment and display the relevant data
                    //entityCanvas.getActivity().toggleFragment(v);
                    entities.addAll(((Planet) clickedEntity).getInfoboxes());
                }
                EntityManager.saveEntities(entityCanvas.getEntities(), "entities");
            }
            // ======================== ACTION MOVE ====================================
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                // Move the canvas
                clickX = event.getX();
                clickY = event.getY();
                entityCanvas.actionDone = false;
                entityCanvas.shipSpeed = calculateHeadingAndSpeed();
            }
            return true;
        }
    };

    private static double calculateHeadingAndSpeed() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        entityCanvas.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        entityCanvas.shipHeading = Math.atan2((clickY - height/2),(clickX - width/2));
        double radius = Math.sqrt((clickX-height/2)*(clickX-height/2) + (clickX - width/2)*(clickX - width/2));
        // Calculate speed for user character
        double speed;
        if(width < height){
            if(radius < width/2)
                speed = 20.0*(radius/width);
            else speed = 10;
        }
        else{
            if(radius < height/2)
                speed = 20.0*(radius/height);
            else speed = 10;
        }
        return speed;
    }


    public static void initialise() {
//        ArrayList<Entity> entities = loadEntities("entities");
//        if (entities.isEmpty()) {
//            entities = getDefaultEntities();
//        }
//        entityCanvas.setEntities(entities);
          entityCanvas.setEntities(getDefaultEntities());
    }

    public static ArrayList<Entity> getDefaultEntities() {
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(new Asteroid(1, new Point(200, 100)));
        entities.add(new Asteroid(2, new Point(-300, -300)));
        entities.add(new Asteroid(3, new Point(0, -500)));
        // Causes crash, TODO check JSON file
        //entities.add(new Asteroid(4, new Point(-500, 200)));

        Planet earth = new Planet("", "Welcome to planet Earth!",
                new Point(2091-entityCanvas.IMAGE_SIZE_X*entityCanvas.H_AREA_SIZE,
                        1465-entityCanvas.IMAGE_SIZE_Y*entityCanvas.H_AREA_SIZE));
        entities.add(earth);
        return entities;
    }
}
