package de.maurice144.homecontrol.Data;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import de.maurice144.homecontrol.Communication.Results.DefaultResult;

/**
 * Created by Maurice on 22.07.2015.
 */
public abstract class ControlGroupItemBase {


    protected final Activity activity;
    private JSONObject data;

    protected ViewGroup viewGroup;

    private int state = 0;



    public ControlGroupItemBase(Activity activity, JSONObject jsonObject) {
        this.data = jsonObject;
        this.activity = activity;
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
        SetState(state);
        return viewGroup;
    }


    protected abstract ViewGroup onCreateView(LayoutInflater inflater, ViewGroup parentView);

    public abstract void SetState(int state);

    protected void SaveNewState(int newState) {
        state = newState;
    }

    protected int getState() {
        return state;
    }


    protected void displayStateChangeErrorToast(final DefaultResult defaultResult) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message;
                if (defaultResult == null) {
                    message = "Die Verbindung konnte nicht aufgebaut werden.";
                } else {
                    message = String.format("Veränderung nicht möglich! Fehlercode: %d", defaultResult.intErrorCode());
                }
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });

    }

}
