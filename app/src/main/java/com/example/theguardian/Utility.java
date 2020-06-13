package com.example.theguardian;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class containing static helper methods that help with code-reuse
 * @author Emrah Kinay
 */
public class Utility {

    /**
     * Converts the contents of an URL to a JSONObject.
     * @param url URL that will turned into a JSONObject
     * @return JSONObject holding the contents of the URL
     */
    public static JSONObject buildJSONObject(String url){
        JSONObject jObject = null;
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(openUrl(url), "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            String result = sb.toString();
            jObject = new JSONObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jObject;
    }

    /**
     * Opens an input stream from a specified URL.
     * @param url The URL you wish to access
     * @return InputStream from the URL
     */
    private static InputStream openUrl(String url){
        InputStream response = null;
        try {
            URL urlObj = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
            response = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}