package de.maurice144.homecontrol.FrontEnd.Activity;

import android.app.ActionBar;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.maurice144.homecontrol.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // get the parent view of home (app icon) imageview
            ViewGroup home = (ViewGroup) findViewById(android.R.id.home);
            home = (ViewGroup)home.getParent();

            // get the first child (up imageview)
            ( (ImageView) home.getChildAt(0) )
                    .setImageResource(R.drawable.startscreen_settings);
        } else {
            // get the up imageview directly with R.id.up
            ( (ImageView) findViewById(R.id.up) )
                    .setImageResource(R.drawable.startscreen_settings);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
