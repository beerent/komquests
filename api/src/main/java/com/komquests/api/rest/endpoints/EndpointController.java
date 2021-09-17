package com.komquests.api.rest.endpoints;

import com.komquests.api.config.ConfigReader;
import com.komquests.api.google.GeocodeConnector;
import com.komquests.api.models.rest.ApiToken;
import com.komquests.api.models.strava.location.Coordinates;
import com.komquests.api.models.strava.segment.Segment;
import com.komquests.api.models.strava.segment.SegmentSearchRequest;
import com.komquests.api.models.strava.segment.leaderboard.CyclingSegmentLeaderboardEntry;
import com.komquests.api.models.strava.segment.leaderboard.RunningSegmentLeaderboardEntry;
import com.komquests.api.models.strava.segment.leaderboard.SegmentLeaderboard;
import com.komquests.api.models.strava.segment.leaderboard.SegmentRecommendation;
import com.komquests.api.rest.AuthenticationType;
import com.komquests.api.rest.RestService;
import com.komquests.api.rest.StravaApiTokenRetriever;
import com.komquests.api.strategy.SweepSearchCoordinateProvider;
import com.komquests.api.strava.StravaConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EndpointController {
    //private static final String CONFIG_FILE_PATH = "/home/ubuntu/.komquests/config";
    private static final String CONFIG_FILE_PATH = "/Users/beerent/.komquests/config";

    @GetMapping("/recommend_cycling")
    List<SegmentRecommendation> recommendCycling(@RequestParam("watts") String watts, @RequestParam("address") String address) throws Exception {
        File f = new File(CONFIG_FILE_PATH);
        ConfigReader configReader = new ConfigReader(f);

        ApiToken googleApiToken = new ApiToken(configReader.getValue("geocode_token"), AuthenticationType.QUERY);
        RestService googleRestService = new RestService(googleApiToken);
        GeocodeConnector geocodeConnector = new GeocodeConnector(googleRestService);

        StravaApiTokenRetriever stravaApiTokenRetriever = createStravaApiTokenRetriever();
        StravaConnector stravaConnector = new StravaConnector(new RestService(), stravaApiTokenRetriever);

        Coordinates coordinates = geocodeConnector.getCoordinates(address);
        List<SegmentSearchRequest> segmentSearchRequests = new SweepSearchCoordinateProvider().search(new Coordinates(coordinates.getLatitude(), coordinates.getLongitude()), 5, 9);
        List<SegmentRecommendation> recommendations = new ArrayList<>();
        List<Segment> observedSegments = new ArrayList<>();
        for (SegmentSearchRequest segmentSearchRequest : segmentSearchRequests)
        {
            List<Segment> localSegments = stravaConnector.getCyclingSegmentRecommendations(segmentSearchRequest);

            for (Segment segment : localSegments) {
                if (segmentAlreadyObserved(observedSegments, segment))
                {
                    continue;
                }

                observedSegments.add(segment);
                double miles = segment.getDistance() / 1609.34;
                SegmentLeaderboard<CyclingSegmentLeaderboardEntry> segmentLeaderboard = stravaConnector.getCyclingSegmentLeaderboard(segment.getId());
                if (segmentLeaderboard.getFirstPlace().getPower() < Integer.valueOf(watts) && segmentLeaderboard.getFirstPlace().getPower() > 0) {
                    SegmentRecommendation segmentRecommendation = new SegmentRecommendation(segment, segmentLeaderboard, miles);
                    recommendations.add(segmentRecommendation);
                }
            }
        }

        return recommendations;
    }

    @GetMapping("/recommend_running")
    List<SegmentRecommendation> recommendRunning(@RequestParam("pace") String pace, @RequestParam("address") String address) throws Exception {
        File f = new File(CONFIG_FILE_PATH);
        ConfigReader configReader = new ConfigReader(f);

        ApiToken googleApiToken = new ApiToken(configReader.getValue("geocode_token"), AuthenticationType.QUERY);
        RestService googleRestService = new RestService(googleApiToken);
        GeocodeConnector geocodeConnector = new GeocodeConnector(googleRestService);

        StravaApiTokenRetriever stravaApiTokenRetriever = createStravaApiTokenRetriever();
        StravaConnector stravaConnector = new StravaConnector(new RestService(), stravaApiTokenRetriever);

        Coordinates coordinates = geocodeConnector.getCoordinates(address);
        List<SegmentSearchRequest> segmentSearchRequests = new SweepSearchCoordinateProvider().search(new Coordinates(coordinates.getLatitude(), coordinates.getLongitude()), 5, 9);
        List<SegmentRecommendation> recommendations = new ArrayList<>();
        List<Segment> observedSegments = new ArrayList<>();
        for (SegmentSearchRequest segmentSearchRequest : segmentSearchRequests)
        {
            List<Segment> localSegments = stravaConnector.getRunSegmentRecommendations(segmentSearchRequest);

            for (Segment segment : localSegments) {
                if (segmentAlreadyObserved(observedSegments, segment))
                {
                    continue;
                }

                observedSegments.add(segment);
                double miles = segment.getDistance() / 1609.34;
                SegmentLeaderboard<RunningSegmentLeaderboardEntry> segmentLeaderboard = stravaConnector.getRunningSegmentLeaderboard(segment.getId());
                //if (segmentLeaderboard.getFirstPlace().getPower() < Integer.valueOf(watts) && segmentLeaderboard.getFirstPlace().getPower() > 0) {
                    SegmentRecommendation segmentRecommendation = new SegmentRecommendation(segment, segmentLeaderboard, miles);
                    recommendations.add(segmentRecommendation);
                //}
            }
        }

        return recommendations;
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