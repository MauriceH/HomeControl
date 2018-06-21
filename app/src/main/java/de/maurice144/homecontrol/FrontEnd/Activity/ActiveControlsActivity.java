package de.maurice144.homecontrol.FrontEnd.Activity;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.maurice144.homecontrol.Adapter.ActiveControlsAdapter;
import de.maurice144.homecontrol.Data.ActiveControl;
import de.maurice144.homecontrol.R;


public class ActiveControlsActivity extends Activity {


    private ListView lv;
    private ActiveControlsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_controls);

        lv = (ListView) findViewById(R.id.active_controls_list);

        adapter = new ActiveControlsAdapter(this);
        lv.setAdapter(adapter);

        AsyncTask<String,String,List<ActiveControl>> task = new AsyncTask<String, String, List<ActiveControl>>() {
            @Override
            protected List<ActiveControl> doInBackground(String... params) {
                try {
                    JSONObject jsonObjectFromURL = getJSONObjectFromURL("http://vs1.validdata.de:11302/commandtransmitter.php?ident=HomeCont");
                    JSONArray controls = jsonObjectFromURL.getJSONArray("Controls");
                    ArrayList<ActiveControl> activeControls = new ArrayList<>();
                    for (int i = 0; i < controls.length() ; i++) {
                        ActiveControl control = new ActiveControl();
                        JSONObject jsonObject = controls.getJSONObject(i);
                        control.setTitle(jsonObject.getString("Title"));
                        control.setControlType(jsonObject.getString("ControlType"));
                        control.setState(jsonObject.getInt("State"));
                        control.setId(jsonObject.getInt("Id"));
                        activeControls.add(control);
                    }
                    return activeControls;
                } catch (Exception e) {
                    return new ArrayList<ActiveControl>();
                }
            }

            @Override
            protected void onPostExecute(final List<ActiveControl> activeControls) {
                super.onPostExecute(activeControls);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setActiveControls(activeControls);
                    }
                });
            }
        };
        task.execute("");

    }

    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));

        char[] buffer = new char[1024];

        String jsonString = new String();

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();

        jsonString = sb.toString();

        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }
}
