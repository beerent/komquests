package com.komquests.api.rest;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class RestService {
    private String token;

    public RestService(String token) {
        this.token = token;
    }

    public String get(String targetUrl) {
        HttpURLConnection conn = buildHttpUrlConnection(targetUrl, "GET");
        if (!isValidHttpUrlConnection(conn)) {
            return null;
        }

        InputStreamReader inputStreamReader = getInputStreamReader(conn);
        if (!isValidInputStreamReader(inputStreamReader)) {
            return null;
        }

        String response = getResponseFromRequest(inputStreamReader);
        return response;
    }

    private HttpURLConnection buildHttpUrlConnection(String targetUrl, String request) {
        HttpURLConnection conn = null;

        try {
            URL url = new URL(targetUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(request);
            String authString = "Bearer " + token;
            conn.setRequestProperty("Authorization", authString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conn;
    }

    private String getResponseFromRequest(InputStreamReader inputStreamReader) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private boolean isValidHttpUrlConnection(HttpURLConnection conn) {
        return conn != null;
    }

    private boolean isValidInputStreamReader(InputStreamReader inputStreamReader) {
        return inputStreamReader != null;
    }

    private InputStreamReader getInputStreamReader(HttpURLConnection conn) {
        try {
            return new InputStreamReader(conn.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
