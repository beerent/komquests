package com.komquests.api.rest.endpoints;

import com.komquests.api.config.ConfigReader;
import com.komquests.api.google.GeocodeConnector;
import com.komquests.api.models.rest.ApiToken;
import com.komquests.api.models.strava.location.Coordinates;
import com.komquests.api.models.strava.segment.Segment;
import com.komquests.api.models.strava.segment.SegmentSearchRequest;
import com.komquests.api.models.strava.segment.leaderboard.SegmentLeaderboard;
import com.komquests.api.rest.AuthenticationType;
import com.komquests.api.rest.RestService;
import com.komquests.api.rest.StravaApiTokenRetriever;
import com.komquests.api.strategy.SweepSearchCoordinateProvider;
import com.komquests.api.strava.StravaConnector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EndpointController {

    public static List<Segment> recommend(int watts, String address) throws Exception {
        File f = new File(CONFIG_FILE_PATH);
        ConfigReader configReader = new ConfigReader(f);

        ApiToken googleApiToken = new ApiToken(configReader.getValue("geocode_token"), AuthenticationType.QUERY);
        RestService googleRestService = new RestService(googleApiToken);
        GeocodeConnector geocodeConnector = new GeocodeConnector(googleRestService);

        StravaApiTokenRetriever stravaApiTokenRetriever = createStravaApiTokenRetriever();
        StravaConnector stravaConnector = new StravaConnector(new RestService(), stravaApiTokenRetriever);

        Coordinates coordinates = geocodeConnector.getCoordinates(address);
        List<SegmentSearchRequest> segmentSearchRequests = new SweepSearchCoordinateProvider().search(new Coordinates(coordinates.getLatitude(), coordinates.getLongitude()), 5, 9);
        List<Segment> achiavableSegments = new ArrayList<>();
        List<Segment> observedSegments = new ArrayList<>();
        for (SegmentSearchRequest segmentSearchRequest : segmentSearchRequests)
        {
            List<Segment> localSegments = stravaConnector.getSegmentRecommendations(segmentSearchRequest);
            for (Segment segment : localSegments) {
                if (segmentAlreadyObserved(observedSegments, segment))
                {
                    continue;
                }

                observedSegments.add(segment);
                double miles = segment.getDistance() / 1609.34;
                SegmentLeaderboard segmentLeaderboard = stravaConnector.getSegmentLeaderboard(segment.getId());
                if (segmentLeaderboard.getFirstPlace().getPower() < Integer.valueOf(watts) && segmentLeaderboard.getFirstPlace().getPower() > 0) {
                    System.out.println("" + segment.getId() + " " + segment.getName());
                    System.out.println("\tDistance: " + miles + " miles\tPower: " + segmentLeaderboard.getFirstPlace().getPower() + " watts");
                    achiavableSegments.add(segment);
                }
            }
        }

        return observedSegments;
    }

    private static boolean segmentAlreadyObserved(List<Segment> observedSegments, Segment segment) {
        for (Segment observedSegment : observedSegments) {
            if (observedSegment.getId() == segment.getId()) {
                return true;
            }
        }

        return false;
    }

    private static StravaApiTokenRetriever createStravaApiTokenRetriever() {
        File f = new File(CONFIG_FILE_PATH);
        ConfigReader configReader = new ConfigReader(f);

        String clientId = configReader.getValue("strava_client_id");
        String clientSecret = configReader.getValue("strava_client_secret");
        String refreshToken = configReader.getValue("strava_refresh_token");

        return new StravaApiTokenRetriever(new RestService(), clientId, clientSecret, refreshToken);
    }
}
