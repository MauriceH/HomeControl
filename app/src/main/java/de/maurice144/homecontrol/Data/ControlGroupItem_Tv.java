package de.maurice144.homecontrol.Data;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import de.maurice144.homecontrol.R;

/**
 * Created by Maurice on 22.07.2015.
 */
public class ControlGroupItem_Tv  extends ControlGroupItemBase {

    public static final String KEY_CONTROL_TYPE = "tv";

    private TextView title;
    private Button button;

    public ControlGroupItem_Tv(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    protected ViewGroup onCreateView(LayoutInflater inflater, ViewGroup parentView) {
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.control_item_tv,parentView,false);

        title = (TextView)layout.findViewById(R.id.control_title);
        title.setText(getTitle());

        button = (Button)layout.findViewById(R.id.control_switch);

        return layout;
    }

    @Override
    public void SetState(Bundle data) {

    }
}