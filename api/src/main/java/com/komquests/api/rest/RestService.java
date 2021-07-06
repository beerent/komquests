package com.komquests.api.rest;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestService {
    private String token;
    private AuthenticationType authenticationType;

    public RestService() {
        this.token = null;
        this.authenticationType = AuthenticationType.INVALID;
    }

    public RestService(String token, AuthenticationType authenticationType) {
        this.token = token;
        this.authenticationType = authenticationType;
    }

    public String get(String targetUrl) {
        return get(targetUrl, new HashMap<>());
    }

    public String get(String targetUrl, Map<String, String> queryParams) {
        if (isQueryAuthentication()) {
            addAuthenticationToQuery(queryParams);
        }

        targetUrl = new QueryBuilder().addQueryParamsToUrl(targetUrl, queryParams);

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

    private HttpURLConnection buildHttpUrlConnection(String targetUrl, String requestMethod) {
        HttpURLConnection conn = null;

        try {
            URL url = new URL(targetUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            if (isBearerAuthentication()) {
                setConnectionBearerAuthentication(conn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conn;
    }

    private boolean isBearerAuthentication() {
        return this.authenticationType == AuthenticationType.BEARER;
    }

    private boolean isQueryAuthentication() {
        return this.authenticationType == AuthenticationType.QUERY;
    }

    private void setConnectionBearerAuthentication(HttpURLConnection conn) {
        String authString = "Bearer " + token;
        conn.setRequestProperty("Authorization", authString);
    }

    private void addAuthenticationToQuery(Map<String, String> queryParams) {
        queryParams.put("key", this.token);
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
