package de.maurice144.homecontrol.Data;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.gcm.Task;

import org.json.JSONObject;

/**
 * Created by Maurice on 22.07.2015.
 */
public abstract class ControlGroupItemBase {

    private JSONObject data;

    protected ViewGroup viewGroup;

    private int state = 0;



    public ControlGroupItemBase(JSONObject jsonObject) {
        this.data = jsonObject;
    }

    public long getId() {
        return data.optLong("Id", 0);
    }

    public String getTitle() {
        return data.optString("Title", "No Title");
    }




    public ViewGroup getViewGroup(LayoutInflater inflater, ViewGroup parentView) {
        //if(viewGroup == null) {
            viewGroup = onCreateView(inflater,parentView);
        //}
        return viewGroup;
    }


    protected abstract ViewGroup onCreateView(LayoutInflater inflater, ViewGroup parentView);

    public abstract void SetState(Bundle data);

    protected void SaveNewState(int newState) {
        state = newState;
    }

    protected int getState() {
        return state;
    }

}
