package com.komquests.api.http;

import com.komquests.api.rest.RestService;

public class HttpConnector {
    private RestService restService;

    public HttpConnector(RestService restService) {
        this.restService = restService;
    }

    public String get(String targetUrl) {
        String response = this.restService.get(targetUrl);

        return response;
    }

    public String get(String targetUrl, String trimStart, String trimEnd) {
        String response = get(targetUrl);
        int trimIndexStart = response.indexOf(trimStart) + trimStart.length();
        int trimIndexEnd = response.indexOf(trimEnd);

        return response.substring(trimIndexStart, trimIndexEnd);
    }
}
