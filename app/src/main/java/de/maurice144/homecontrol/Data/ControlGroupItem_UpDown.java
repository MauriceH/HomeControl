package de.maurice144.homecontrol.Data;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import de.maurice144.homecontrol.Communication.Requests.ControlStateChangeRequest;
import de.maurice144.homecontrol.Communication.Results.DefaultResult;
import de.maurice144.homecontrol.Communication.WebApi;
import de.maurice144.homecontrol.R;

/**
 * Created by Maurice on 28.07.2015.
 */
public class ControlGroupItem_UpDown extends ControlGroupItemBase {

    public static final String KEY_CONTROL_TYPE = "updown";

    private TextView title;
    private ImageButton buttonDown;
    private ImageButton buttonUp;
    private Button buttonStop;


    public ControlGroupItem_UpDown(JSONObject jsonObject, Activity activity) {
        super(activity, jsonObject);
    }

    @Override
    protected ViewGroup onCreateView(LayoutInflater inflater, ViewGroup parentView) {
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.control_item_updown,parentView,false);

        title = (TextView)layout.findViewById(R.id.control_title);
        title.setText(getTitle());

        buttonDown = (ImageButton)layout.findViewById(R.id.control_down);
        buttonUp = (ImageButton)layout.findViewById(R.id.control_up);
        buttonStop = (Button)layout.findViewById(R.id.control_stop);

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int newState = 2;
                if(ControlGroupItem_UpDown.this.getState() != newState ) {
                    ControlGroupItem_UpDown.this.SaveNewState(newState);
                    final LocalSettings settings = new LocalSettings(activity);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ControlStateChangeRequest request = new ControlStateChangeRequest(settings.getDeviceToken(), ControlGroupItem_UpDown.this.getId(), newState);
                                DefaultResult defaultResult = new WebApi(activity).SendControlChange(request);
                                if(defaultResult == null || !defaultResult.isDoneCorrect()) {
                                    displayStateChangeErrorToast(defaultResult);
                                    return;
                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SetState(newState);
                                    }
                                });
                            }catch (Exception ex) {
                                displayStateChangeErrorToast(null);
                                Log.e("err", ex.getMessage(), ex);
                            }
                        }
                    }).start();

                }
            }
        });

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int newState = 1;
                if(ControlGroupItem_UpDown.this.getState() != newState ) {
                    ControlGroupItem_UpDown.this.SaveNewState(newState);
                    final LocalSettings settings = new LocalSettings(activity);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ControlStateChangeRequest request = new ControlStateChangeRequest(settings.getDeviceToken(), ControlGroupItem_UpDown.this.getId(), newState);
                                DefaultResult defaultResult = new WebApi(activity).SendControlChange(request);
                                if(defaultResult == null || !defaultResult.isDoneCorrect()) {
                                    displayStateChangeErrorToast(defaultResult);
                                    return;
                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SetState(newState);
                                    }
                                });
                            }catch (Exception ex) {
                                displayStateChangeErrorToast(null);
                                Log.e("err", ex.getMessage(), ex);
                            }
                        }
                    }).start();

                }
            }
        });


        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int newState = 0;
                if(ControlGroupItem_UpDown.this.getState() != newState ) {
                    ControlGroupItem_UpDown.this.SaveNewState(newState);
                    final LocalSettings settings = new LocalSettings(activity);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ControlStateChangeRequest request = new ControlStateChangeRequest(settings.getDeviceToken(), ControlGroupItem_UpDown.this.getId(), newState);
                                DefaultResult defaultResult = new WebApi(activity).SendControlChange(request);
                                if(defaultResult == null || !defaultResult.isDoneCorrect()) {
                                    displayStateChangeErrorToast(defaultResult);
                                    return;
                                }
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SetState(newState);
                                    }
                                });
                            }catch (Exception ex) {
                                displayStateChangeErrorToast(null);
                                Log.e("err", ex.getMessage(), ex);
                            }
                        }
                    }).start();

                }
            }
        });



        return layout;
    }

    @Override
    public void SetState(int state) {
        SaveNewState(state);
        switch (state) {
            case 0:
                buttonStop.setSelected(true);
                buttonUp.setSelected(false);
                buttonDown.setSelected(false);
                break;
            case 1:
                buttonStop.setSelected(false);
                buttonUp.setSelected(true);
                buttonDown.setSelected(false);
                break;
            case 2:
                buttonStop.setSelected(false);
                buttonUp.setSelected(false);
                buttonDown.setSelected(true);
                break;
        }

    }
}