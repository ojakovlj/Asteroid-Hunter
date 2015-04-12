package com.sincress.entitymap;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sincress.entitymap.Abstract.Entity;
import com.sincress.entitymap.Entities.Asteroid;
import com.sincress.entitymap.Entities.ImgTextEntity;
import com.sincress.entitymap.Entities.Planet;
import com.sincress.entitymap.EntityCanvas.Connector;

import java.util.ArrayList;

public class EntityManager {

    private static float clickX, clickY;
    public static EntityCanvas entityCanvas;
    private static Entity clickedEntity;

    public static void saveEntities(ArrayList<Entity> array, String filename){
        // TODO : Add save function to file with custom format
    }

    public static ArrayList<Entity> loadEntities(String filename){
        ArrayList<Entity> array = null;
        // TODO : Add load function from file with custom format
        return array;
    }

    public static void setCanvasInstance(EntityCanvas instance){
        EntityManager.entityCanvas = instance;
    }

    /**
     * Iterates through the given array of entities and returns true if the given
     * coordinate is inside one of the entity rectangles.
     * @param array Array of available entities
     * @param coordinate Coordinate to test
     * @return True if inside any entity, false otherwise
     */
    private static Entity isInEntity(ArrayList<Entity> array, Point coordinate){
        Point position, dimension;

        for(Entity entity: array){
            position = entity.getPosition();
            dimension = entity.getEntityDimens();
            if(coordinate.x > position.x && coordinate.x < position.x+dimension.x)
                if(coordinate.y > position.y && coordinate.y < position.y+dimension.y)
                    return entity;
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
                Bitmap bmp = BitmapFactory.decodeFile(((ImgTextEntity) clickedEntity).imageFile);
                image.setImageBitmap(bmp);
            }
            textView.setText(((ImgTextEntity) clickedEntity).textContent);
        }
        else {
            if(!((Planet)clickedEntity).imageFile.equals("")) {
                Bitmap bmp = BitmapFactory.decodeFile(((Planet) clickedEntity).imageFile);
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
                clickX = event.getX();
                clickY = event.getY();
            }
            // ======================== ACTION UP ====================================
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Check if entity is clicked
                clickedEntity = isInEntity(entities,
                        new Point((int) (event.getX()/entityCanvas.ZOOM_LEVEL + cameraX), (int) (event.getY()/entityCanvas.ZOOM_LEVEL + cameraY)));

                if(clickedEntity == null)
                    return true;
                // Type = ImgTextEntity
                if(clickedEntity.getType() == Entity.EntityType.ImgText){
                    // Toggle the fragment and display the relevant data
                    entityCanvas.getActivity().toggleFragment(v);
                }
                // Type = Asteroid
                else if (clickedEntity.getType() == Entity.EntityType.Asteroid){
                    entities.add( ((Asteroid) clickedEntity).getImgTextEntity());
                }
                // Type = Earth
                else{
                    // Toggle the fragment and display the relevant data
                    entityCanvas.getActivity().toggleFragment(v);
                }
            }
            // ======================== ACTION MOVE ====================================
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                // Move the canvas
               /* clampCameraPosition(cameraX, cameraY, event, v);
                clickX = event.getX();
                clickY = event.getY();*/
                clampCameraPosition(cameraX, cameraY, event, v, entityCanvas.getScale());
                clickX = event.getX();
                clickY = event.getY();
            }
            return true;
        }
    };

    private static void clampCameraPosition(float cameraX, float cameraY, MotionEvent event, View v, float zoomLevel) {
        int bottomBound = entityCanvas.H_AREA_SIZE  * entityCanvas.IMAGE_SIZE_Y - (int)(v.getHeight()/zoomLevel);
        int topBound = -entityCanvas.H_AREA_SIZE * entityCanvas.IMAGE_SIZE_Y;
        int leftBound = -entityCanvas.H_AREA_SIZE * entityCanvas.IMAGE_SIZE_X;
        int rightBound = entityCanvas.H_AREA_SIZE * entityCanvas.IMAGE_SIZE_X - (int)(v.getWidth()/zoomLevel);
        double newCameraX = cameraX + (clickX - event.getX());
        double newCameraY = cameraY + (clickY - event.getY());
        if(!(newCameraX > rightBound || newCameraX < leftBound))
            cameraX += (clickX - event.getX());
        if(!(newCameraY > bottomBound || newCameraY < topBound))
            cameraY += (clickY - event.getY());
        entityCanvas.setCameraCoords(cameraX, cameraY);
        entityCanvas.postInvalidate();
    }

    public static void initialise() {
        Asteroid ast1 = new Asteroid(1, new Point(200, 100));
        entityCanvas.addEntity(ast1);
        Planet earth = new Planet("", "Welcome to planet Earth!",
                new Point(2091-entityCanvas.IMAGE_SIZE_X*entityCanvas.H_AREA_SIZE,
                        1465-entityCanvas.IMAGE_SIZE_Y*entityCanvas.H_AREA_SIZE));
        entityCanvas.addEntity(earth);
    }
}
