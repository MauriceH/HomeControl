package de.maurice144.homecontrol.FrontEnd.Activity;

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
import de.maurice144.homecontrol.Data.ControlPage;
import de.maurice144.homecontrol.R;


public class MainControlActivity extends ActionBarActivity implements ActionBar.TabListener {

    private String jsonData = "  {                                                     " +
            "    \"pages\":[                                         " +
            "        {                                               " +
            "          \"id\":0,                                     " +
            "          \"title\":\"Dachgeschoss\",                   " +
            "          \"groups\":[                                  " +
            "              {                                         " +
            "                \"id\":0,                               " +
            "                \"title\":\"Schlafzimmer\",             " +
            "                \"controls\":[                          " +
            "                    {                                   " +
            "                      \"controltype\":\"light\",        " +
            "                      \"id\":0,                         " +
            "                      \"title\":\"Licht\"               " +
            "                    },                                  " +
            "                    {                                   " +
            "                      \"controltype\":\"tv\",           " +
            "                      \"id\":1,                         " +
            "                      \"title\":\"Fernseher\"           " +
            "                    }                                   " +
            "                  ]                                     " +
            "              },                                        " +
            "              {                                         " +
            "                \"id\":1,                               " +
            "                \"title\":\"Allgemein\",                " +
            "                \"controls\":[                          " +
            "                    {                                   " +
            "                      \"controltype\":\"light\",        " +
            "                      \"id\":2,                         " +
            "                      \"title\":\"Ankleidelicht\"       " +
            "                    },                                  " +
            "                    {                                   " +
            "                      \"controltype\":\"light\",        " +
            "                      \"id\":3,                         " +
            "                      \"title\":\"Flurlicht\"           " +
            "                    }                                   " +
            "                  ]                                     " +
            "              }                                         " +
            "            ]                                           " +
            "        },                                              " +
            "        {                                               " +
            "          \"id\":1,                                     " +
            "          \"title\":\"Obergeschoss\",                   " +
            "          \"groups\":[                                  " +
            "              {                                         " +
            "                \"id\":0,                               " +
            "                \"title\":\"Schlafzimmer\",             " +
            "                \"controls\":[                          " +
            "                    {                                   " +
            "                      \"controltype\":\"light\",        " +
            "                      \"id\":0,                         " +
            "                      \"title\":\"Licht\"               " +
            "                    },                                  " +
            "                    {                                   " +
            "                      \"controltype\":\"tv\",           " +
            "                      \"id\":1,                         " +
            "                      \"title\":\"Fernseher\"           " +
            "                    }                                   " +
            "                  ]                                     " +
            "              },                                        " +
            "              {                                         " +
            "                \"id\":1,                               " +
            "                \"title\":\"Allgemein\",                " +
            "                \"controls\":[                          " +
            "                    {                                   " +
            "                      \"controltype\":\"light\",        " +
            "                      \"id\":2,                         " +
            "                      \"title\":\"Licht Ankleiderzimmer\" " +
            "                    },                                  " +
            "                    {                                   " +
            "                      \"controltype\":\"light\",        " +
            "                      \"id\":3,                         " +
            "                      \"title\":\"Flurlicht\"           " +
            "                    }                                   " +
            "                  ]                                     " +
            "              }                                         " +
            "            ]                                           " +
            "        },                                              " +
            "        {                                               " +
            "          \"id\":2,                                     " +
            "          \"title\":\"Erdgeschoss\",                    " +
            "          \"groups\":[                                  " +
            "              {                                         " +
            "                \"id\":0,                               " +
            "                \"title\":\"Schlafzimmer\",             " +
            "                \"controls\":[                          " +
            "                    {                                   " +
            "                      \"controltype\":\"light\",        " +
            "                      \"id\":0,                         " +
            "                      \"title\":\"Licht\"               " +
            "                    },                                  " +
            "                    {                                   " +
            "                      \"controltype\":\"tv\",           " +
            "                      \"id\":1,                         " +
            "                      \"title\":\"Fernsehn\"            " +
            "                    }                                   " +
            "                  ]                                     " +
            "              },                                        " +
            "              {                                         " +
            "                \"id\":1,                               " +
            "                \"title\":\"Allgemein\",                " +
            "                \"controls\":[                          " +
            "                    {                                   " +
            "                      \"controltype\":\"light\",        " +
            "                      \"id\":2,                         " +
            "                      \"title\":\"Ankleidelicht\"       " +
            "                    },                                  " +
            "                    {                                   " +
            "                      \"controltype\":\"light\",        " +
            "                      \"id\":3,                         " +
            "                      \"title\":\"Flurlicht\"           " +
            "                    }                                   " +
            "                  ]                                     " +
            "              }                                         " +
            "            ]                                           " +
            "        }                                               " +
            "      ]                                                 " +
            "  }                                                     "  ;



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
            rootJsonElement = new JSONObject(jsonData);
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
