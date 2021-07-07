package com.komquests.api.main;

import com.komquests.api.config.ConfigReader;
import com.komquests.api.google.GeocodeConnector;
import com.komquests.api.models.strava.location.Coordinates;
import com.komquests.api.models.strava.segment.Segment;
import com.komquests.api.models.strava.segment.SegmentSearchRequest;
import com.komquests.api.models.strava.segment.leaderboard.SegmentLeaderboard;
import com.komquests.api.rest.AuthenticationType;
import com.komquests.api.rest.RestService;
import com.komquests.api.strava.SegmentLeaderboardBuilder;
import com.komquests.api.strava.StravaConnector;
import com.komquests.api.strava.coordinates.CoordinatesRangeCalculator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class KOMQuests {
    public static void main(String[] args) {
        File f = new File("/Users/beerent/.komquests/config");
        ConfigReader configReader = new ConfigReader(f);

        RestService googleRestService = new RestService(configReader.getValue("geocode_token"), AuthenticationType.QUERY);
        GeocodeConnector geocodeConnector = new GeocodeConnector(googleRestService);

        RestService stravaRestService = new RestService(configReader.getValue("strava_token"), AuthenticationType.BEARER);
        StravaConnector stravaConnector = new StravaConnector(stravaRestService);

        List<Segment> achiavableSegments = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            Coordinates coordinates = geocodeConnector.getCoordinates("78660");
            coordinates = new CoordinatesRangeCalculator().calculateWestCoordinates(coordinates, i);
            SegmentSearchRequest segmentSearchRequest = new SegmentSearchRequest(coordinates, 1d);
            List<Segment> localSegments = stravaConnector.getSegmentRecommendations(segmentSearchRequest);
            for (Segment segment : localSegments) {
                double miles = segment.getDistance() / 1609.34;
                SegmentLeaderboard segmentLeaderboard = stravaConnector.getSegmentLeaderboard(segment.getId());
                if (segmentLeaderboard.getFirstPlace().getPower() < 300 && miles < 1d) {
                    System.out.println(segment.getName());
                    System.out.println("\tDistance: " + miles + " miles\tPower: " + segmentLeaderboard.getFirstPlace().getPower() + " watts");
                    achiavableSegments.add(segment);
                }
            }
        }

        int x = 4;
    }
}
