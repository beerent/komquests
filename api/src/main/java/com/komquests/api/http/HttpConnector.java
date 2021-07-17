package com.komquests.api.http;

import com.komquests.api.models.rest.HttpRequestResponse;
import com.komquests.api.rest.RestService;

public class HttpConnector {
    private RestService restService;

    public HttpConnector(RestService restService) {
        this.restService = restService;
    }

    public String get(String targetUrl) {
        HttpRequestResponse httpRequestResponse = this.restService.get(targetUrl);
        return httpRequestResponse.getBody();
    }

    public String get(String targetUrl, String trimStart, String trimEnd) {
        String response = get(targetUrl);
        if (!responseIsValid(response)) {
            return response;
        }

        int trimIndexStart = response.indexOf(trimStart) + trimStart.length();
        int trimIndexEnd = response.substring(trimIndexStart).indexOf(trimEnd) + trimIndexStart;

        return response.substring(trimIndexStart, trimIndexEnd);
    }

    private boolean responseIsValid(String response) {
        return response != null;
    }
}
