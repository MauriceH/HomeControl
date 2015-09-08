package de.maurice144.homecontrol.FrontEnd.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import de.maurice144.homecontrol.Data.LocalSettings;
import de.maurice144.homecontrol.R;

/**
 * Created by Maurice on 08.09.2015.
 */
public class ServerSettingActivity extends Activity {


    ScrollView scrollView;
    ProgressBar progressBar;
    EditText remoteServer;
    EditText localServer;
    EditText serverPort;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting_server);

        Button searchInWlan = (Button)findViewById(R.id.settings_server_search);
        scrollView = (ScrollView)findViewById(R.id.server_settings_form);
        progressBar = (ProgressBar)findViewById(R.id.server_settings_progress);

        remoteServer = (EditText)findViewById(R.id.server_hostname_remote);
        localServer = (EditText)findViewById(R.id.server_hostname_local);
        serverPort = (EditText)findViewById(R.id.server_port);

        setDataOfSettings();


        searchInWlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchForServer();
            }
        });
    }

    private void startSearchForServer() {

        scrollView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        AsyncTask<String,String,String> task = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                DatagramSocket socket = null;
                DatagramPacket packet = null;
                byte[] buf = new byte[1024];

                try {
                    socket = new DatagramSocket(10710);
                    packet = new DatagramPacket(buf, buf.length);
                    socket.setSoTimeout(10000);
                    socket.receive(packet);
                    String data;
                    data = convert(packet.getData(),packet.getLength());

                    return data;
                }catch (SocketTimeoutException e){Log.e("receive","time out");}
                catch (IOException e) {Log.e("receive","error 1");
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                serverAppBeaconObtained(s);
                super.onPostExecute(s);
            }
        };
        task.execute("");


    }

    String convert(byte[] data,int length) {
        StringBuilder sb = new StringBuilder(data.length);
        for (int i = 0; i < length; ++ i) {
            if (data[i] < 0) throw new IllegalArgumentException();
            sb.append((char) data[i]);
        }
        return sb.toString();
    }

    private void serverAppBeaconObtained(String data) {

        scrollView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);



        if(data == null) {
            return;
        }

        try {
            String[] separated = data.split(";");
            LocalSettings settings = new LocalSettings(this);
            settings.setServerConfiguration(separated[1],separated[0], Integer.parseInt(separated[2]));
            settings.Save();
            setDataOfSettings();
        } catch (Exception ex) {
            Log.e("Rec",ex.getMessage());
        }



        Log.e("receive",data);
    }

    private void setDataOfSettings() {
        LocalSettings settings = new LocalSettings(this);
        localServer.setText(settings.getServerHostNameLocal());
        remoteServer.setText(settings.getServerHostNameRemote());
        serverPort.setText(String.valueOf(settings.getServerPort()));

    }

}