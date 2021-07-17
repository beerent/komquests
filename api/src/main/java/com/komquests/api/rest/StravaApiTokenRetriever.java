package com.komquests.api.rest;

import com.google.gson.Gson;
import com.komquests.api.models.rest.HttpRequestResponse;

import java.util.HashMap;
import java.util.Map;

public class StravaApiTokenRetriever {
    private static final String REFRESH_TOKEN_ENDPOINT = "https://www.strava.com/api/v3/oauth/token";
    private final String REFRESH_TOKEN = "refresh_token";
    private static final Object ACCESS_TOKEN = "access_token";
    private RestService restService;
    private String clientId;
    private String clientSecret;
    private String refreshToken;

    public StravaApiTokenRetriever(RestService restService, String clientId, String clientSecret, String refreshToken) {
        this.restService = restService;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.refreshToken = refreshToken;
    }

    public String fetch() {
        HttpRequestResponse httpRequestResponse = restService.post(REFRESH_TOKEN_ENDPOINT, buildParams());
        String apiKey = getApiKey(httpRequestResponse);

        return apiKey;
    }

    private String getApiKey(HttpRequestResponse httpRequestResponse) {
        String body = httpRequestResponse.getBody();
        Map jsonMap = new Gson().fromJson(body, Map.class);

        return (String) jsonMap.get(ACCESS_TOKEN);
    }

    private Map<String, String> buildParams() {
        return new HashMap<String, String>() {
            {
                put("grant_type", REFRESH_TOKEN);
                put("client_id", clientId);
                put("client_secret", clientSecret);
                put("refresh_token", refreshToken);
            }
        };
    }
}