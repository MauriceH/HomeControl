package de.maurice144.homecontrol.FrontEnd;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import de.maurice144.homecontrol.Data.ControlGroup;
import de.maurice144.homecontrol.Data.ControlGroupItemBase;
import de.maurice144.homecontrol.Data.ControlGroupItem_Light;
import de.maurice144.homecontrol.Data.ControlPage;
import de.maurice144.homecontrol.R;

/**
 * Created by Maurice on 21.07.2015.
 */
public class Control_Main_Fragment extends ControlBaseFragment {

    private JSONObject jsonData;
    private ControlPage page;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        String jsonString = args.getString("data");
        try {
            jsonData = new JSONObject(jsonString);
        } catch (Exception ex) {
            jsonData = null;
        }
        page = new ControlPage(jsonData,this.getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragement created",String.valueOf(page.getId()));
        View rootView = inflater.inflate(R.layout.fragment_controlpage, container, false);

        LinearLayout groupParent = (LinearLayout)rootView.findViewById(R.id.control_scrollContainer);

        LinearLayout groupView;

        for(ControlGroup group : this.page.getGroups()) {
            groupView = (LinearLayout)group.getViewGroup(inflater,groupParent);
            groupParent.addView(groupView);
        }

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Fragement destroyed",String.valueOf(page.getId()));
    }
}

