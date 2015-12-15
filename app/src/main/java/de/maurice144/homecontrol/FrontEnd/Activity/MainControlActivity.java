package de.maurice144.homecontrol.FrontEnd.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.maurice144.homecontrol.Adapter.ControlPageAdapter;
import de.maurice144.homecontrol.Communication.Requests.SecureDefaultRequest;
import de.maurice144.homecontrol.Communication.Results.ControlStateResult;
import de.maurice144.homecontrol.Communication.SynchronisationService;
import de.maurice144.homecontrol.Communication.WebApi;
import de.maurice144.homecontrol.Data.ControlGroupItemBase;
import de.maurice144.homecontrol.Data.ControlPage;
import de.maurice144.homecontrol.Data.ControlStructureJsonFile;
import de.maurice144.homecontrol.Data.LocalSettings;
import de.maurice144.homecontrol.R;


public class MainControlActivity extends ActionBarActivity implements ActionBar.TabListener {


    ControlPageAdapter controlPageAdapter;
    ViewPager mViewPager;

    BroadcastReceiver mBroadcastReceiver;

    public JSONArray states;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_control);


        if(!checkStructuredataAvailable()) {
            return;
        }


        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String messageMode = intent.getStringExtra("mode");


                if(messageMode == null)
                    return;

                if(messageMode.equalsIgnoreCase("structurechanged")) {
                    showStructureSyncDialog();
                }

                if(messageMode.equalsIgnoreCase("controlstatechanged")) {
                    long controlId = Long.parseLong(intent.getStringExtra("controlid"));
                    int state = intent.getStringExtra("state").equals("on") ? 1 : 0;
                    setControlState(controlId,state);
                    if(states == null) {
                        return;
                    }

                    JSONObject obj;
                    for(int i=0;i<states.length();i++) {
                        try {
                            obj = states.getJSONObject(i);
                            if(obj.getLong("Id") == controlId) {
                                obj.put("State",state);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        };



        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);




        JSONObject rootJsonElement;
        try {
            ControlStructureJsonFile controlStructureJsonFile = ControlStructureJsonFile.LoadFile();
            rootJsonElement = controlStructureJsonFile.getObj();
        } catch (Exception ex) {
            rootJsonElement = null;
            // NOTHING TO SHOW
            finish();
            return;
        }





        JSONArray jsonPages = rootJsonElement.optJSONArray("Pages");
        controlPageAdapter = new ControlPageAdapter(getSupportFragmentManager(),jsonPages);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(controlPageAdapter);

         mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < controlPageAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(controlPageAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


        AsyncTask<String,String,String> task = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    WebApi api = new WebApi(MainControlActivity.this);
                    final ControlStateResult result =  api.ControlStates(new SecureDefaultRequest(new LocalSettings(MainControlActivity.this).getDeviceToken()));
                    if(!result.isDoneCorrect()) {
                        return null;
                    }

                    MainControlActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                states = result.getControls();
                                controlPageAdapter.resyncStates();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } catch (Exception ex) {
                    Log.e("statesync",ex.getMessage(),ex);
                }
                return null;
            }
        };
        task.execute();

    }


    @Override
    protected void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter("de.maurice144.homecontrol.event.control"));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    private void setControlState(long controlId, int state) {
        ControlGroupItemBase item;
        ArrayList<ControlPage> pages = controlPageAdapter.getPages();
        for(ControlPage pg : pages) {
            item = pg.RecursiveFindControlById(controlId);
            if(item != null) {
                item.SetState(state);
            }
        }
    }




    private boolean checkStructuredataAvailable() {
        LocalSettings settings = new LocalSettings(this);
        if (settings.isStructureAvailable()) {
            return true;
        } else {
            showStructureSyncDialog();
            return false;
        }
    }

    private void showStructureSyncDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainControlActivity.this);
        alertDialogBuilder.setTitle("Strukturdaten");
        alertDialogBuilder
                .setMessage(getString(R.string.structurechange_text))
                .setCancelable(false)
                .setPositiveButton("Warten", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainControlActivity.this.startActivity(new Intent(MainControlActivity.this, SyncWaitActivity.class));
                        MainControlActivity.this.finish();
                    }
                })
                .setNegativeButton("Nein danke", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainControlActivity.this.finish();
                    }
                });
        alertDialogBuilder.create().show();
    }



    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


}

