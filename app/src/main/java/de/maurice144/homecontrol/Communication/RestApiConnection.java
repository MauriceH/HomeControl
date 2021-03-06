package de.maurice144.homecontrol.Communication;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestApiConnection {

    private static final String CONST_HOSTNAME = "192.168.137.1:10701";
    private static final String CONST_SERVICEPATH = "" + CONST_HOSTNAME + "/HomeControl/";

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_BYTES = "binary/octet-stream";

    private String hostName;
    private int port;

    public RestApiConnection(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
    }


    public JSONObject sendPostData(String url, JSONObject pPostData) throws Exception {
        JSONObject ret = null;
        byte[] jsonByte = pPostData.toString().getBytes();

        String jsonString = getResultForPostMethod(url, jsonByte, CONTENT_TYPE_JSON);
        if (!jsonString.equals("")) {
            ret = new JSONObject(jsonString);
        }

        return ret;
    }

    public JSONObject callGet(String url) throws Exception {
        JSONObject ret = null;

        String jsonString = getResultForGetMethod(url, CONTENT_TYPE_JSON);
        if (!jsonString.equals("")) {
            ret = new JSONObject(jsonString);
        }

        return ret;
    }



    private String getResultForPostMethod(String pUrl, byte[] bytes, String contentType) throws Exception {
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection con = null;
        StringBuilder builder = new StringBuilder();

        String address = hostName + ":" + String.valueOf(port);

        try {
            con = initConnection(address,pUrl, contentType);
            con.setConnectTimeout(4000);
            con.setFixedLengthStreamingMode(bytes.length);
            con.setRequestMethod("POST");
            os = con.getOutputStream();
            os.write(bytes);
            os.flush();
            os.close();

            is = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line);

            is.close();

        } finally {
            if (os != null) os.close();
            if (is != null) is.close();

            if (con != null)
                con.disconnect();

        }

        return builder.toString();
    }

    private HttpURLConnection initConnection(String host, String url, String requestValue) throws Exception {

        String address;
        address = "http://" + host + "/HomeControl/";

        HttpURLConnection con;
        con = (HttpURLConnection) (new URL(address + url)).openConnection();
        con.setRequestProperty("CONTENT-TYPE", requestValue);

        return con;
    }


    private String getResultForGetMethod(String pUrl, String contentType) throws Exception {
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection con = null;
        StringBuilder builder = new StringBuilder();

        String address = hostName + ":" + String.valueOf(port);

        try {
            con = initConnection(address, pUrl, contentType);
            con.setConnectTimeout(4000);
            con.setRequestMethod("GET");
            is = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null)
                builder.append(line);

            is.close();

        } finally {
            if (os != null) os.close();
            if (is != null) is.close();

            if (con != null)
                con.disconnect();

        }

        return builder.toString();
    }



}
