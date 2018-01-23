package com.mawaqaa.eatandrun.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by HP on 7/30/2017.
 */

public class syncJsonClass {

    public static JSONArray getJSONDataArray(String urlString) throws IOException, JSONException {

        try {
            HttpURLConnection urlConnection = null;

            URL url = new URL(urlString.trim());

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);

            urlConnection.setDoOutput(true);

            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            char[] buffer = new char[1024];

            String jsonString = new String();

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            jsonString = sb.toString();
            //jsonString="{\"data\":"+jsonString+"}";
            System.out.println("JSON: " + jsonString);
            JSONArray jArray= new JSONArray(jsonString);
            return jArray;//new JSONArray(jsonString);
        }
        catch (Exception xx)
        {return  null;}
    }

    public static JSONObject getJSONDataObject(String urlString) throws IOException, JSONException {

        try {
            HttpURLConnection urlConnection = null;

            URL url = new URL(urlString.trim());

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            urlConnection.setDoOutput(true);

            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            char[] buffer = new char[1024];

            String jsonString = new String();

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            jsonString = sb.toString();
            //jsonString="{\"data\":"+jsonString+"}";
            System.out.println("JSON: " + jsonString);
            JSONObject jArray= new JSONObject(jsonString);
            return jArray;//new JSONArray(jsonString);
        }
        catch (Exception xx)
        {return  null;}
    }

    public static String getJSONDataAsString(String urlString) throws IOException, JSONException {

        try {
            HttpURLConnection urlConnection = null;

            URL url = new URL(urlString.trim());

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            urlConnection.setDoOutput(true);

            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            char[] buffer = new char[1024];

            String jsonString = new String();

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            jsonString = sb.toString();
            //jsonString="{\"data\":"+jsonString+"}";
            //System.out.println("JSON: " + jsonString);
            //JSONObject jArray= new JSONObject(jsonString);
            return jsonString.replace('\"',' ');//new JSONArray(jsonString);
        }
        catch (Exception xx)
        {return  null;}
    }
}
