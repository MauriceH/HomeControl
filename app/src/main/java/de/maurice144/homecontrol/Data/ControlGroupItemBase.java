package de.maurice144.homecontrol.Data;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

/**
 * Created by Maurice on 22.07.2015.
 */
public abstract class ControlGroupItemBase {

    private JSONObject data;
    private Drawable icon;

    protected ViewGroup viewGroup;


    public ControlGroupItemBase(JSONObject jsonObject,Drawable icon) {
        this.data = jsonObject;
        this.icon = icon;
    }

    public long getId() {
        return data.optLong("id", 0);
    }

    public String getTitle() {
        return data.optString("title", "No Title");
    }




    public ViewGroup getViewGroup(LayoutInflater inflater, ViewGroup parentView) {
        //if(viewGroup == null) {
            viewGroup = onCreateView(inflater,parentView);
        //}
        return viewGroup;
    }


    protected abstract ViewGroup onCreateView(LayoutInflater inflater, ViewGroup parentView);

    public Drawable getIcon() {
        return icon;
    }


}
