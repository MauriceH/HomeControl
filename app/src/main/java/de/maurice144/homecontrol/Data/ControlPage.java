package de.maurice144.homecontrol.Data;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.maurice144.homecontrol.FrontEnd.Control_Main_Fragment;

/**
 * Created by Maurice on 22.07.2015.
 */
public class ControlPage {

    private JSONObject data;
    private ArrayList<ControlGroup> groups;
    private Control_Main_Fragment fragement;

    public ControlPage(JSONObject jsonObject,Context context) {
        this.data = jsonObject;
        this.groups = new ArrayList<ControlGroup>();
        this.fragement = new Control_Main_Fragment();

        JSONArray items = this.data.optJSONArray("groups");
        JSONObject jsonObj;
        ControlGroup controlGroup;
        if(items != null && items.length() > 0) {
            for(int i=0;i<items.length();i++) {
                jsonObj = items.optJSONObject(i);
                if(jsonObj != null) {
                    controlGroup = new ControlGroup(jsonObj,context);
                    this.getGroups().add(controlGroup);
                }
            }
        }
    }

    public long getId() {
        return data.optLong("id",0);
    }

    public String getTitle() {
        return data.optString("title", "No Title");
    }


    public ArrayList<ControlGroup> getGroups() {
        return groups;
    }
}
