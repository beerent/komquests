package com.komquests.api.rest;

import com.komquests.api.models.rest.ApiToken;
import com.komquests.api.models.rest.HttpRequestResponse;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestService {
    private static final String GET = "GET";
    private static final String POST = "POST";

    private ApiToken apiToken;

    public RestService() {
        this.apiToken = null;
    }

    public RestService(ApiToken apiToken) {
        this.apiToken = apiToken;
    }

    public ApiToken getApiToken() {
        return this.apiToken;
    }

    public void setApiToken(ApiToken apiToken) {
        this.apiToken = apiToken;
    }

    public HttpRequestResponse get(String targetUrl) {
        return get(targetUrl, new HashMap<>());
    }

    public HttpRequestResponse get(String targetUrl, Map<String, String> queryParams) {
        return request(GET, targetUrl, queryParams);
    }

    public HttpRequestResponse post(String targetUrl) {
        return post(targetUrl, new HashMap<>());
    }

    public HttpRequestResponse post(String targetUrl, Map<String, String> queryParams) {
        return request(POST, targetUrl, queryParams);
    }

    private HttpRequestResponse request(String method, String targetUrl, Map<String, String> queryParams) {
        if (isQueryAuthentication()) {
            addAuthenticationToQuery(queryParams);
        }

        targetUrl = new QueryBuilder().addQueryParamsToUrl(targetUrl, queryParams);
        HttpURLConnection conn = buildHttpUrlConnection(targetUrl, method);
        if (!isValidHttpUrlConnection(conn)) {
            return null;
        }

        return createHttpRequestResponse(conn);
    }

    private HttpRequestResponse createHttpRequestResponse(HttpURLConnection conn) {
        int code = getResponseCode(conn);
        String message = getResponseMessage(conn);
        String body = getResponseBody(conn);

        return new HttpRequestResponse(code, message, body);
    }

    private Integer getResponseCode(HttpURLConnection conn) {
        try {
            return conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getResponseMessage(HttpURLConnection conn) {
        try {
            return conn.getResponseMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getResponseBody(HttpURLConnection conn) {
        try {
            return getResponseFromRequest(new InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            //.printStackTrace();
        }

        return null;
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
        return apiKeyIsValid() && AuthenticationType.BEARER == this.apiToken.getAuthenticationType();
    }

    private boolean isQueryAuthentication() {
        return apiKeyIsValid() && AuthenticationType.QUERY == this.apiToken.getAuthenticationType();
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
        if (conn == null) {
            return false;
        }

        return true;
    }

    private boolean apiKeyIsValid() {
        return this.apiToken != null;
    }
}
