package com.sincress.entitymap;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sincress.entitymap.Abstract.Entity;

import java.util.ArrayList;

public class CanvasActivity extends ActionBarActivity{

    private EntityCanvas entityCanvas;
    private PromptFragment fragment;
    private FragmentManager manager;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen and if the canvas needs to be zoomed in
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            entityCanvas.checkScreenSizeBounds();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            entityCanvas.checkScreenSizeBounds();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment = new PromptFragment();
        manager = getFragmentManager();

        setContentView(R.layout.activity_canvas);

        entityCanvas = (EntityCanvas) findViewById(R.id.canvas);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.group, fragment, "Prompt");
        transaction.remove(fragment);
        transaction.commit();
    }

    /**
     * Callback: can either be called from the prompt's OK button or from the EntityCanvas when
     * adding new entities.
     * @param v view reference needed for XML onClick attribute
     */
    public void toggleFragment(View v) {
        // Hide canvas, show prompt
        if (v == entityCanvas) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.group, fragment, "Prompt");
            transaction.commit();

            // Bring the fragment inside the group to front
            findViewById(R.id.group).bringToFront();
        } else { // Hide prompt, show canvas
            // Bring the canvas to front
            findViewById(R.id.canvas).bringToFront();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_zoomin:
                entityCanvas.setScale(true);
                return true;
            case R.id.action_zoomout:
                entityCanvas.setScale(false);
                return true;
            case R.id.action_clear:
                ArrayList<Entity> entities = EntityManager.getDefaultEntities();
                entityCanvas.setEntities(entities);
                EntityManager.saveEntities(entities, "entities");
                entityCanvas.postInvalidate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
