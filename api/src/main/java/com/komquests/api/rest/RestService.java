package com.komquests.api.rest;
import com.komquests.api.models.rest.ApiToken;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestService {
    private static final String GET = "GET";
    private static final String POST = "POST";

    private final ApiToken apiToken;

    public RestService() {
        this.apiToken = null;
    }

    public RestService(ApiToken apiToken) {
        this.apiToken = apiToken;
    }

    public ApiToken getApiToken() {
        return this.apiToken;
    }

    public String get(String targetUrl) {
        return get(targetUrl, new HashMap<>());
    }

    public String get(String targetUrl, Map<String, String> queryParams) {
        return request(GET, targetUrl, queryParams);
    }

    public String post(String targetUrl) {
        return post(targetUrl, new HashMap<>());
    }

    public String post(String targetUrl, Map<String, String> queryParams) {
        return request(POST, targetUrl, queryParams);
    }

    private String request(String method, String targetUrl, Map<String, String> queryParams) {
        if (isQueryAuthentication()) {
            addAuthenticationToQuery(queryParams);
        }

        targetUrl = new QueryBuilder().addQueryParamsToUrl(targetUrl, queryParams);

        HttpURLConnection conn = buildHttpUrlConnection(targetUrl, method);
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
        return this.apiToken.getAuthenticationType() == AuthenticationType.BEARER;
    }

    private boolean isQueryAuthentication() {
        return this.apiToken.getAuthenticationType() == AuthenticationType.QUERY;
    }

    private void setConnectionBearerAuthentication(HttpURLConnection conn) {
        String authString = "Bearer " + apiToken.getToken();
        conn.setRequestProperty("Authorization", authString);
    }

    private void addAuthenticationToQuery(Map<String, String> queryParams) {
        queryParams.put("key", this.apiToken.getToken());
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
