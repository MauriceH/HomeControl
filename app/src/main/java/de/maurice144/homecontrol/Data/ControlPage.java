package de.maurice144.homecontrol.Data;

import android.app.Activity;

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

    public ControlPage(JSONObject jsonObject,Activity activity) {
        this.data = jsonObject;
        this.groups = new ArrayList<>();
        this.fragement = new Control_Main_Fragment();

        JSONArray items = this.data.optJSONArray("Groups");
        JSONObject jsonObj;
        ControlGroup controlGroup;
        if(items != null && items.length() > 0) {
            for(int i=0;i<items.length();i++) {
                jsonObj = items.optJSONObject(i);
                if(jsonObj != null) {
                    controlGroup = new ControlGroup(jsonObj, activity);
                    this.getGroups().add(controlGroup);
                }
            }
        }
    }

    public long getId() {
        return data.optLong("Id",0);
    }

    public String getTitle() {
        return data.optString("Title", "No Title");
    }


    public ArrayList<ControlGroup> getGroups() {
        return groups;
    }


    public void RecursiveAddControls(ArrayList<ControlGroupItemBase> items) {
        for(ControlGroup group : groups) {
            items.addAll(group.getControls());
        }
    }

    public ControlGroupItemBase RecursiveFindControlById(long id) {
        for(ControlGroup group : groups) {
            for(ControlGroupItemBase item : group.getControls()) {
                if(item.getId() == id) {
                    return item;
                }
            }
        }
        return null;
    }

}
