package com.sincress.entitymap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class EntityCanvas extends View {

    private float cameraX = 0, cameraY = 0;
    //private Canvas canvas;
    private ArrayList<Entity> entities;
    private ArrayList<Connector> connectors;
    private CanvasActivity activity;

    public EntityCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        entities = new ArrayList<>();
        connectors = new ArrayList<>();
        activity = (CanvasActivity)context;
        // Set the instance of the entity canvas needed in the onTouch method
        EntityManager.setCanvasInstance(this);
        // Set the onClick listeners defined in EntityManager
        this.setOnTouchListener(EntityManager.onTouchListener);
    }


    public Point getCameraCoords(){
        return new Point((int)(cameraX), (int)(cameraY));
    }

    public void setCameraCoords(float x, float y){
        cameraX = x;
        cameraY = y;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public ArrayList<Connector> getConnectors() {
        return connectors;
    }

    public CanvasActivity getActivity(){
        return activity;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(-cameraX, -cameraY);
        // Draw all connectors
        for (Connector line: connectors)
            line.drawConnector(canvas);
        // Draw all entities drawn to map
        for (Entity pair : entities)
            pair.drawEntity(canvas);
        canvas.translate(cameraX, cameraY);
    }

    /**
     * Class describring a connector line, with its startpoint and endpoint defined.
     */
    public static class Connector{
        public Entity start, end;

        // Connector is defined by the start entity and the end entity
        public Connector(Entity start, Entity end){
            this.start = start;
            this.end = end;
        }

        public void drawConnector(Canvas canvas){
            Paint paint = new Paint();
            paint.setStrokeWidth(3);
            float hEntityHeight = end.getEntityDimens().y / 2;
            float hEntity1Width = start.getEntityDimens().x / 2, hEntity2Width = end.getEntityDimens().x / 2 ;

            canvas.drawLine(start.getPosition().x + hEntity1Width, start.getPosition().y + hEntityHeight,
                    end.getPosition().x + hEntity2Width, end.getPosition().y + hEntityHeight, paint);
            /*float arrowX = end.getPosition().x > start.getPosition().x ? (end.getPosition().x+start.getPosition().x) /2:
                    (start.getPosition().x - end.getPosition().x)/2;
            float arrowY = end.getPosition().y > start.getPosition().y ? (end.getPosition().y - start.getPosition().y)/2 :
                    (start.getPosition().y - end.getPosition().y)/2;
            float coef =  (end.getPosition().x - start.getPosition().x) / (end.getPosition().y - start.getPosition().y);
            float q = coef*coef + 1;

            double arrow1X = (q*arrowX + 20*Math.sqrt(q)) / q;
            double arrow1Y = coef*(arrow1X - arrowX) + arrowY;
            double arrow2X = (q*arrowX - 20*Math.sqrt(q)) / q;
            double arrow2Y = coef*(arrow2X - arrowX) + arrowY;
            canvas.drawLine((float)arrow1X, (float)arrow1Y, arrowX, arrowY, paint);
            canvas.drawLine((float)arrow2X, (float)arrow2Y, arrowX, arrowY, paint);*/

        }
    }
}
