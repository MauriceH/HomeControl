package de.maurice144.homecontrol.FrontEnd.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import de.maurice144.homecontrol.Communication.Requests.ControlStateChangeRequest;
import de.maurice144.homecontrol.Communication.WebApi;
import de.maurice144.homecontrol.Data.LocalSettings;
import de.maurice144.homecontrol.R;

public class DimmerActivity extends ActionBarActivity {

    private LocalSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = new LocalSettings(this);

        setContentView(R.layout.activity_control_dimmer);

        LinearLayout linearLayout;
        linearLayout = (LinearLayout)findViewById(R.id.control_dimmer_layout_10to50);
        createButtons(linearLayout,10,50);
        linearLayout = (LinearLayout)findViewById(R.id.control_dimmer_layout_60to100);
        createButtons(linearLayout,60,100);

    }

    private void createButtons(LinearLayout linearLayout,int start, int end) {
        Button button;
        for(int i=start; i<=end; i+=10) {
            button = new Button(this);
            button.setText(String.valueOf(i) + "%");
            button.setGravity(Gravity.CENTER);
            button.setTag(String.valueOf(i));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = (String)v.getTag();
                    setControlState(111, tag);
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(160, 160);
            params.setMargins(15,15,15,15);
            linearLayout.addView(button, params);
        }
    }

    private void setControlState(final long controlId, String tag) {
        AsyncTask<String,String,String> task = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {

                ControlStateChangeRequest request;
                request = new ControlStateChangeRequest(settings.getDeviceToken(),controlId,Integer.valueOf(params[0]));
                try {
                    new WebApi(DimmerActivity.this).SendControlChange(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.execute(tag);
    }


    public void turnOn(View view) {
        setControlState(110,"1");
    }

    public void turnOff(View view) {
        setControlState(110,"0");
    }
}
