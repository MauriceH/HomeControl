package de.maurice144.homecontrol.FrontEnd.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 12345;

    LocalSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_DENIED) {
            ResumeCreate();
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }

    }

    private void ResumeCreate() {
        setContentView(R.layout.activity_start);
        ImageButton button = (ImageButton)findViewById(R.id.startscreen_menu_control);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        if(getSettings().isLoggedIn()) {
            if (!getSettings().isGcmRegistrationObtained()) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    ResumeCreate();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Diese App benötigt Zugriff auf die SDKarte", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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
//        LocalSettings settings = new LocalSettings(this);
//        settings.clearAccountData();
//        settings.setGcmToken(null);
//        Toast.makeText(this,"Aktivierung gelöscht", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,DimmerActivity.class);
        startActivity(intent);
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


    public void onMenu_Timer_Click(View view) {
        startActivity(new Intent(this,ActiveControlsActivity.class));
    }
}
