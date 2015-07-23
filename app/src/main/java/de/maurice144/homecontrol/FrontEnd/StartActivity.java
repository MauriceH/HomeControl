package de.maurice144.homecontrol.FrontEnd;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import de.maurice144.homecontrol.MainControlActivity;
import de.maurice144.homecontrol.R;

public class StartActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ImageButton button = (ImageButton)findViewById(R.id.startscreen_menu_control);

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });




    }


    public void onMenu_StartControl_Click(View v) {
        startActivity(new Intent(this, MainControlActivity.class));
    }

    public void onMenu_StartSettings_Click(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }



}
