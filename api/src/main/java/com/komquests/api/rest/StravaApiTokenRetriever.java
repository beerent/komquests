package com.komquests.api.rest;

public class StravaApiTokenRetriever {
    private static final String REFRESH_TOKEN_ENDPOINT = "https://www.strava.com/api/v3/oauth/token";
    private static final CharSequence EXPIRED_TOKEN_KEYWORD = "Authorization Error";
    private RestService restService;

    public StravaApiTokenRetriever(RestService restService) {
        this.restService = restService;
    }

    public String fetch() {
        return restService.post(REFRESH_TOKEN_ENDPOINT);
    }

    public boolean isExpired(String response) {
        return response.contains(EXPIRED_TOKEN_KEYWORD);
    }
}
