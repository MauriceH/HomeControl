package de.maurice144.homecontrol.Data;

import android.app.Application;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Switch button;

    public ControlGroupItem_Light(JSONObject jsonObject,Context context) {
        super(jsonObject, ContextCompat.getDrawable(context, R.drawable.bulb));
    }

    @Override
    protected ViewGroup onCreateView(LayoutInflater inflater, ViewGroup parentView) {
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.control_item_light,parentView,false);

        title = (TextView)layout.findViewById(R.id.control_title);
        title.setText(getTitle());

        button = (Switch)layout.findViewById(R.id.control_switch);

        return layout;
    }
}
