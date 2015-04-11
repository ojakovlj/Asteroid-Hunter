package com.sincress.entitymap;


import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import com.sincress.entitymap.Abstract.Entity;
import com.sincress.entitymap.EntityCanvas.Connector;

import java.util.ArrayList;

public class EntityManager {

    private static float clickX, clickY;
    private static EntityCanvas entityCanvas;

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
    public static Entity isInEntity(ArrayList<Entity> array, Point coordinate){
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
     * @param entityCanvas canvas which is related to the activity on which we do our drawing
     */
    public static void doFragmentAction(View fragmentview, EntityCanvas entityCanvas){
       //When Close is clicked on the fragment, canvas is brought to front and no action is done
    }

    public static View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        long starttime;
        float oldCameraX, oldCameraY, oldClickX, oldClickY;
        boolean entityIsSelected = false;
        Entity dragPoint1;

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
                clickX = event.getX();
                clickY = event.getY();
                // Used to check if the long touch is static (for adding entities)
                oldClickX = clickX;     oldClickY = clickY;
                oldCameraX = cameraX;   oldCameraY = cameraY;

                // If we started a drag, mark it here
                dragPoint1 = EntityManager.isInEntity(entities,
                        new Point((int)(clickX+cameraX), (int)(clickY+cameraY)));

                //Start timer
                starttime = System.currentTimeMillis();
                return true;
            }
            // ======================== ACTION UP ====================================
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Entity clickedEntity = EntityManager.isInEntity(entities,
                        new Point((int) (event.getX() + cameraX), (int) (event.getY() + cameraY)));

                // Add new connector if the drag start entity isnt the same as drag end entity
                if( clickedEntity != null && dragPoint1 != null)
                    if (!clickedEntity.equals(dragPoint1)) {
                        connectors.add(new Connector(dragPoint1, clickedEntity));
                        entityCanvas.postInvalidate();
                        return true;
                    }

                // Toggle entity selection
                if(clickedEntity != null) {
                    entityIsSelected = false;
                    clickedEntity.setSelected(!clickedEntity.isSelected()); // Invert selection
                    for (Entity entity : entities)
                        if(entity.isSelected())
                            entityIsSelected = true;
                    entityCanvas.postInvalidate(); // Redraw
                }
                else {   // Reset all entities' "selected" value
                    for (Entity entity : entities)
                        entity.setSelected(false);
                    entityCanvas.postInvalidate();
                    entityIsSelected = false;
                }
                // Add a new entity if touch has been held for at least 500ms
                // and if the camera did not move and if no entity has been clicked
                if (oldCameraX == cameraX && oldCameraY == cameraY && !entityIsSelected &&
                        event.getX() == clickX && event.getY() == clickY) { // To prevent unwanted popups
                    if (System.currentTimeMillis() - starttime > 500) {
                        // Now add the entity - call the prompt fragment
                        entityCanvas.getActivity().toggleFragment(entityCanvas);
                    }
                }
            }
            // ======================== ACTION MOVE ====================================
            if( event.getAction() == MotionEvent.ACTION_MOVE){
                // If one of the entities is selected, then MOVE entities, not canvas
                for(Entity entity: entities) {
                    if(entity.isSelected()) {
                        entityIsSelected = true;
                        entity.setPosition(new Point((int)(event.getX()),
                                (int) (event.getY())));
                        entityCanvas.postInvalidate();
                    }
                }
                // If no entity is selected, we want to move the canvas
                if(!entityIsSelected) {
                    // If user has clicked on an entity he wants to drag a connector, so dont move the camera
                    if(dragPoint1 != null)
                        return true;
                    clampCameraPosition(cameraX, cameraY, event, v);
                    clickX = event.getX();
                    clickY = event.getY();
                }
            }
            return false;
        }
    };

    private static void clampCameraPosition(float cameraX, float cameraY, MotionEvent event, View v) {
        int bottomBound = entityCanvas.H_AREA_SIZE  * entityCanvas.IMAGE_SIZE_Y - v.getHeight() ;
        int topBound = -entityCanvas.H_AREA_SIZE * entityCanvas.IMAGE_SIZE_Y;
        int leftBound = -entityCanvas.H_AREA_SIZE * entityCanvas.IMAGE_SIZE_X;
        int rightBound = entityCanvas.H_AREA_SIZE * entityCanvas.IMAGE_SIZE_X - v.getWidth();
        double newCameraX = cameraX + (clickX - event.getX());
        double newCameraY = cameraY + (clickY - event.getY());
        if(!(newCameraX > rightBound || newCameraX < leftBound))
            cameraX += (clickX - event.getX());
        if(!(newCameraY > bottomBound || newCameraY < topBound))
            cameraY += (clickY - event.getY());
        entityCanvas.setCameraCoords(cameraX, cameraY);
        entityCanvas.postInvalidate();
    }

}
