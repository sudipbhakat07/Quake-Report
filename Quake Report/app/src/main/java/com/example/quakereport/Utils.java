package com.example.quakereport;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Utils {

    private static URL createUrl(String url) {
        URL urlTemp = null;
        try {
            urlTemp = new URL(url);
        }
        catch(MalformedURLException e) {
            Log.e("URL : ","Error in creating url!");
        }
        return urlTemp;
    }

    public static ArrayList<Report> fetchEarthquakeData(String urlString) {
        URL url = createUrl(urlString);
        Log.v("urlString---->", urlString);
        if(url != null) {
            Log.v("url---->", "is null" + url);

        }
        String jsonString = null;
        try {
            jsonString = makeHttpRequest(url);
        }
        catch (IOException e) {
            Log.e("Error : ", "Problem making the HTTP request.", e);
        }
        ArrayList<Report> earthquakes = extract(jsonString);
        return earthquakes;
    }

    private static String makeHttpRequest(URL url) throws  IOException{
        HttpURLConnection httpURLConnection = null ;
        InputStream inputStream = null;
        String jsonString = "";
        if(url == null)
            return  jsonString;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setRequestMethod("GET");

            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonString = readFromStream(inputStream);
            }
            else {
                Log.e("Error : ", "Response code = " + httpURLConnection.getResponseCode());
            }
        }
        catch (IOException e) {
            Log.e("IOException : ", "Error making HTTP request" + e );
        }
        finally {
            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if(inputStream != null) {
                inputStream.close();
            }
        }
        return  jsonString;
    }

    private static String readFromStream(InputStream input) throws  IOException{
        StringBuilder output = new StringBuilder();
        if(input != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = br.readLine();
            while(line != null) {
                output.append(line);
                line = br.readLine();
            }
        }
        return output.toString();
    }




    public static ArrayList<Report> extract(String urlString) {
        if(TextUtils.isEmpty(urlString)) {
            return  null;
        }

        ArrayList<Report> earthquakes = new ArrayList<>();

        try {
//            JSONObject rootJSON = new JSONObject(new Utils().makeHttpRequest(urlString));
            JSONObject rootJSON = new JSONObject(urlString);
            JSONArray featuresArray = rootJSON.optJSONArray("features");

            for(int i=0; i<featuresArray.length(); i++) {
                JSONObject featuresObj = featuresArray.getJSONObject(i);
                JSONObject properties = featuresObj.getJSONObject("properties");
                Double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                Long time = properties.getLong("time");
                String url = properties.getString("url");

                earthquakes.add(new Report(mag,place,time,url));

            }
        }
        catch(JSONException e) {
            Log.e("Earthquakes","Problem parsing the earthquake JSON results.",e);
        }

        return earthquakes;
    }


}
