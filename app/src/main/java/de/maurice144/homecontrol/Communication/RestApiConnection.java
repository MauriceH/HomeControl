package de.maurice144.homecontrol.Communication;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by mhessing on 26.07.2015.
 */
public class RestApiConnection {

    private static final String CONST_HOSTNAME = "familiehessing.de";
    private static final String CONST_SERVICEPATH = "http://" + CONST_HOSTNAME + "/homecontrol/";

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_BYTES = "binary/octet-stream";


    /**
     * Initialisierung der Dienstverbindung auf HTTPS-Basis
     */
    private HttpURLConnection initConnection(String pUrl, String requestValue) throws Exception {
        HttpURLConnection con;

        SSLContext sslContext = SSLContext.getInstance("TLS");

        TrustManager[] tm = new TrustManager[]{new ServiceTrustManager()};
        sslContext.init(null, tm, new SecureRandom());


        con = (HttpURLConnection) (new URL(CONST_SERVICEPATH + pUrl)).openConnection();
        /*con.setSSLSocketFactory(sslContext.getSocketFactory());
        con.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hostnameVerifier =
                        HttpsURLConnection.getDefaultHostnameVerifier();
                return hostnameVerifier.verify(CONST_HOSTNAME, session);
            }
        });*/


        con.setRequestProperty("CONTENT-TYPE", requestValue);
        con.setRequestMethod("POST");


        /** Ermöglicht die beste Perfomance der Verbindung, ohne die übertragungslänge zu wissen */
        //con.setChunkedStreamingMode(0);


        return con;
    }



    /**
     * Post-JSON/JSONArray with Result
     */
    public JSONObject sendPostData(String url, JSONObject pPostData) throws Exception {
        JSONObject ret = null;
        byte[] jsonByte = pPostData.toString().getBytes();

        String jsonString = getResultForPostMethod(url, jsonByte, CONTENT_TYPE_JSON);
        if (jsonString != null && !jsonString.equals(""))
            ret = new JSONObject(jsonString);

        return ret;
    }


    private String getResultForPostMethod(String pUrl, byte[] bytes, String contentType) throws Exception {
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection con = null;
        StringBuilder builder = new StringBuilder();

        /** Initialisierung der Dienstverbindung */
        try {
            con = initConnection(pUrl, contentType);

            con.setFixedLengthStreamingMode(bytes.length);
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

        } catch (Exception ex) {
            Log.e("RestCon", ex.getMessage(), ex);
            throw ex;
        } finally {
            if (os != null) os.close();
            if (is != null) is.close();

            if (con != null)
                con.disconnect();

        }

        return builder.toString();
    }


    /**
     * SSL-TrustManager
     */
    private class ServiceTrustManager implements X509TrustManager {

        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
