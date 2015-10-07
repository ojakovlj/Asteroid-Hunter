package com.sincress.entitymap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sincress.entitymap.Abstract.Entity;

import java.util.ArrayList;

public class EntityCanvas extends View {
    public static EntityCanvas instance;
    private double cameraX = 0, cameraY = 0;
    //private Canvas canvas;
    private ArrayList<Entity> entities;
    private ArrayList<Connector> connectors;
    private CanvasActivity activity;
    private Bitmap background, spaceship_originalBMP, trailBmp;
    private Matrix mtx;
    public double shipHeading = 0, shipSpeed = 0;
    public boolean actionDone = false;
    public final int H_AREA_SIZE = 5;
    public int IMAGE_SIZE_X, IMAGE_SIZE_Y;
    public float ZOOM_LEVEL = 1.0f;

    private int[] splash_alphas = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] splash_xcoords = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int[] splash_ycoords = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    public double shipX = 0, shipY = 0;

    public EntityCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        entities = new ArrayList<>();
        connectors = new ArrayList<>();
        activity = (CanvasActivity)context;
        // Set the instance of the entity canvas needed in the onTouch method
        EntityManager.setCanvasInstance(this);
        instance = this;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.spacebackground);
        trailBmp = BitmapFactory.decodeResource(getResources(), R.drawable.splash);
        IMAGE_SIZE_X = background.getWidth();
        IMAGE_SIZE_Y = background.getHeight();
        EntityManager.initialise();

        // Initialise the original spaceship bitmap to be used in OnDraw
        spaceship_originalBMP = BitmapFactory.decodeResource(getResources(), R.drawable.spaceship);
        // Prevent constant object re-allocation in onDraw
        mtx = new Matrix();

        // Set the onClick listeners defined in EntityManager
        this.setOnTouchListener(EntityManager.onTouchListener);

        //RENDERING THREAD Game Main Loop
        new Thread(new Runnable() {

            public void run() {
                while (true) { //otherwise thread will run only once
                    try {
                        Thread.sleep(25); //25 ms sleep, rendering @ 40 FPS
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    cameraX += Math.cos(shipHeading)*shipSpeed;
                    cameraY += Math.sin(shipHeading)*shipSpeed;
                    shipX = cameraX;// + entityCanvas.getWidth()/2;
                    shipY = cameraY;// + entityCanvas.getHeight()/2;
                    if(actionDone) {
                        if (shipSpeed > 0.2)
                            shipSpeed -= 0.2;
                        else shipSpeed = 0;
                    }

                    // Clamp camera position
                    float ScrnW = getWidth()/ZOOM_LEVEL, ScrnH = getHeight()/ZOOM_LEVEL;
                    int bottomBound = H_AREA_SIZE  * IMAGE_SIZE_Y - (int)(ScrnH);
                    int topBound = -H_AREA_SIZE * IMAGE_SIZE_Y;
                    int leftBound = -H_AREA_SIZE * IMAGE_SIZE_X;
                    int rightBound = H_AREA_SIZE * IMAGE_SIZE_X - (int)(ScrnW);
                    if(cameraX > rightBound){
                        Log.d("Adjust ", "Right");
                        cameraX = rightBound;
                    }
                    if(cameraX < leftBound) {
                        Log.d("Adjust ", "Left");
                        cameraX = leftBound;
                    }
                    if(cameraY < topBound) {
                        Log.d("Adjust ", "Top");
                        cameraY = topBound;
                    }
                    if(cameraY > bottomBound) {
                        Log.d("Adjust ", "Bottom");
                        cameraY = bottomBound;
                    }

                    postInvalidate();
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                while (true) { //otherwise thread will run only once
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < 10; i++) {
                        if(shipSpeed < 0.1)
                            break;
                        try {
                            Thread.sleep(100); //100 ms sleep to update the ship splashes
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        splash_xcoords[i] = (int) (shipX + getWidth() / 2 /ZOOM_LEVEL);
                        splash_ycoords[i] = (int) (shipY + getHeight() / 2 / ZOOM_LEVEL);
                        splash_alphas[i] = 255;
                    }
                }
            }
        }).start();
    }


    public Point getCameraCoords(){
        return new Point((int)(cameraX), (int)(cameraY));
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
    public void setEntities(ArrayList<Entity> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
    }

    public ArrayList<Connector> getConnectors() {
        return connectors;
    }

    public CanvasActivity getActivity(){
        return activity;
    }

    public void setScale(boolean zoomin){
        if(zoomin) {
            if (ZOOM_LEVEL < 1.5f) {
                Log.d("Moving camera by ",(ZOOM_LEVEL*getWidth() - (ZOOM_LEVEL-0.5)*getWidth())+" left");
                double offsetX = ZOOM_LEVEL*getWidth() - (ZOOM_LEVEL-0.5)*getWidth();
                double offsetY = ZOOM_LEVEL*getHeight() - (ZOOM_LEVEL-0.5)*getHeight();
                cameraX += ZOOM_LEVEL == 1.0 ? offsetX/2 : offsetX;
                cameraY += ZOOM_LEVEL == 1.0 ? offsetY/2 : offsetY;
                ZOOM_LEVEL += 0.5f;
            }
        }
        else
            if (ZOOM_LEVEL > 0.5) {
                // Prevent zoom out if the screen is larger than the area
                if (this.getHeight() > (ZOOM_LEVEL-0.5) * (H_AREA_SIZE * 2 * IMAGE_SIZE_Y) ||
                        this.getWidth() > (ZOOM_LEVEL-0.5) * (H_AREA_SIZE * 2 * IMAGE_SIZE_X))
                    return;
                Log.d("Moving camera by ",(ZOOM_LEVEL*getWidth() - (ZOOM_LEVEL+0.5)*getWidth())+" left");
                double offsetX = ZOOM_LEVEL*getWidth() - (ZOOM_LEVEL+0.5)*getWidth();
                double offsetY = ZOOM_LEVEL*getHeight() - (ZOOM_LEVEL+0.5)*getHeight();
                cameraX += ZOOM_LEVEL == 1.5 ? offsetX/2 : offsetX;
                cameraY += ZOOM_LEVEL == 1.5 ? offsetY/2 : offsetY;
                ZOOM_LEVEL -= 0.5f;
            }
        // TODO camera position correction

        // Clamp camera position if the user unzooms while facing a lower/right boundary
        int bottomBound = H_AREA_SIZE  * IMAGE_SIZE_Y - (int)(this.getHeight()/ZOOM_LEVEL);
        int rightBound = H_AREA_SIZE * IMAGE_SIZE_X - (int)(this.getWidth()/ZOOM_LEVEL);
        if(cameraX + (int)(this.getWidth()/ZOOM_LEVEL) > rightBound)
            cameraX -= 0.75*(this.getHeight()/ZOOM_LEVEL);
        if(cameraY + (int)(this.getHeight()/ZOOM_LEVEL) > bottomBound)
            cameraY -= 0.75*(this.getHeight()/ZOOM_LEVEL);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.scale(ZOOM_LEVEL, ZOOM_LEVEL);
        canvas.translate(-(float)cameraX, -(float)cameraY);
        // Draw our background, tiled
        for(int j=-H_AREA_SIZE; j<H_AREA_SIZE; j++)
            for(int i=-H_AREA_SIZE; i<H_AREA_SIZE; i++)
                canvas.drawBitmap(background, IMAGE_SIZE_X*i, IMAGE_SIZE_Y*j, null);
        // Draw all connectors
        for (Connector line: connectors)
            line.drawConnector(canvas);
        // Draw all entities drawn to map
        for (Entity entity : entities)
            entity.drawEntity(canvas);
        drawTrail(canvas);
        canvas.translate((float)cameraX, (float)cameraY);

        mtx.reset();
        mtx.setRotate((float)(shipHeading*180/Math.PI), 0, 0);   // rotating 180 degrees clockwise
        mtx.postScale(0.5f, 0.5f, spaceship_originalBMP.getWidth() / 4,
                spaceship_originalBMP.getHeight() / 4); //default scale
        Bitmap spaceship = Bitmap.createBitmap(spaceship_originalBMP, 0, 0,
                spaceship_originalBMP.getWidth(), spaceship_originalBMP.getHeight(), mtx, true);
        float spaceshipX = this.getWidth()/2/ZOOM_LEVEL-spaceship.getWidth()/2;
        float spaceshipY = this.getHeight()/2/ZOOM_LEVEL-spaceship.getHeight()/2;
        canvas.drawBitmap(spaceship, spaceshipX, spaceshipY, null);
    }

    public void drawTrail(Canvas canvas) {
        //WATER SPLASH DRAWING
        ArrayList<Bitmap> splashes = new ArrayList<Bitmap>();
        Paint paint = new Paint();

        for (int i = 0; i < 10; i++) {
            splashes.add(trailBmp);
            paint.setAlpha(splash_alphas[i]);
            if (splash_alphas[i] > 3) splash_alphas[i] -= 3;
            canvas.drawBitmap(splashes.get(i), splash_xcoords[i] - splashes.get(i).getWidth() / 2,
                    splash_ycoords[i] - splashes.get(i).getWidth() / 2, paint);
        }
    }

    public void checkScreenSizeBounds() {
        // If the screen is larger than the relevant canvas dimension, zoom in
        if (this.getHeight() > ZOOM_LEVEL * (H_AREA_SIZE * 2 * IMAGE_SIZE_Y) ||
                this.getWidth() > ZOOM_LEVEL * (H_AREA_SIZE * 2 * IMAGE_SIZE_X))
            setScale(true);
    }

    /**
     * Class describring a connector line, with its startpoint and endpoint defined.
     */
    public static class Connector{
        public Entity start, end;

        public void drawConnector(Canvas canvas){
            Paint paint = new Paint();
            paint.setStrokeWidth(3);
            float hEntityHeight = end.getEntityDimens().y / 2;
            float hEntity1Width = start.getEntityDimens().x / 2, hEntity2Width = end.getEntityDimens().x / 2 ;
            canvas.drawLine(start.getPosition().x + hEntity1Width, start.getPosition().y + hEntityHeight,
                    end.getPosition().x + hEntity2Width, end.getPosition().y + hEntityHeight, paint);

        }
    }
}
