package de.maurice144.homecontrol.FrontEnd.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.iid.InstanceID;

import de.maurice144.homecontrol.Data.LocalSettings;
import de.maurice144.homecontrol.FrontEnd.Activity.SettingsActivity;
import de.maurice144.homecontrol.GCM.RegistrationIntentService;
import de.maurice144.homecontrol.MainControlActivity;
import de.maurice144.homecontrol.R;

public class StartActivity extends ActionBarActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "StartActivity";

    LocalSettings settings;

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


        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }


    }


    public void onMenu_StartControl_Click(View v) {
        if(checkLogon()) return;
        startActivity(new Intent(this, MainControlActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onMenu_StartSettings_Click(View v) {
        if(checkLogon()) return;
        startActivity(new Intent(this, SettingsActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onMenu_StartMusic_Click(View v) {
        LocalSettings settings = new LocalSettings(this);
        settings.setNewActivation(null, -1, null);
        settings.Save();
        Toast.makeText(this,"Aktivierung gelöscht",Toast.LENGTH_SHORT).show();
    }


    private boolean checkLogon() {
        settings = new LocalSettings(this);
        if(!settings.isLoggedIn()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Anmeldung");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Für diese APP benötigen Sie einen Home-Control Account. Bitte melden Sie sich mit Ihren Zugangsdaten an")
                    .setCancelable(false)
                    .setPositiveButton("Anmelden", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(StartActivity.this,LoginActivity.class));
                            StartActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Beenden", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            StartActivity.this.finish();
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
            return true;
        }
        return false;
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }



}
