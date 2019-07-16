package com.example.bebo.newsfeed_app;

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
import java.util.List;

class NetworkUtils {
    private static final String RESPONSE = "response";
    private static final String RESULTS = "results";

    public List<MainList> fetchDataFromURL(String url) {
        List<MainList> reportList;
        String jsonString = null;
        if (url == null || url.isEmpty()) {
            return null;
        }
        URL urlObject = makeURLObject(url);
        try {
            jsonString = makeHTTPConnect(urlObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        reportList = extraReportsFromJson(jsonString);
        return reportList;

    }

    private String extraFromIndustrial(InputStream inputStream) {
        StringBuilder builderJson = new StringBuilder();
        if (inputStream == null) {
            return null;
        }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                builderJson.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builderJson.toString();
    }

    private String makeHTTPConnect(URL url) throws IOException {
        String jsonString = null;
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        if (url == null) {
            return null;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(20000);
            urlConnection.setConnectTimeout(25000);
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonString = extraFromIndustrial(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jsonString;
    }

    private URL makeURLObject(String url) {
        URL urlObject = null;
        if (url == null) {
            return null;
        }
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return urlObject;
    }

    private List<MainList> extraReportsFromJson(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }

        List<MainList> reportList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONObject response = root.getJSONObject(RESPONSE);
            JSONArray resultArray = response.getJSONArray(RESULTS);
            String author;
            for (int i = 0; i < resultArray.length(); i++) {

                JSONObject reportObject = resultArray.getJSONObject(i);
                JSONArray array = reportObject.getJSONArray("tags");
                if (array.length() > 0) {
                    JSONObject arrayJSONObject = array.getJSONObject(0);
                    author = arrayJSONObject.optString("webTitle");
                } else {
                    author = null;
                }
                String title = reportObject.optString("webTitle");
                String section = reportObject.optString("sectionName");
                String webURL = reportObject.optString("webUrl");
                String webPublicationDate = reportObject.optString("webPublicationDate");
                MainList report = new MainList(title, section, author, webURL , webPublicationDate);
                reportList.add(report);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reportList;
    }

}

