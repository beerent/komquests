package com.komquests.api.rest.endpoints;

import com.komquests.api.config.ConfigReader;
import com.komquests.api.google.GeocodeConnector;
import com.komquests.api.models.rest.ApiToken;
import com.komquests.api.models.strava.location.Coordinates;
import com.komquests.api.models.strava.segment.Segment;
import com.komquests.api.models.strava.segment.SegmentSearchRequest;
import com.komquests.api.models.strava.segment.leaderboard.RunningSegmentLeaderboardEntry;
import com.komquests.api.models.strava.segment.leaderboard.SegmentLeaderboard;
import com.komquests.api.models.strava.segment.leaderboard.SegmentRecommendation;
import com.komquests.api.rest.AuthenticationType;
import com.komquests.api.rest.RestService;
import com.komquests.api.rest.StravaApiTokenRetriever;
import com.komquests.api.strategy.SweepSearchCoordinateProvider;
import com.komquests.api.strava.StravaActivity;
import com.komquests.api.strava.StravaConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EndpointController {
    private static final String CONFIG_FILE_PATH = "/home/ubuntu/.komquests/config";
    //private static final String CONFIG_FILE_PATH = "/Users/beerent/.komquests/config";

    private static final Logger logger = LogManager.getLogger(EndpointController.class);

    @GetMapping("/recommend_cycling")
    List<SegmentRecommendation> recommendCycling(
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "longitude", required = false) String longitude,
            @RequestParam(value = "latitude", required = false) String latitude) {
        logger.info(String.format("cycling request: address [%s] coordinates [%s, %s]", address, longitude, latitude));

        Coordinates coordinates = null;
        if (longitude != null && latitude != null) {
            coordinates = new Coordinates(Double.valueOf(latitude), Double.valueOf(longitude));
        } else if (address != null) {
            coordinates = getCoordinatesFromAddress(address);
        }

        if (coordinates == null) {
            return new ArrayList<>();
        }

        return getRecommendations(StravaActivity.CYCLING, coordinates);
    }

    @GetMapping("/recommend_running")
    List<SegmentRecommendation> recommendRunning(
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "longitude", required = false) String longitude,
            @RequestParam(value = "latitude", required = false) String latitude) {
        logger.info(String.format("run request: address [%s] coordinates [%s, %s]", address, longitude, latitude));

        Coordinates coordinates = null;
        if (longitude != null && latitude != null) {
            coordinates = new Coordinates(Double.valueOf(latitude), Double.valueOf(longitude));
        } else if (address != null) {
            coordinates = getCoordinatesFromAddress(address);
        }

        if (coordinates == null) {
            return new ArrayList<>();
        }

        return getRecommendations(StravaActivity.RUNNING, coordinates);
    }

    private List<SegmentRecommendation> getRecommendations(StravaActivity activity, Coordinates coordinates) {
        StravaApiTokenRetriever stravaApiTokenRetriever = createStravaApiTokenRetriever();
        StravaConnector stravaConnector = new StravaConnector(new RestService(), stravaApiTokenRetriever);

        List<SegmentSearchRequest> segmentSearchRequests = getSegmentSearchRequests(coordinates);

        return getSegmentRecommendations(activity, stravaConnector, segmentSearchRequests);
    }

    private List<SegmentRecommendation> getSegmentRecommendations(StravaActivity activity, StravaConnector stravaConnector, List<SegmentSearchRequest> segmentSearchRequests) {
        if (activity == StravaActivity.CYCLING)
        {
            return getCyclingSegmentRecommendations(stravaConnector, segmentSearchRequests);
        }
        
        return getRunningSegmentRecommendations(stravaConnector, segmentSearchRequests);
    }

    private List<SegmentRecommendation> getCyclingSegmentRecommendations(StravaConnector stravaConnector, List<SegmentSearchRequest> segmentSearchRequests) {
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
                double miles = metersToMiles(segment);
                SegmentLeaderboard<RunningSegmentLeaderboardEntry> segmentLeaderboard = stravaConnector.getCyclingSegmentLeaderboard(segment.getId());
                SegmentRecommendation segmentRecommendation = new SegmentRecommendation(segment, segmentLeaderboard, miles);
                recommendations.add(segmentRecommendation);
            }
        }

        return recommendations;
    }

    private List<SegmentRecommendation> getRunningSegmentRecommendations(StravaConnector stravaConnector, List<SegmentSearchRequest> segmentSearchRequests) {
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
                double miles = metersToMiles(segment);
                SegmentLeaderboard<RunningSegmentLeaderboardEntry> segmentLeaderboard = stravaConnector.getRunningSegmentLeaderboard(segment.getId());
                SegmentRecommendation segmentRecommendation = new SegmentRecommendation(segment, segmentLeaderboard, miles);
                recommendations.add(segmentRecommendation);
            }
        }

        return recommendations;
    }

    private Coordinates getCoordinatesFromAddress(String address) {
        File f = new File(CONFIG_FILE_PATH);
        ConfigReader configReader = new ConfigReader(f);
        ApiToken googleApiToken = new ApiToken(configReader.getValue("geocode_token"), AuthenticationType.QUERY);
        RestService googleRestService = new RestService(googleApiToken);
        GeocodeConnector geocodeConnector = new GeocodeConnector(googleRestService);
        Coordinates coordinates = geocodeConnector.getCoordinates(address);

        return coordinates;
    }

    private double metersToMiles(Segment segment) {
        return segment.getDistance() / 1609.34;
    }

    private List<SegmentSearchRequest> getSegmentSearchRequests(Coordinates coordinates) {
        List<SegmentSearchRequest> segmentSearchRequests = null;
        try {
            segmentSearchRequests = new SweepSearchCoordinateProvider().search(new Coordinates(coordinates.getLatitude(), coordinates.getLongitude()), 5, 9);
        } catch (Exception e) {
            logger.error("failed to get segment search requests.", e);
        }
        return segmentSearchRequests;
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
