package com.komquests.api.strava;

import com.google.gson.Gson;
import com.komquests.api.rest.RestService;
import com.komquests.api.strava.models.Segment;

public class StravaConnector {
    private static final String SEGMENT_ENDPOINT = "https://www.strava.com/api/v3/segments";
    private RestService restService;

    public StravaConnector(RestService restService) {
        this.restService = restService;
    }

    public Segment getSegment(int id) {
        String requestUrl = buildGetSegmentRequestUrl(id);

        String response = this.restService.get(requestUrl);
        if (!isValidResponse(response)) {
            return null;
        }

        Segment segment = jsonToSegmentObject(response);
        return segment;
    }

    private Segment jsonToSegmentObject(String json) {
        return new Gson().fromJson(json, Segment.class);
    }

    private String buildGetSegmentRequestUrl(int id) {
        return String.format("%s/%s", SEGMENT_ENDPOINT, id);
    }

    private boolean isValidResponse(String response) {
        return response != null;
    }
}
