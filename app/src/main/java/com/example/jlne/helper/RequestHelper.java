package com.example.jlne.helper;

import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestHelper {
    private String TAG = "JLNE_Request_Helper";
    private StringBuilder responseBuilder = new StringBuilder();

    public String sendPostRequest(String link, HashMap<String, String> parameters) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(processParameters(parameters));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                for (String line; (line = bufferedReader.readLine()) != null; ) {
                    responseBuilder.append(line);
                }

            }
        } catch (Exception e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }

        return responseBuilder.toString();
    }
    
    public String sendGetRequest(String link) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            for (String line; (line = bufferedReader.readLine()) != null; ) {
                stringBuilder.append(line);
            }

        } catch (Exception e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        }

        return stringBuilder.toString();
    }

    private String processParameters(HashMap<String, String> parameters) throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> parameter : parameters.entrySet()) {
            if (first) {
                first = false;
            }
            else {
                stringBuilder.append("&");
            }

            stringBuilder.append(URLEncoder.encode(parameter.getKey(), "UTF-8"));
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(parameter.getValue(), "UTF-8"));
        }

        return stringBuilder.toString();
    }
}