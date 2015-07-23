package de.maurice144.homecontrol.FrontEnd.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import de.maurice144.homecontrol.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.fadeout, R.anim.fadein);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }


    public void onLoginClicked(View v) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }


}
