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

import de.maurice144.homecontrol.Communication.SynchronisationService;
import de.maurice144.homecontrol.Data.LocalSettings;
import de.maurice144.homecontrol.GCM.RegistrationIntentService;
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

        if(getSettings().isLoggedIn()) {
            if (!getSettings().isGcmRegistered()) {
                if (checkPlayServices()) {
                    // Start IntentService to register this application with GCM.
                    Intent intent = new Intent(this, RegistrationIntentService.class);
                    startService(intent);
                }
            } else {
                if(!getSettings().isGcmTokenTransfered()) {
                    startService(SynchronisationService.getServiceStartIntentByMode(this,SynchronisationService.STARTMODE_SendGcmToken));
                }
            }
        }
    }


    public void onMenu_StartControl_Click(View v) {
        if(!checkServer()) return;
        if(!checkLogon()) return;
        if(!settings.isStructureAvailable()) {
            displayNoStructureDialog(settings);
            return;
        }
        if(!getSettings().isGcmTokenTransfered()) {
            startService(SynchronisationService.getServiceStartIntentByMode(this,SynchronisationService.STARTMODE_SendGcmToken));
            displayNoGcmSentDialog(settings);
            return;
        }
        startActivity(new Intent(this, MainControlActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onMenu_StartSettings_Click(View v) {
        if(!checkServer()) return;
        startActivity(new Intent(this, SettingsActivity.class));
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    public void onMenu_StartMusic_Click(View v) {
        // TODO Temp for app test
        LocalSettings settings = new LocalSettings(this);
        settings.clearAccountData();
        settings.setGcmToken(null);
        settings.Save();
        Toast.makeText(this,"Aktivierung gelöscht", Toast.LENGTH_SHORT).show();
    }


    private boolean checkLogon() {
        settings = getSettings();
        if (settings.isLoggedIn()) {
            return true;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Anmeldung");

        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.login_text))
                .setCancelable(false)
                .setPositiveButton("Anmelden", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(StartActivity.this,LoginActivity.class));
                        StartActivity.this.finish();
                    }
                })
                .setNegativeButton("Abbrechen", null);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        return false;
    }

    private boolean checkServer() {
        settings = getSettings();
        if (settings.isServerConfigured()) {
            return true;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Server");

        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.server_text))
                .setCancelable(false)
                .setPositiveButton("Konfigurieren", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(StartActivity.this, ServerSettingActivity.class));
                    }
                })
                .setNegativeButton("Abbrechen", null);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
        return false;
    }

    private boolean checkServer() {
        settings = getSettings();
        if(!settings.isServerConfigured()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Servereinstellung");

            // set dialog message
            alertDialogBuilder
                    .setMessage(getString(R.string.login_text))
                    .setCancelable(false)
                    .setPositiveButton("Konfigurieren", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(StartActivity.this,ServerSettingActivity.class));
                            StartActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Abbrechen", null);

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
            return false;
        }
        return true;
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

    private LocalSettings getSettings() {
        return new LocalSettings(this);
    }



    private void displayNoStructureDialog(LocalSettings settings) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle("Keine Strukturdaten");
        // set dialog mesage
        alertDialogBuilder
                .setMessage(getString(R.string.nostructure_text))
                .setCancelable(false)
                .setPositiveButton("Synchronisieren", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startService(SynchronisationService.getServiceStartIntentByMode(StartActivity.this, SynchronisationService.STARTMODE_SyncStructure));
                    }
                })
                .setNegativeButton("Beenden", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StartActivity.this.finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void displayNoGcmSentDialog(LocalSettings settings) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle("Kein Push-Token");
        // set dialog mesage
        alertDialogBuilder
                .setMessage(getString(R.string.nogcmsent_text))
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }



}
