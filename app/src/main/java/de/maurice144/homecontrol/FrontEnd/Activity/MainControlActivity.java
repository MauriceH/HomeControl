package de.maurice144.homecontrol.FrontEnd.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.maurice144.homecontrol.Adapter.ControlPageAdapter;
import de.maurice144.homecontrol.Communication.SynchronisationService;
import de.maurice144.homecontrol.Data.ControlPage;
import de.maurice144.homecontrol.Data.ControlStructureJsonFile;
import de.maurice144.homecontrol.Data.LocalSettings;
import de.maurice144.homecontrol.R;


public class MainControlActivity extends ActionBarActivity implements ActionBar.TabListener {


    private ArrayList<ControlPage> pages;

    ControlPageAdapter controlPageAdapter;
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_control);



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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_control, menu);
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
