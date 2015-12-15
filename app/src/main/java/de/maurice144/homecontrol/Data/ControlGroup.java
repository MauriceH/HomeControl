package de.maurice144.homecontrol.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.maurice144.homecontrol.R;

/**
 * Created by Maurice on 22.07.2015.
 */
public class ControlGroup {

    private JSONObject data;
    private ArrayList<ControlGroupItemBase> controls;

    protected ViewGroup viewGroup;
    private TextView titleTextView;

    public ControlGroup(JSONObject jsonObject, Context context) {
        this.data = jsonObject;
        Context context1 = context;
        this.controls = new ArrayList<ControlGroupItemBase>();

        JSONArray items = this.data.optJSONArray("Controls");
        JSONObject jsonObj;
        ControlGroupItemBase controlGroupItemBase;
        String objType;
        if(items != null && items.length() > 0) {
            for(int i=0;i<items.length();i++) {
                jsonObj = items.optJSONObject(i);
                if(jsonObj != null) {
                    objType = jsonObj.optString("ControlType","NULL");
                    controlGroupItemBase = null;

                    if(objType.equalsIgnoreCase(ControlGroupItem_Light.KEY_CONTROL_TYPE)) {
                        controlGroupItemBase = new ControlGroupItem_Light(context, jsonObj);
                    }
                    if(objType.equalsIgnoreCase(ControlGroupItem_Tv.KEY_CONTROL_TYPE)) {
                        controlGroupItemBase = new ControlGroupItem_Tv(jsonObj, context);
                    }
                    if(objType.equalsIgnoreCase(ControlGroupItem_UpDown.KEY_CONTROL_TYPE)) {
                        controlGroupItemBase = new ControlGroupItem_UpDown(jsonObj, context);
                    }
                    if(controlGroupItemBase == null) {
                        continue;
                    }
                    this.getControls().add(controlGroupItemBase);
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


    public ArrayList<ControlGroupItemBase> getControls() {
        return controls;
    }


    public ViewGroup getViewGroup(LayoutInflater inflater, ViewGroup parentView) {
        //if(viewGroup == null) {
            viewGroup = (ViewGroup)inflater.inflate(R.layout.control_group,parentView,false);
            for(ControlGroupItemBase control : controls) {
                viewGroup.addView(control.getViewGroup(inflater,viewGroup));
            }
            titleTextView = (TextView)viewGroup.findViewById(R.id.controlGroup_title);
        //}

        titleTextView.setText(getTitle());

        return viewGroup;
    }

}
