package de.maurice144.homecontrol.FrontEnd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.maurice144.homecontrol.R;

/**
 * Created by Maurice on 21.07.2015.
 */
public class Control_DG_Fragment extends ControlBaseFragment {

    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_control_dg, container, false);
        return rootView;
    }

}

