package de.maurice144.homecontrol.FrontEnd.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.maurice144.homecontrol.Adapter.ControlPageAdapter;
import de.maurice144.homecontrol.Communication.SynchronisationService;
import de.maurice144.homecontrol.Data.ControlGroupItemBase;
import de.maurice144.homecontrol.Data.ControlPage;
import de.maurice144.homecontrol.Data.ControlStructureJsonFile;
import de.maurice144.homecontrol.Data.LocalSettings;
import de.maurice144.homecontrol.R;


public class MainControlActivity extends ActionBarActivity implements ActionBar.TabListener {


    ControlPageAdapter controlPageAdapter;
    ViewPager mViewPager;

    BroadcastReceiver mBroadcastReceiver;

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
                    setControlState(intent.getExtras());
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


        JSONArray jsonPages = rootJsonElement.optJSONArray("pages");
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


    private void setControlState(Bundle data) {
        ControlGroupItemBase item;
        long controlId;
        String conId = data.getString("controlid");
        controlId = Long.parseLong(conId);
        ArrayList<ControlPage> pages = controlPageAdapter.getPages();
        for(ControlPage pg : pages) {
            item = pg.RecursiveFindControlById(controlId);
            if(item != null) {
                item.SetState(data);
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
