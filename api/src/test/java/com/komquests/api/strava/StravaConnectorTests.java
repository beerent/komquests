package com.komquests.api.strava;

import com.komquests.api.rest.RestService;
import com.komquests.api.strava.models.Segment;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StravaConnectorTests {
    @Test
    public void testGetSegmentWithValidId() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(getValidResponse());

        StravaConnector stravaConnector = new StravaConnector(restService);
        Segment segment = stravaConnector.getSegment(1);
        assertNotNull(segment);
    }

    @Test
    public void testGetSegmentWithInvalidId() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(null);

        StravaConnector stravaConnector = new StravaConnector(restService);
        Segment segment = stravaConnector.getSegment(-1);
        assertNull(segment);
    }

    private String getValidResponse() {
        return "{\n" +
                "    \"id\": 740569,\n" +
                "    \"resource_state\": 3,\n" +
                "    \"name\": \"Rain Creek Fun Climb - LONG\",\n" +
                "    \"activity_type\": \"Ride\",\n" +
                "    \"distance\": 612.569,\n" +
                "    \"average_grade\": 9.7,\n" +
                "    \"maximum_grade\": 24.9,\n" +
                "    \"elevation_high\": 263.6,\n" +
                "    \"elevation_low\": 204.2,\n" +
                "    \"start_latlng\": [\n" +
                "        30.39838139131488,\n" +
                "        -97.76269343956046\n" +
                "    ],\n" +
                "    \"end_latlng\": [\n" +
                "        30.39662806551883,\n" +
                "        -97.75683584816639\n" +
                "    ],\n" +
                "    \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/G62AXG6K3SNMOVHXU4E5VRERSKR2XNZQYWSZCY2V2WKORG2KSSZ42OTTZI3HSQMF6KAD3GEAML73PHJ4FACA====\",\n" +
                "    \"start_latitude\": 30.39838139131488,\n" +
                "    \"start_longitude\": -97.76269343956046,\n" +
                "    \"end_latitude\": 30.39662806551883,\n" +
                "    \"end_longitude\": -97.75683584816639,\n" +
                "    \"climb_category\": 0,\n" +
                "    \"city\": \"Austin\",\n" +
                "    \"state\": \"TX\",\n" +
                "    \"country\": \"United States\",\n" +
                "    \"private\": false,\n" +
                "    \"hazardous\": false,\n" +
                "    \"starred\": false,\n" +
                "    \"created_at\": \"2011-08-31T19:49:14Z\",\n" +
                "    \"updated_at\": \"2021-05-20T08:02:05Z\",\n" +
                "    \"total_elevation_gain\": 59.53,\n" +
                "    \"map\": {\n" +
                "        \"id\": \"s740569\",\n" +
                "        \"polyline\": \"{dpxDzgusQVq@Hg@B]Ac@Eo@OcBHiA`@qA`@u@Rc@lAmGv@yCn@oAPk@f@iA\",\n" +
                "        \"resource_state\": 3\n" +
                "    },\n" +
                "    \"effort_count\": 13294,\n" +
                "    \"athlete_count\": 2233,\n" +
                "    \"star_count\": 42,\n" +
                "    \"athlete_segment_stats\": {\n" +
                "        \"pr_elapsed_time\": null,\n" +
                "        \"pr_date\": null,\n" +
                "        \"pr_activity_id\": null,\n" +
                "        \"effort_count\": 0\n" +
                "    },\n" +
                "    \"xoms\": {\n" +
                "        \"kom\": \"1:16\",\n" +
                "        \"qom\": \"2:00\",\n" +
                "        \"destination\": {\n" +
                "            \"href\": \"strava://segments/740569/leaderboard\",\n" +
                "            \"type\": \"overall\",\n" +
                "            \"name\": \"All-Time\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"local_legend\": {\n" +
                "        \"athlete_id\": 86665167,\n" +
                "        \"title\": \"Tyson Nordmann\",\n" +
                "        \"profile\": \"https://d3nn82uaxijpm6.cloudfront.net/assets/avatar/athlete/large-800a7033cc92b2a5548399e26b1ef42414dd1a9cb13b99454222d38d58fd28ef.png\",\n" +
                "        \"effort_description\": \"19 efforts in the last 90 days\",\n" +
                "        \"effort_count\": \"19\",\n" +
                "        \"effort_counts\": {\n" +
                "            \"overall\": \"19 efforts\",\n" +
                "            \"female\": \"5 efforts\"\n" +
                "        },\n" +
                "        \"destination\": \"strava://segments/740569/local_legend?categories%5B%5D=overall\"\n" +
                "    }\n" +
                "}";
    }
}
