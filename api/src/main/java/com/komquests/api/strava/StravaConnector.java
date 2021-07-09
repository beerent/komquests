package com.komquests.api.strava;

import com.google.gson.Gson;
import com.komquests.api.http.HttpConnector;
import com.komquests.api.models.rest.ApiToken;
import com.komquests.api.models.strava.location.Coordinates;
import com.komquests.api.models.strava.segment.SegmentSearchRequest;
import com.komquests.api.rest.AuthenticationType;
import com.komquests.api.rest.RestService;
import com.komquests.api.models.strava.segment.Segment;
import com.komquests.api.models.strava.segment.leaderboard.SegmentLeaderboard;
import com.komquests.api.rest.StravaApiTokenRetriever;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StravaConnector {
    private static final String SEGMENT_ENDPOINT = "https://www.strava.com/api/v3/segments";
    private static final String SEGMENT_LEADERBOARD_ENDPOINT = "https://www.strava.com/segments";
    private static final String SEGMENT_RECOMMENDATION_ENDPOINT = "https://www.strava.com/api/v3/segments/explore";
    private static final String SEGMENT_LEADERBOARD_START_TRIM_KEYWORD = "<h2 class='text-title1'>Overall Leaderboard</h2>";
    private static final String SEGMENT_LEADERBOARD_END_TRIM_KEYWORD = "</div>";
    private RestService restService;
    private final StravaApiTokenRetriever stravaApiTokenRetriever;

    public StravaConnector(RestService restService, StravaApiTokenRetriever stravaApiTokenRetriever) {
        this.restService = restService;
        this.stravaApiTokenRetriever = stravaApiTokenRetriever;
    }

    public Segment getSegment(int id) {
        String requestUrl = buildGetSegmentRequestUrl(id);

        String response = this.restService.get(requestUrl);
        if (!isValidResponse(response)) {
            return null;
        }

        if (isApiTokenExpired(response)) {
            refreshApiToken();
            return getSegment(id);
        }

        Segment segment = jsonToSegmentObject(response);
        return segment;
    }

    private void refreshApiToken() {
        String apiToken = this.stravaApiTokenRetriever.fetch();
        this.restService.getApiToken().setApiToken(apiToken);
    }

    public SegmentLeaderboard getSegmentLeaderboard(int id) {
        String requestUrl = buildGetSegmentLeaderboardRequestUrl(id);

        String response = new HttpConnector(this.restService)
                .get(requestUrl, SEGMENT_LEADERBOARD_START_TRIM_KEYWORD, SEGMENT_LEADERBOARD_END_TRIM_KEYWORD);

        if (!isValidResponse(response)) {
            return null;
        }

        return new SegmentLeaderboardBuilder().build(response);
    }

    public List<Segment> getSegmentRecommendations(SegmentSearchRequest segmentSearchRequest) {
        Coordinates southWestCoordinates = segmentSearchRequest.getSouthWestCoordinates();
        Coordinates northEastCoordinates = segmentSearchRequest.getNorthEastCoordinates();

        String requestUrl = SEGMENT_RECOMMENDATION_ENDPOINT;
        Map<String, String> queryParams = buildGetSegmentRecommendationsQueryParams(southWestCoordinates, northEastCoordinates);

        String response = this.restService.get(requestUrl, queryParams);

        if (!isValidResponse(response)) {
            return null;
        }

        return new SegmentRecommendationBuilder().build(response);
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

    private Map<String, String> buildGetSegmentRecommendationsQueryParams(Coordinates southWestCoordinates, Coordinates northEastCoordinates) {
        String boundsValue = String.format("%s,%s,%s,%s",
                southWestCoordinates.getLatitude(), southWestCoordinates.getLongitude(),
                northEastCoordinates.getLatitude(), northEastCoordinates.getLongitude());

        return new HashMap<String, String>() {{ put("bounds", boundsValue); }};
    }

    private boolean isValidResponse(String response) {
        return response != null;
    }

    private boolean isApiTokenExpired(String response) {
        return this.stravaApiTokenRetriever.isExpired(response);
    }
}
