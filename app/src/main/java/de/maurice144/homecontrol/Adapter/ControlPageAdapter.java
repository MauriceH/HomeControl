package de.maurice144.homecontrol.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.maurice144.homecontrol.Data.ControlPage;
import de.maurice144.homecontrol.FrontEnd.Control_Main_Fragment;

/**
 * Created by Maurice on 22.07.2015.
 */
public class ControlPageAdapter extends FragmentPagerAdapter {

    private ArrayList<Control_Main_Fragment> fragments;
    private JSONArray pages;

    public ControlPageAdapter(FragmentManager fm,JSONArray pages) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.pages = pages;
        for(int i=0;i<this.pages.length();i++) {
            this.fragments.add(null);
        }
    }


    @Override
    public Fragment getItem(int position) {

        Control_Main_Fragment fragment =  this.fragments.get(position);
        if(fragment == null) {
            Bundle data = new Bundle();
            JSONObject jsonObject;
            try {
                jsonObject = this.pages.getJSONObject(position);
                data.putString("data",jsonObject.toString());
                fragment = new Control_Main_Fragment();
                fragment.setArguments(data);
                this.fragments.set(position,fragment);
            } catch (Exception ex) {
                Log.e("getFragmentItem",ex.getMessage(),ex);
                return null;
            }
        }
        return fragment;
    }

    public ArrayList<ControlPage> getPages() {
        ArrayList<ControlPage> retPages = new ArrayList<>();
        for(Control_Main_Fragment fragment : fragments) {
            if(fragment != null)
                retPages.add(fragment.getPage());
        }
        return retPages;
    }

    @Override
    public int getCount() {
        return pages.length();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        JSONObject jsonObject;
        try {
            jsonObject = this.pages.getJSONObject(position);
            if(jsonObject != null) {
                return jsonObject.optString("title","Kein Titel");
            }
        } catch (Exception ex) {
            Log.e("GetFragementTitle",ex.getMessage(),ex);
        }
        return "Fehler";
    }
}