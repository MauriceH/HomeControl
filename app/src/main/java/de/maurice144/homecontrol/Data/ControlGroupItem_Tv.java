package de.maurice144.homecontrol.Data;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONObject;

import de.maurice144.homecontrol.Communication.Requests.ControlStateChangeRequest;
import de.maurice144.homecontrol.Communication.WebApi;
import de.maurice144.homecontrol.R;

/**
 * Created by Maurice on 22.07.2015.
 */
public class ControlGroupItem_Tv  extends ControlGroupItemBase {

    public static final String KEY_CONTROL_TYPE = "tv";

    private TextView title;
    private Switch switchOnOff;

    private CompoundButton.OnCheckedChangeListener listener;

    public ControlGroupItem_Tv(JSONObject jsonObject, Activity activity) {
        super(activity, jsonObject);
    }

    @Override
    protected ViewGroup onCreateView(LayoutInflater inflater, ViewGroup parentView) {
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.control_item_tv,parentView,false);

        title = (TextView)layout.findViewById(R.id.control_title);
        title.setText(getTitle());

        switchOnOff = (Switch)layout.findViewById(R.id.control_switch);

        listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final int newState = isChecked ? 1 : 0;
                if (ControlGroupItem_Tv.this.getState() != newState) {
                    ControlGroupItem_Tv.this.SaveNewState(newState);
                    final LocalSettings settings = new LocalSettings(activity);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                new WebApi(activity).SendControlChange(new ControlStateChangeRequest(settings.getDeviceToken(), ControlGroupItem_Tv.this.getId(), newState));
                            } catch (Exception ex) {
                                Log.e("err", ex.getMessage(), ex);
                            }
                        }
                    }).start();

                }


            }
        };

        switchOnOff.setOnCheckedChangeListener(listener);

        return layout;
    }

    @Override
    public void SetState(int state) {
        SaveNewState(state);
        switchOnOff.setOnCheckedChangeListener(null);
        switchOnOff.setChecked(getState() == 1);
        switchOnOff.setOnCheckedChangeListener(listener);
    }
}