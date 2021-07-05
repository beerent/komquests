package com.komquests.api.strava;

import com.google.gson.Gson;
import com.komquests.api.http.HttpConnector;
import com.komquests.api.rest.RestService;
import com.komquests.api.strava.models.segment.Segment;
import com.komquests.api.strava.models.segment_leaderboard.SegmentLeaderboard;

public class StravaConnector {
    private static final String SEGMENT_ENDPOINT = "https://www.strava.com/api/v3/segments";
    private static final String SEGMENT_LEADERBOARD_ENDPOINT = "https://www.strava.com/segments";
    private static final String SEGMENT_LEADERBOARD_START_TRIM_KEYWORD = "<h2 class='text-title1'>Overall Leaderboard</h2>";
    private static final String SEGMENT_LEADERBOARD_END_TRIM_KEYWORD = "</div>";
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

    public SegmentLeaderboard getSegmentLeaderboard(int id) {
        String requestUrl = buildGetSegmentLeaderboardRequestUrl(id);

        String response = new HttpConnector(this.restService)
                .get(requestUrl, SEGMENT_LEADERBOARD_START_TRIM_KEYWORD, SEGMENT_LEADERBOARD_END_TRIM_KEYWORD);

        if (!isValidResponse(response)) {
            return null;
        }

        return null; //buildSegmentLeaderboard(response);
    }

    private Segment jsonToSegmentObject(String json) {
        return new Gson().fromJson(json, Segment.class);
    }

    private String buildGetSegmentRequestUrl(int id) {
        return String.format("%s/%s", SEGMENT_ENDPOINT, id);
    }

    private String buildGetSegmentLeaderboardRequestUrl(int id) {
        return String.format("%s/%s", SEGMENT_LEADERBOARD_ENDPOINT, id);
    }

    private boolean isValidResponse(String response) {
        return response != null;
    }
}
