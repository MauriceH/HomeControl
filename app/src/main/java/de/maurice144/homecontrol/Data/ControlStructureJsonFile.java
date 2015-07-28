package de.maurice144.homecontrol.Data;

import android.os.Environment;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.PriorityQueue;

/**
 * Created by Maurice on 28.07.2015.
 */
public class ControlStructureJsonFile {

    private static final String TAG = "ControlJsonFile";
    private static final String FILE_NAME = "controlStructure.json";


    private JSONObject data;


    private ControlStructureJsonFile(JSONObject data) {
        this.data = data;
    }


    public static ControlStructureJsonFile CreateByJsonString(String json) throws org.json.JSONException {
       return new ControlStructureJsonFile(new JSONObject(json));
    }

    public static ControlStructureJsonFile CreateByJsonObject(JSONObject jsonObj) throws org.json.JSONException {
        return new ControlStructureJsonFile(jsonObj);
    }

    public static ControlStructureJsonFile LoadFile() throws org.json.JSONException,java.io.IOException {

        File jsonFile = new File(dataDir(),FILE_NAME);

        if (!jsonFile.exists()) {
            return null;
        }

        FileInputStream stream = new FileInputStream(jsonFile);
        String jsonStr = null;
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            jsonStr = Charset.defaultCharset().decode(bb).toString();
            return new ControlStructureJsonFile(new JSONObject(jsonStr));
        }
        finally {
            stream.close();
        }
    }


    public JSONObject getObj() {
        return this.data;
    }

    public void SaveFile() throws org.json.JSONException,java.io.IOException {
        File jsonFile = new File(dataDir(),FILE_NAME);
        FileWriter fw = new FileWriter(jsonFile);
        try {
            fw.write(this.data.toString());
        } finally {
            fw.flush();
            fw.close();
        }
    }


    private static File dataDir()
    {
        File sdcard = Environment.getExternalStorageDirectory();
        if( sdcard == null || !sdcard.isDirectory() ) {
            // TODO: warning popup
            Log.w(TAG, "Storage card not found " + sdcard);
            return null;
        }
        File datadir = new File(sdcard, "MyApplication");
        if( !confirmDir(datadir) ) {
            // TODO: warning popup
            Log.w(TAG, "Unable to create " + datadir);
            return null;
        }
        return datadir;
    }

    private static final boolean confirmDir(File dir) {
        if( dir.isDirectory() ) return true;
        if( dir.exists() ) return false;
        return dir.mkdirs();
    }


}
