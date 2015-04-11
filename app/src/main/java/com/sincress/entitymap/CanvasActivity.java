package com.sincress.entitymap;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class CanvasActivity extends ActionBarActivity{

    private EntityCanvas entityCanvas;
    private PromptFragment fragment;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment = new PromptFragment();
        manager = getFragmentManager();

        setContentView(R.layout.activity_canvas);

        entityCanvas = (EntityCanvas) findViewById(R.id.canvas);
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

            // Obtain data from fragment
            EntityManager.doFragmentAction(fragment.getView(), entityCanvas);
        }
    }

    /**
     * Called by prompt fragment's CANCEL button. Just removes the fragment.
     * @param v
     */
    public void removeFragment(View v){
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
        // Bring the canvas to top
        findViewById(R.id.canvas).bringToFront();
    }

}
