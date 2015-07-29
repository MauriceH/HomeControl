package de.maurice144.homecontrol.FrontEnd.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import java.util.Timer;
import java.util.TimerTask;

import de.maurice144.homecontrol.Data.LocalSettings;
import de.maurice144.homecontrol.R;

public class SyncWaitActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_wait);
        callAsynchronousTask();
    }


    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        final Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        LocalSettings settings = new LocalSettings(SyncWaitActivity.this);
                        if(settings.isStructureAvailable()) {
                            timer.cancel();
                            SyncWaitActivity.this.startActivity(new Intent(SyncWaitActivity.this,MainControlActivity.class));
                            SyncWaitActivity.this.finish();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 500); //execute in every 50000 ms

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
