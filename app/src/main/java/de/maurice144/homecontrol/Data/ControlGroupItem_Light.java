package de.maurice144.homecontrol.Data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONObject;

import de.maurice144.homecontrol.R;

/**
 * Created by Maurice on 22.07.2015.
 */
public class ControlGroupItem_Light extends ControlGroupItemBase {

    public static final String KEY_CONTROL_TYPE = "light";

    private TextView title;
    private Switch switchOnOff;



    public ControlGroupItem_Light(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    protected ViewGroup onCreateView(LayoutInflater inflater, ViewGroup parentView) {
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.control_item_light,parentView,false);

        title = (TextView)layout.findViewById(R.id.control_title);
        title.setText(getTitle());

        switchOnOff = (Switch)layout.findViewById(R.id.control_switch);

        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ControlGroupItem_Light.this.SaveNewState(isChecked ? 1 : 0);
            }
        });

        return layout;
    }

    @Override
    public void SetState(Bundle data) {
        String newState = data.getString("state","off");
        SaveNewState(newState.equalsIgnoreCase("on") ? 1 : 0);
        switchOnOff.setChecked(getState() == 1);
    }
}
