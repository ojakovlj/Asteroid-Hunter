package com.sincress.entitymap;


import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
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
        EntityManager.entityCanvas = entityCanvas;
        EditText dateView = (EditText) fragmentview.findViewById(R.id.dateField);
        EditText descView = (EditText) fragmentview.findViewById(R.id.descField);
        String color = ((Spinner) fragmentview.findViewById(R.id.colorSpinner)).getSelectedItem().toString();

        int intColor = 0;
        switch(color){
            case "Red":
                intColor = Color.RED;
                break;
            case "Blue":
                intColor = Color.BLUE;
                break;
            case "Yellow":
                intColor = Color.YELLOW;
                break;
            case "Green":
                intColor = Color.GREEN;
                break;
            case "Magenta":
                intColor = Color.MAGENTA;
                break;
            case "Gray":
                intColor = Color.GRAY;
                break;
            case "Cyan":
                intColor = Color.CYAN;
                break;
        }
        Entity entity = new DateEventEntity(dateView.getText().toString(),
                descView.getText().toString(),
                intColor,
                new Point((int)(clickX + entityCanvas.getCameraCoords().x),
                        (int)(clickY + entityCanvas.getCameraCoords().y)));
        entityCanvas.getEntities().add(entity);
        // Call postInvalidate to redraw the scene
        entityCanvas.postInvalidate();
        // Reset the fields
        dateView.setText("");   dateView.setHint("Date");
        descView.setText("");   descView.setHint("Description");
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
                    cameraX += (clickX - event.getX());
                    cameraY += (clickY - event.getY());
                    entityCanvas.setCameraCoords(cameraX, cameraY);
                    entityCanvas.postInvalidate();
                    clickX = event.getX();
                    clickY = event.getY();
                }
            }
            return false;
        }
    };

}
