package com.komquests.api.strava;

import com.google.gson.Gson;
import com.komquests.api.http.HttpConnector;
import com.komquests.api.models.rest.ApiToken;
import com.komquests.api.models.rest.HttpRequestResponse;
import com.komquests.api.models.strava.location.Coordinates;
import com.komquests.api.models.strava.segment.Segment;
import com.komquests.api.models.strava.segment.SegmentSearchRequest;
import com.komquests.api.models.strava.segment.leaderboard.SegmentLeaderboard;
import com.komquests.api.rest.AuthenticationType;
import com.komquests.api.rest.RestService;
import com.komquests.api.rest.StravaApiTokenRetriever;
import com.komquests.api.strava.leaderboard.CyclingSegmentLeaderboardBuilder;
import com.komquests.api.strava.leaderboard.RunningSegmentLeaderboardBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StravaConnector {
    private static final String SEGMENT_ENDPOINT = "https://www.strava.com/api/v3/segments";
    private static final String SEGMENT_LEADERBOARD_ENDPOINT = "https://www.strava.com/segments";
    private static final String SEGMENT_RECOMMENDATION_ENDPOINT = "https://www.strava.com/api/v3/segments/explore";
    private static final String SEGMENT_LEADERBOARD_START_TRIM_KEYWORD = "<h2 class='text-title1'>Overall Leaderboard</h2>";
    private static final String SEGMENT_LEADERBOARD_END_TRIM_KEYWORD = "</div>";
    private static final String BOUNDS_KEY = "bounds";
    private static final String ACTIVITY_KEY = "activity_type";
    private static final int UNAUTHORIZED_RESPONSE_CODE = 401;
    private RestService restService;
    private final StravaApiTokenRetriever stravaApiTokenRetriever;

    public StravaConnector(RestService restService, StravaApiTokenRetriever stravaApiTokenRetriever) {
        this.restService = restService;
        this.stravaApiTokenRetriever = stravaApiTokenRetriever;
    }

    public Segment getSegment(int id) {
        String requestUrl = buildGetSegmentRequestUrl(id);

        HttpRequestResponse httpRequestResponse = this.restService.get(requestUrl);
        if (!isValidResponse(httpRequestResponse)) {
            return null;
        }

        if (isApiTokenExpired(httpRequestResponse)) {
            refreshApiToken();
            return getSegment(id);
        }

        String body = httpRequestResponse.getBody();
        Segment segment = jsonToSegmentObject(body);
        return segment;
    }

    private void refreshApiToken() {
        String apiToken = this.stravaApiTokenRetriever.fetch();
        this.restService.setApiToken(new ApiToken(apiToken, AuthenticationType.BEARER));
    }

    public SegmentLeaderboard getCyclingSegmentLeaderboard(int id) {
        return getSegmentLeaderboard(StravaActivity.CYCLING, id);
    }

    public SegmentLeaderboard getRunningSegmentLeaderboard(int id) {
        return getSegmentLeaderboard(StravaActivity.RUNNING, id);
    }

    private SegmentLeaderboard getSegmentLeaderboard(StravaActivity activity, int id) {
        String requestUrl = buildGetSegmentLeaderboardRequestUrl(id);

        String body = new HttpConnector(this.restService).get(requestUrl, SEGMENT_LEADERBOARD_START_TRIM_KEYWORD, SEGMENT_LEADERBOARD_END_TRIM_KEYWORD);

        if (!isValidBody(body)) {
            return null;
        }

        if (activity == StravaActivity.CYCLING) {
            return new CyclingSegmentLeaderboardBuilder().build(body);
        } else {
            return new RunningSegmentLeaderboardBuilder().build(body);
        }
    }

    public List<Segment> getRunSegmentRecommendations(SegmentSearchRequest segmentSearchRequest) {
        return getSegmentRecommendations(StravaActivity.RUNNING, segmentSearchRequest);
    }

    public List<Segment> getCyclingSegmentRecommendations(SegmentSearchRequest segmentSearchRequest) {
        return getSegmentRecommendations(StravaActivity.CYCLING, segmentSearchRequest);
    }

    private List<Segment> getSegmentRecommendations(StravaActivity stravaActivity, SegmentSearchRequest segmentSearchRequest) {
        Coordinates southWestCoordinates = segmentSearchRequest.getSouthWestCoordinates();
        Coordinates northEastCoordinates = segmentSearchRequest.getNorthEastCoordinates();

        String requestUrl = SEGMENT_RECOMMENDATION_ENDPOINT;
        Map<String, String> queryParams = buildGetSegmentRecommendationsQueryParams(stravaActivity, southWestCoordinates, northEastCoordinates);

        HttpRequestResponse httpRequestResponse = this.restService.get(requestUrl, queryParams);
        if(!isValidResponse(httpRequestResponse)) {
            return null;
        }

        if (isApiTokenExpired(httpRequestResponse)) {
            refreshApiToken();
            return getSegmentRecommendations(stravaActivity, segmentSearchRequest);
        }

        return new SegmentRecommendationBuilder().build(httpRequestResponse.getBody());
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

    private Map<String, String> buildGetSegmentRecommendationsQueryParams(StravaActivity stravaActivity, Coordinates southWestCoordinates, Coordinates northEastCoordinates) {
        String boundsValue = String.format("%s,%s,%s,%s",
                southWestCoordinates.getLatitude(), southWestCoordinates.getLongitude(),
                northEastCoordinates.getLatitude(), northEastCoordinates.getLongitude());

        String stravaActivityValue = StravaActivity.getValue(stravaActivity);

        return new HashMap<String, String>() {{ put(BOUNDS_KEY, boundsValue); put(ACTIVITY_KEY, stravaActivityValue); }};
    }

    private boolean isValidResponse(HttpRequestResponse httpRequestResponse) {
        return httpRequestResponse != null;
    }

    private boolean isValidBody(String body) {
        return body != null;
    }

    private boolean isApiTokenExpired(HttpRequestResponse httpRequestResponse) {
        return UNAUTHORIZED_RESPONSE_CODE == httpRequestResponse.getCode();
    }
}
