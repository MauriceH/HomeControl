package de.maurice144.homecontrol.FrontEnd.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import de.maurice144.homecontrol.Communication.WebApi;
import de.maurice144.homecontrol.Data.LocalSettings;
import de.maurice144.homecontrol.R;

/**
 * Created by Maurice on 08.09.2015.
 */
public class ServerSettingActivity extends Activity {


    ScrollView scrollView;
    RelativeLayout progressBar;
    EditText remoteServer;
    EditText localServer;
    EditText serverPort;
    TextView countdownText;
    Button testConnectionButton;

    private CountDownTimer countDownTimer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting_server);



        Button searchInWlan = (Button)findViewById(R.id.settings_server_search);
        scrollView = (ScrollView)findViewById(R.id.server_settings_form);
        progressBar = (RelativeLayout)findViewById(R.id.server_settings_progress_layout);

        remoteServer = (EditText)findViewById(R.id.server_hostname_remote);
        localServer = (EditText)findViewById(R.id.server_hostname_local);
        serverPort = (EditText)findViewById(R.id.server_port);

        countdownText = (TextView)findViewById(R.id.server_settings_progress_text);

        testConnectionButton = (Button)findViewById(R.id.settings_server_connect);


        progressBar.setVisibility(View.GONE);

        setDataOfSettings();


        searchInWlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearchForServer();
            }
        });

        testConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testConnectionAndSaveSetting();
            }
        });

    }

    private void startSearchForServer() {


        View view = localServer;
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        scrollView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        if(countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(10 * 1000 + 800,100 ) {


            public void onTick(long millisUntilFinished) {
                countdownText.setText(String.valueOf(millisUntilFinished / 1000));
            }
            public void onFinish() {
                countdownText.setText("0");
            }
        }.start();


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
                    data = convertByteToString(packet.getData(), packet.getLength());

                    return data;
                }catch (SocketTimeoutException e){Log.e("receive","time out" + e.getMessage());}
                catch (IOException e) {Log.e("receive","error 1");
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
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

    String convertByteToString(byte[] data, int length) {
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

        if(countDownTimer != null) {
            countDownTimer.cancel();
        }

        if(data != null) {
            try {
                String[] separated = data.split(";");

                localServer.setText(separated[1]);
                remoteServer.setText(separated[0]);
                serverPort.setText(String.valueOf(separated[2]));

                new AlertDialog.Builder(this)
                        .setMessage("Ihr HomeControl Server wurde gefunden. Prüfen Sie nun die Verbindung")
                        .setTitle("Serversuche")
                        .setNegativeButton("OK",null).show();

                return;
            } catch (Exception ex) {
                Log.e("Rec",ex.getMessage());
            }
        }

        new AlertDialog.Builder(this)
                .setMessage("Server nicht gefunden")
                .setTitle("Serversuche")
                .setNegativeButton("OK",null).show();

    }

    private void testConnectionAndSaveSetting() {
        final String hostNameRemote;
        final String hostNameLocal;
        final int port;

        hostNameLocal = localServer.getText().toString();
        hostNameRemote = remoteServer.getText().toString();
        port = Integer.parseInt(serverPort.getText().toString());

        AsyncTask<Integer,Integer,Boolean> task = new AsyncTask<Integer, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Integer... ints) {
                return new WebApi(ServerSettingActivity.this,hostNameLocal,port).CheckConnection();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                if(aBoolean) {

                    LocalSettings localSettings = new LocalSettings(ServerSettingActivity.this);

                    localSettings.setServerConfiguration(hostNameLocal,hostNameRemote,port);

                    new AlertDialog.Builder(ServerSettingActivity.this)
                            .setMessage("Verbindung erfolgreich")
                            .setTitle("Server")
                            .setNegativeButton("OK",null).show();
                } else {
                    new AlertDialog.Builder(ServerSettingActivity.this)
                            .setMessage("Verbindung nicht erfolgreich")
                            .setTitle("Server")
                            .setNegativeButton("OK",null).show();
                }


            }
        };
        task.execute(0);
    }


    private void setDataOfSettings() {
        LocalSettings settings = new LocalSettings(this);
        localServer.setText(settings.getServerHostNameLocal());
        remoteServer.setText(settings.getServerHostNameRemote());
        serverPort.setText(String.valueOf(settings.getServerPort()));

    }


}
