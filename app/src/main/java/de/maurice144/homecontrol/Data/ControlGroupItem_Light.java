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

    public static final String KEY_CONTROL_TYPE = "LIGHT";

    private TextView title;
    private Switch switchOnOff;

    private boolean state = false;

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
                state = isChecked;
            }
        });

        return layout;
    }

    @Override
    public void SetState(Bundle data) {
        String newState = data.getString("state","off");
        switchOnOff.setChecked(newState.equalsIgnoreCase("on"));
    }
}
