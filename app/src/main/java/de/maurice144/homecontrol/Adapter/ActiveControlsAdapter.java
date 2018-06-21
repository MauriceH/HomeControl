package de.maurice144.homecontrol.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import de.maurice144.homecontrol.Data.ActiveControl;
import de.maurice144.homecontrol.R;

public class ActiveControlsAdapter extends BaseAdapter {

    private List<ActiveControl> activeControls;

    private final LayoutInflater layoutInflater;

    public ActiveControlsAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return activeControls == null ? 0 : activeControls.size();
    }

    @Override
    public Object getItem(int position) {
        return activeControls == null ? null : activeControls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ActiveControl control = activeControls.get(position);

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.active_control_row,parent,false);
        }

        TextView tv1 = (TextView) convertView.findViewById(R.id.active_control_row_text1);
        TextView tv2 = (TextView) convertView.findViewById(R.id.active_control_row_text2);

        tv1.setText(control.getTitle());
        tv2.setText(String.format(Locale.ROOT,"(%d)",control.getId()));

        return convertView;
    }

    public void setActiveControls(List<ActiveControl> activeControls) {
        this.activeControls = activeControls;
        notifyDataSetChanged();
    }
}
