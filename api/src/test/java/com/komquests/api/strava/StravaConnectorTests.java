package com.komquests.api.strava;

import com.komquests.api.models.rest.HttpRequestResponse;
import com.komquests.api.models.strava.location.Coordinates;
import com.komquests.api.models.strava.segment.Segment;
import com.komquests.api.models.strava.segment.SegmentSearchRequest;
import com.komquests.api.models.strava.segment.leaderboard.SegmentLeaderboard;
import com.komquests.api.rest.RestService;
import com.komquests.api.rest.StravaApiTokenRetriever;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StravaConnectorTests {
    @Test
    public void testGetSegmentWithValidId() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(getValidResponse());

        StravaApiTokenRetriever stravaApiTokenRetriever = new StravaApiTokenRetriever(restService, "", "", "");

        StravaConnector stravaConnector = new StravaConnector(restService, stravaApiTokenRetriever);
        Segment segment = stravaConnector.getSegment(1);
        assertNotNull(segment);
    }

    @Test
    public void testGetSegmentWithInvalidId() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(new HttpRequestResponse(0, null, null));

        StravaApiTokenRetriever stravaApiTokenRetriever = new StravaApiTokenRetriever(restService, "", "", "");

        StravaConnector stravaConnector = new StravaConnector(restService, stravaApiTokenRetriever);
        Segment segment = stravaConnector.getSegment(-1);
        assertNull(segment);
    }

    @Test
    public void testGetCyclingSegmentLeaderboard() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(getValidCyclingLeaderboardResponse());

        StravaApiTokenRetriever stravaApiTokenRetriever = new StravaApiTokenRetriever(restService, "", "", "");

        StravaConnector stravaConnector = new StravaConnector(restService, stravaApiTokenRetriever);
        SegmentLeaderboard segmentLeaderboard = stravaConnector.getCyclingSegmentLeaderboard(1);
        assertNotNull(segmentLeaderboard);
    }

    @Test
    public void testGetRunningSegmentLeaderboard() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(getValidRunningLeaderboardResponse());

        StravaApiTokenRetriever stravaApiTokenRetriever = new StravaApiTokenRetriever(restService, "", "", "");

        StravaConnector stravaConnector = new StravaConnector(restService, stravaApiTokenRetriever);
        SegmentLeaderboard segmentLeaderboard = stravaConnector.getRunningSegmentLeaderboard(1);
        assertNotNull(segmentLeaderboard);
    }

    @Test
    public void testGetCyclingSegmentRecommendations() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString(), Mockito.anyMap())).thenReturn(getValidRunningSegmentResponse());

        Coordinates coordinates = new Coordinates(30.446380, -97.573890);
        SegmentSearchRequest segmentSearchRequest = new SegmentSearchRequest(coordinates, 1.5);

        StravaApiTokenRetriever stravaApiTokenRetriever = new StravaApiTokenRetriever(restService, "", "", "");

        StravaConnector stravaConnector = new StravaConnector(restService, stravaApiTokenRetriever);
        List<Segment> segments = stravaConnector.getCyclingSegmentRecommendations(segmentSearchRequest);

        assertEquals(10, segments.size());
        assertEquals("400 at WilCo Park's Track ", segments.get(0).getName());
        assertEquals("Bison Stampede 10K - 2000", segments.get(9).getName());
    }

    @Test
    public void testGetRunningSegmentRecommendations() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString(), Mockito.anyMap())).thenReturn(getValidCyclingSegmentSearchResponse());

        Coordinates coordinates = new Coordinates(30.446380, -97.573890);
        SegmentSearchRequest segmentSearchRequest = new SegmentSearchRequest(coordinates, 1.5);

        StravaApiTokenRetriever stravaApiTokenRetriever = new StravaApiTokenRetriever(restService, "", "", "");

        StravaConnector stravaConnector = new StravaConnector(restService, stravaApiTokenRetriever);
        List<Segment> segments = stravaConnector.getRunSegmentRecommendations(segmentSearchRequest);

        assertEquals(10, segments.size());
        assertEquals("Jesse's hill", segments.get(0).getName());
        assertEquals("Hidden Lake Crossing East", segments.get(9).getName());
    }

    @Test
    public void testGetSegmentCanRefreshApiTokenWhenExpired() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(getExpiredTokenResponse()).thenReturn(getValidResponse());

        StravaApiTokenRetriever stravaApiTokenRetriever = Mockito.spy(new StravaApiTokenRetriever(restService, "", "", ""));
        Mockito.doReturn("new_api_key").when(stravaApiTokenRetriever).fetch();

        StravaConnector stravaConnector = Mockito.spy(new StravaConnector(restService, stravaApiTokenRetriever));
        Segment segment = stravaConnector.getSegment(1);
        assertNotNull(segment);
    }

    @Test
    public void testGetSegmentRecommendationsCanRefreshApiTokenWhenExpired() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.doReturn(getExpiredTokenResponse()).doReturn(getValidCyclingSegmentSearchResponse()).when(restService).get(Mockito.anyString(), Mockito.anyMap());
        Mockito.doReturn(new HttpRequestResponse(null, null, null)).when(restService).post(Mockito.anyString());

        Coordinates coordinates = new Coordinates(30.446380, -97.573890);
        SegmentSearchRequest segmentSearchRequest = new SegmentSearchRequest(coordinates, 1.5);

        StravaApiTokenRetriever stravaApiTokenRetriever = Mockito.spy(new StravaApiTokenRetriever(restService, "", "", ""));
        Mockito.doReturn("new_api_key").when(stravaApiTokenRetriever).fetch();

        StravaConnector stravaConnector = new StravaConnector(restService, stravaApiTokenRetriever);
        List<Segment> segments = stravaConnector.getCyclingSegmentRecommendations(segmentSearchRequest);

        assertEquals(10, segments.size());
        assertEquals("Jesse's hill", segments.get(0).getName());
        assertEquals("Hidden Lake Crossing East", segments.get(9).getName());
    }

    private HttpRequestResponse getValidResponse() {
        return new HttpRequestResponse(0, "", "{\n" +
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
                "}");
    }

    public HttpRequestResponse getValidCyclingLeaderboardResponse() {
        return new HttpRequestResponse(0, "", "<!-- Orion-App Layout -->\n" +
                "<!DOCTYPE html>\n" +
                "<html class='logged-out responsive feed3p0 old-login strava-orion responsive' dir='ltr' lang='en-US' xmlns:fb='http://www.facebook.com/2008/fbml' xmlns:og='http://opengraphprotocol.org/schema/' xmlns='http://www.w3.org/TR/html5'>\n" +
                "<!--\n" +
                "layout orion app\n" +
                "-->\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "<meta content='width = device-width, initial-scale = 1, maximum-scale = 1' name='viewport'>\n" +
                "<style type='text/css'>\n" +
                ".spinner, .spinner .status {\n" +
                "  position: relative;\n" +
                "}\n" +
                ".spinner {\n" +
                "  margin-top: 1em;\n" +
                "  margin-bottom: 1em;\n" +
                "}\n" +
                ".spinner .status {\n" +
                "  top: 2px;\n" +
                "  margin-left: 0.5em;\n" +
                "}\n" +
                ".spinner .status:empty {\n" +
                "  display: none;\n" +
                "}\n" +
                ".spinner.lg .graphic {\n" +
                "  border-width: 3px;\n" +
                "  height: 32px;\n" +
                "  width: 32px;\n" +
                "}\n" +
                ".spinner.tiny {\n" +
                "  height: 10px;\n" +
                "  width: 10px;\n" +
                "}\n" +
                ".spinner.centered, .spinner.vcentered {\n" +
                "  box-sizing: border-box;\n" +
                "  width: 100%;\n" +
                "}\n" +
                ".spinner.vcentered {\n" +
                "  left: 0;\n" +
                "  margin-top: -12px;\n" +
                "  position: absolute;\n" +
                "  right: 0;\n" +
                "  text-align: center;\n" +
                "  top: 50%;\n" +
                "}\n" +
                ".spinner .graphic, .ajax-loading-image {\n" +
                "  animation: spin 1.2s infinite linear;\n" +
                "  box-sizing: border-box;\n" +
                "  border-color: #eee;\n" +
                "  border-radius: 50%;\n" +
                "  border-style: solid;\n" +
                "  border-top-color: #666;\n" +
                "  border-top-style: solid;\n" +
                "  border-width: 2px;\n" +
                "  content: \"\";\n" +
                "  display: inline-block;\n" +
                "  height: 20px;\n" +
                "  position: relative;\n" +
                "  vertical-align: middle;\n" +
                "  width: 20px;\n" +
                "}\n" +
                "@keyframes spin {\n" +
                "  from {\n" +
                "    transform: rotate(0deg);\n" +
                "  }\n" +
                "  to {\n" +
                "    transform: rotate(359deg);\n" +
                "  }\n" +
                "}\n" +
                "</style>\n" +
                "\n" +
                "<link rel=\"stylesheet\" media=\"screen\" href=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava-app-icons-dff4c105de7bc4e5105473cf3756c5a991ebd5e39a5bc3ee0110afa6e0425650.css\" />\n" +
                "<link rel=\"stylesheet\" media=\"screen\" href=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava-orion-9d18c50a074dc9ac9bea3bd5ec57737938851e6c53d91da7a6b6524c27763fa7.css\" />\n" +
                "\n" +
                "<link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-180x180.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='180x180'>\n" +
                "<link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-152x152.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='152x152'>\n" +
                "<link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-144x144.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='144x144'>\n" +
                "<link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-120x120.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='120x120'>\n" +
                "<link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-114x114.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='114x114'>\n" +
                "<link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-76x76.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='76x76'>\n" +
                "<link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-72x72.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='72x72'>\n" +
                "<link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-60x60.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='60x60'>\n" +
                "<link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-57x57.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='57x57'>\n" +
                "<link href='https://d3nn82uaxijpm6.cloudfront.net/favicon-32x32.png?v=dLlWydWlG8' rel='icon' sizes='32x32' type='image/png'>\n" +
                "<link href='https://d3nn82uaxijpm6.cloudfront.net/icon-strava-chrome-192.png?v=dLlWydWlG8' rel='icon' sizes='192x192' type='image/png'>\n" +
                "<link href='https://d3nn82uaxijpm6.cloudfront.net/favicon-96x96.png?v=dLlWydWlG8' rel='icon' sizes='96x96' type='image/png'>\n" +
                "<link href='https://d3nn82uaxijpm6.cloudfront.net/favicon-16x16.png?v=dLlWydWlG8' rel='icon' sizes='16x16' type='image/png'>\n" +
                "<link href='/manifest.json?v=dLlWydWlG8' rel='manifest'>\n" +
                "<meta content='#FC5200' name='msapplication-TileColor'>\n" +
                "<meta content='https://d3nn82uaxijpm6.cloudfront.net/mstile-144x144.png?v=dLlWydWlG8' name='msapplication-TileImage'>\n" +
                "<meta content='#FC5200' name='theme-color'>\n" +
                "<meta content='Strava' name='apple-mobile-web-app-title'>\n" +
                "<meta content='Strava' name='application-name'>\n" +
                "<meta content='yes' name='apple-mobile-web-app-capable'>\n" +
                "<meta content='black' name='apple-mobile-web-app-status-bar-style'>\n" +
                "\n" +
                "<script type='application/ld+json'>\n" +
                "{\n" +
                "  \"@context\": \"http://schema.org\",\n" +
                "  \"@type\": \"Organization\",\n" +
                "  \"name\": \"Strava\",\n" +
                "  \"url\": \"https://www.strava.com/\",\n" +
                "  \"logo\": \"https://d3nn82uaxijpm6.cloudfront.net/assets/brands/strava/logo-strava-lg-5671105ffddb86e437bb68503a4973e8bf07e2a41c0b332d3e3bced21d537e98.png\",\n" +
                "  \"sameAs\": [\n" +
                "    \"https://facebook.com/Strava\",\n" +
                "    \"https://twitter.com/strava\",\n" +
                "    \"https://instagram.com/strava\",\n" +
                "    \"https://youtube.com/stravainc\",\n" +
                "    \"https://blog.strava.com\",\n" +
                "    \"https://github.com/strava\",\n" +
                "    \"https://medium.com/strava-engineering\"\n" +
                "  ]\n" +
                "}\n" +
                "\n" +
                "\n" +
                "</script>\n" +
                "<meta name=\"csrf-param\" content=\"authenticity_token\" />\n" +
                "<meta name=\"csrf-token\" content=\"5xR9Uz4WIpB45gT1ApkmiNWbc3mz5N8CEKdhJQHqDOEnKG2Bg5bIfpeiDCpITmWLCQcf1PbrPYjaUqZe4Qprlw==\" />\n" +
                "<script src=\"https://d3nn82uaxijpm6.cloudfront.net/packs/js/chunking_runtime-5ba71968b1202cbedf0f.js\"></script>\n" +
                "<script src=\"https://d3nn82uaxijpm6.cloudfront.net/packs/js/global-4a7bf2063759785716a4.chunk.js\"></script>\n" +
                "<script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava-head-d0f4c1f1472bbfd71048d6bbbe5e3b9041ec1330343ab67cfc3a6099b8fee09e.js\"></script>\n" +
                "\n" +
                "\n" +
                "<link rel=\"stylesheet\" media=\"screen\" href=\"https://d3nn82uaxijpm6.cloudfront.net/assets/segments/show-deb34c5603f8040e3db057abb7c6464cb9e3a5eb5e7f3fd39545e353f4ccee2a.css\" />\n" +
                "<link rel=\"stylesheet\" media=\"screen\" href=\"https://d3nn82uaxijpm6.cloudfront.net/packs/css/segments-23c548dc.chunk.css\" />\n" +
                "<title>Rain Creek Fun Climb - LONG | Strava Ride Segment in Austin, TX</title>\n" +
                "<meta content='View bike ride segment, 0.6 kilometers long, starting in Austin, TX, with 60 meters in elevation gain.' type='description'>\n" +
                "<link href='https://www.strava.com/segments/740569' rel='canonical'>\n" +
                "<link href='https://www.strava.com/segments/740569' rel='canonical'>\n" +
                "<link href='https://www.strava.com/segments/740569' hreflang='x-default' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569' hreflang='en-US' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=en-GB' hreflang='en-GB' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=fr-FR' hreflang='fr-FR' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=de-DE' hreflang='de-DE' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=pt-BR' hreflang='pt-BR' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=es-ES' hreflang='es-ES' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=it-IT' hreflang='it-IT' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=ru-RU' hreflang='ru-RU' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=es-419' hreflang='es-419' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=ja-JP' hreflang='ja-JP' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=ko-KR' hreflang='ko-KR' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=nl-NL' hreflang='nl-NL' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=zh-TW' hreflang='zh-TW' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=pt-PT' hreflang='pt-PT' rel='alternate'>\n" +
                "<link href='https://www.strava.com/segments/740569?hl=zh-CN' hreflang='zh-CN' rel='alternate'>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<script>\n" +
                "  !function(options){\n" +
                "    window.Strava = window.Strava || {};\n" +
                "    var _enabled = true;\n" +
                "    var _options = options;\n" +
                "    var _snowplowReady = null;\n" +
                "  \n" +
                "    window.Strava.ExternalAnalytics = window.Strava.ExternalAnalytics || (\n" +
                "      {\n" +
                "        isEnabled: function() {\n" +
                "          return _enabled;\n" +
                "        },\n" +
                "        isDebug: function() {\n" +
                "          return _options.debug;\n" +
                "        },\n" +
                "        track: function() {\n" +
                "        },\n" +
                "        trackV2: function(event) {\n" +
                "          var eventData = {\n" +
                "            'category': event.category,\n" +
                "            'page': event.page,\n" +
                "            'action': event.action,\n" +
                "            'element': event.element || null,\n" +
                "            'properties': event.properties || {}\n" +
                "          }\n" +
                "          if (this.isEnabled()) {\n" +
                "            snowplow('trackSelfDescribingEvent', {\n" +
                "              schema: 'iglu:com.strava/track/jsonschema/1-0-0',\n" +
                "              data: eventData\n" +
                "            });\n" +
                "          } else {\n" +
                "            !!console.table && console.table(eventData);\n" +
                "          }\n" +
                "        },\n" +
                "        trackLink: function() {\n" +
                "        },\n" +
                "        trackForm: function() {\n" +
                "        },\n" +
                "        identifyV2: function () {\n" +
                "        },\n" +
                "        page: function(pageProperties) {\n" +
                "          if(this.isEnabled()) {\n" +
                "            snowplow('trackPageView');\n" +
                "          }\n" +
                "        },\n" +
                "        identify: function(athleteId, options, eventName) {\n" +
                "          if (this.isEnabled()) {\n" +
                "            var properties = options || {}\n" +
                "            properties.athlete_id = athleteId;\n" +
                "            var eventData = {\n" +
                "              'category': 'identify',\n" +
                "              'page': null,\n" +
                "              'action': eventName,\n" +
                "              'element': null,\n" +
                "              'properties': properties\n" +
                "            };\n" +
                "            snowplow('trackSelfDescribingEvent', {\n" +
                "              schema: 'iglu:com.strava/track/jsonschema/1-0-0',\n" +
                "              data: eventData\n" +
                "            });\n" +
                "          }\n" +
                "        },\n" +
                "        reset: function() {\n" +
                "          if(this.isEnabled()) {\n" +
                "            snowplow('setUserId', null)\n" +
                "            var spCookie = document.cookie.match('_sp_id\\\\.[a-f0-9]+')\n" +
                "            if(spCookie != null) {\n" +
                "              document.cookie = spCookie[0] + \"= ; expires = Thu, 01 Jan 1970 00:00:00 GMT\"\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        setupSnowplow: function(id) {\n" +
                "          if(this.isEnabled()) {\n" +
                "            snowplow(\"newTracker\", \"cf\", \"c.strava.com\", {\n" +
                "              appId: \"strava-web\",\n" +
                "              platform: \"web\"\n" +
                "            });\n" +
                "            snowplow('setUserId', id);\n" +
                "            snowplow('enableFormTracking');\n" +
                "          }\n" +
                "        },\n" +
                "        getDomainUserId: function() {\n" +
                "          var d = jQuery.Deferred();\n" +
                "          if (this.isEnabled()) {\n" +
                "            if (!_snowplowReady) {\n" +
                "              _snowplowReady = jQuery.Deferred();\n" +
                "              snowplow(function(){\n" +
                "                _snowplowReady.resolve(this.cf.getDomainUserId());\n" +
                "              });\n" +
                "            }\n" +
                "            _snowplowReady.always(function(getDomainUserId){\n" +
                "              d.resolve(getDomainUserId);\n" +
                "            });\n" +
                "          } else {\n" +
                "            d.reject(null);\n" +
                "          }\n" +
                "          return d;\n" +
                "        },\n" +
                "        log: function(message, values) {\n" +
                "          if(this.isDebug()) {\n" +
                "            console.log(message, 'background-color: yellow; color: blue; font-size: medium;', values);\n" +
                "          }\n" +
                "        },\n" +
                "        debug: function(value) {\n" +
                "          _options.debug = value;\n" +
                "        }\n" +
                "      }\n" +
                "    )\n" +
                "  }({\n" +
                "    is_mobile: false,\n" +
                "    os: \"\",\n" +
                "    debug: false,\n" +
                "    athlete_id: null,\n" +
                "    locale: \"en-US\"\n" +
                "  });\n" +
                "</script>\n" +
                "\n" +
                "<script>\n" +
                "  !function(){\n" +
                "    var analytics = window.analytics = window.analytics || [];\n" +
                "    if(analytics.invoked) {\n" +
                "      window.console && console.error && console.error(\"Segment snippet included twice.\");\n" +
                "    } else {\n" +
                "      (function(p,l,o,w,i,n,g){if(!p[i]){p.GlobalSnowplowNamespace=p.GlobalSnowplowNamespace||[];p.GlobalSnowplowNamespace.push(i);p[i]=function(){(p[i].q=p[i].q||[]).push(arguments)};p[i].q=p[i].q||[];n=l.createElement(o);g=l.getElementsByTagName(o)[0];n.async=1;n.src=w;g.parentNode.insertBefore(n,g)}}(window,document,\"script\",\"https://dy9z4910shqac.cloudfront.net/1oG5icild0laCtJMi45LjA.js\",\"snowplow\"));\n" +
                "      Strava.ExternalAnalytics.setupSnowplow();\n" +
                "  \n" +
                "      Strava.ExternalAnalytics.page(null);\n" +
                "    }\n" +
                "  }();\n" +
                "</script>\n" +
                "\n" +
                "<script>\n" +
                "  !function(debug){\n" +
                "    window.Strava = window.Strava || {};\n" +
                "    var _enabled = false;\n" +
                "    var _debug = !!debug;\n" +
                "    var _branchData = null;\n" +
                "  \n" +
                "    window.Strava.BranchIO = window.Strava.BranchIO || (\n" +
                "      {\n" +
                "        isEnabled: function() {\n" +
                "          return _enabled;\n" +
                "        },\n" +
                "        isDebug: function() {\n" +
                "          return _debug;\n" +
                "        },\n" +
                "        dataToLocalStorage: function() {\n" +
                "          if (!_branchData) {\n" +
                "            _branchData = new Strava.BranchAnalytics.BranchData();\n" +
                "          }\n" +
                "  \n" +
                "          var d = this.data()\n" +
                "          var that = this;\n" +
                "          d.done(function(data) {\n" +
                "            that.log('storing data %o to local storage', data)\n" +
                "            _branchData.data(data)\n" +
                "          });\n" +
                "          d.fail(function(message) {\n" +
                "            that.log('failed to retrieve data from branch');\n" +
                "            _branchData.data({})\n" +
                "          });\n" +
                "          return d;\n" +
                "        },\n" +
                "        createLink: function(options) {\n" +
                "          var d = jQuery.Deferred();\n" +
                "          var data = null;\n" +
                "          const that = this;\n" +
                "          var callback = function(e, l) {\n" +
                "            if (!e) {\n" +
                "              d.resolve(l);\n" +
                "            } else {\n" +
                "              d.reject(e);\n" +
                "            }\n" +
                "          }\n" +
                "          if (options.peek_data) {\n" +
                "            data = this.dataFromLocalStorage();\n" +
                "            if (data && data.data_parsed && data.data_parsed['~referring_link']) {\n" +
                "              d.resolve(data.data_parsed['~referring_link']);\n" +
                "            } else {\n" +
                "              d.reject();\n" +
                "            }\n" +
                "          } else {\n" +
                "  \n" +
                "            Strava.ExternalAnalytics\n" +
                "              .getDomainUserId()\n" +
                "              .always(function(domainUserId){\n" +
                "                if (domainUserId) {\n" +
                "                  options.data['domainUserId'] = domainUserId;\n" +
                "                }\n" +
                "  \n" +
                "                if(that.isEnabled()) {\n" +
                "                  branch.link(options, callback);\n" +
                "                };\n" +
                "            });\n" +
                "          }\n" +
                "          return d;\n" +
                "        },\n" +
                "        dataFromLocalStorage: function() {\n" +
                "          if (!_branchData) {\n" +
                "            _branchData = new Strava.BranchAnalytics.BranchData();\n" +
                "          }\n" +
                "          return _branchData.data();\n" +
                "        },\n" +
                "        clearLocalStorage: function() {\n" +
                "          if (!_branchData) {\n" +
                "            _branchData = new Strava.BranchAnalytics.BranchData();\n" +
                "          }\n" +
                "          _branchData.data({});\n" +
                "        },\n" +
                "        data: function(checkLocalStorage) {\n" +
                "          var d = jQuery.Deferred();\n" +
                "          var that = this;\n" +
                "          var c = function(message, meta_data) {\n" +
                "            var storedData = null;\n" +
                "  \n" +
                "            if(message) {\n" +
                "              d.reject(message);\n" +
                "            } else {\n" +
                "              if (checkLocalStorage == true && (meta_data == null || meta_data.data == \"\" || meta_data.data == null)) {\n" +
                "                storedData = that.dataFromLocalStorage();\n" +
                "                that.clearLocalStorage();\n" +
                "  \n" +
                "                d.resolve(storedData);\n" +
                "              } else {\n" +
                "                d.resolve(meta_data);\n" +
                "              }\n" +
                "            }\n" +
                "          };\n" +
                "  \n" +
                "          if(this.isEnabled()) {\n" +
                "            branch.data(c);\n" +
                "            this.log('%cdata (branch enabled)');\n" +
                "          } else {\n" +
                "            this.log('%cdata (branch disabled)');\n" +
                "            d.resolve({});\n" +
                "          }\n" +
                "          return d;\n" +
                "        },\n" +
                "        identify: function(athleteId) {\n" +
                "          var callback = function(error, data) {\n" +
                "            if (error) {\n" +
                "              console.log(error);\n" +
                "            }\n" +
                "          }\n" +
                "          if(this.isEnabled()) {\n" +
                "            branch.setIdentity(athleteId, callback);\n" +
                "          }\n" +
                "        },\n" +
                "        track: function(eventName, metaData) {\n" +
                "          var callback = function(error, data) {\n" +
                "            if (error) {\n" +
                "              console.log(error);\n" +
                "            }\n" +
                "          }\n" +
                "          if(this.isEnabled()) {\n" +
                "            branch.track(eventName, metaData, callback);\n" +
                "          }\n" +
                "        },\n" +
                "        log: function(message, values) {\n" +
                "          if(this.isDebug()) {\n" +
                "            console.log(message, 'background-color: yellow; color: blue; font-size: medium;', values);\n" +
                "          }\n" +
                "        },\n" +
                "        debug: function(value) {\n" +
                "          _debug = value;\n" +
                "        }\n" +
                "      }\n" +
                "    )\n" +
                "  }(false);\n" +
                "</script>\n" +
                "\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<link rel=\"stylesheet\" media=\"screen\" href=\"https://d3nn82uaxijpm6.cloudfront.net/assets/common/smartbanner_orion-2d815ff185df733b835fcc83c93080835593cb5aa031e983fbc59b3e5f8244bc.css\" />\n" +
                "<div class='container smartbanner-content pt-md pb-md' id='smartbanner-orion'>\n" +
                "<div class='row'>\n" +
                "<div class='col-xs-12'>\n" +
                "<img class=\"app-icon\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/activities/icon-ios-app-fdd1ed3da3ce334990af99517dc8f735832f6e5a3bb97feff3abdbd091caffba.svg\" />\n" +
                "<div class='app-info mt-xs'>\n" +
                "<div class='app-name'>Strava</div>\n" +
                "<div class='app-subtitle'>Free app for Android and iPhone</div>\n" +
                "</div>\n" +
                "<div class='text-right mt-xs'>\n" +
                "<a href=\"https://www.strava.com/mobile\" class=\"btn btn-primary btn-outline btn-cta text-uppercase\" role=\"button\">Download</a>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "\n" +
                "<header id='global-header'><!--\n" +
                "deploy: 576b84c536b0fd8f2c0e7250e5495c7999590934\n" +
                "-->\n" +
                "<!--[if lte IE 8]>\n" +
                "<div class='alert alert-warning message warning mb-0 text-center'>\n" +
                "<p>It looks like you're using a version of Internet Explorer that Strava no longer supports. Please <a href='http://www.microsoft.com/en-us/download/ie.aspx?q=internet+explorer'>upgrade your web browser</a> &mdash; <a href='https://strava.zendesk.com/entries/20420212-Supported-Browsers-on-Strava'>Learn more</a>.</p>\n" +
                "</div>\n" +
                "<![endif]-->\n" +
                "<nav class='nav-bar container collapsable-nav' role='navigation'>\n" +
                "<div title=\"Return to the Strava home page\" class=\"branding\"><a class=\"branding-content\" href=\"/\"><span class=\"sr-only\">Strava</span></a></div>\n" +
                "<!-- / Nav Menu Button -->\n" +
                "<a href=\"#container-nav\" aria-expanded=\"false\" aria-controls=\"container-nav\" data-toggle=\"collapse\" class=\"btn btn-default btn-mobile-menu\" role=\"button\">Menu</a>\n" +
                "<div class='nav-container collapse' id='container-nav'>\n" +
                "<ul class='user-nav nav-group'>\n" +
                "<li class='nav-item'>\n" +
                "<a class=\"nav-link\" href=\"/mobile\">Mobile</a>\n" +
                "</li>\n" +
                "<li class='nav-item'>\n" +
                "<a class=\"nav-link\" href=\"/features\">Features</a>\n" +
                "</li>\n" +
                "<li class='nav-item'>\n" +
                "<a class=\"nav-link\" href=\"/premium?cta=premium&amp;element=nav&amp;source=global-header\">Subscription</a>\n" +
                "</li>\n" +
                "<li class='nav-item'>\n" +
                "<a class=\"nav-link\" href=\"http://blog.strava.com/?utm_source=website-header\">Blog</a>\n" +
                "</li>\n" +
                "<li class='nav-object-group'>\n" +
                "<div class='nav-item logged_out_nav'>\n" +
                "<a class='btn btn-primary btn-signup' data-segioevent='{&quot;name&quot;:&quot;Signup Flow&quot;,&quot;method&quot;:&quot;web&quot;,&quot;source&quot;:&quot;Global Header&quot;}' href='/register?cta=sign-up&amp;element=global-header&amp;plan=free&amp;source=segments_show'>\n" +
                "Sign Up\n" +
                "</a>\n" +
                "</div>\n" +
                "<div class='nav-item logged_out_nav'>\n" +
                "<a class='btn btn-default btn-login' data-segioevent='{&quot;name&quot;:&quot;Login Flow&quot;,&quot;method&quot;:&quot;web&quot;,&quot;source&quot;:&quot;Global Header&quot;}' href='https://www.strava.com/login?cta=log-in&amp;element=global-header&amp;source=segments_show'>\n" +
                "Log In\n" +
                "</a>\n" +
                "</div>\n" +
                "</li>\n" +
                "</ul>\n" +
                "\n" +
                "</div>\n" +
                "</nav>\n" +
                "</header>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "<script id='custom-map-controls-show-privacy-fullscreen-template' type='text/template'>\n" +
                "<div id='map-control-container' style='padding: 5px'>\n" +
                "<div class='js-map-control' id='map-control-container' index='1'>\n" +
                "<div class='inline-inputs' id='strava-map-controls'>\n" +
                "\n" +
                "\n" +
                "<div class='drop-down-menu' id='map-type-control'>\n" +
                "<a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a>\n" +
                "<ul class='options'>\n" +
                "<li>\n" +
                "<a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a id='start-street-view'>Street View (Start)</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a id='end-street-view'>Street View (End)</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<label>\n" +
                "<input id='privacy_toggle' type='checkbox'>Show Privacy Zone</input>\n" +
                "</label>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "\n" +
                "<a class='button' id='toggle-fullscreen'></a>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</script>\n" +
                "<script id='custom-map-controls-suggest-privacy-fullscreen-template' type='text/template'>\n" +
                "<div id='map-control-container' style='padding: 5px'>\n" +
                "<div class='js-map-control' id='map-control-container' index='1'>\n" +
                "<div class='inline-inputs' id='strava-map-controls'>\n" +
                "\n" +
                "\n" +
                "<div class='drop-down-menu' id='map-type-control'>\n" +
                "<a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a>\n" +
                "<ul class='options'>\n" +
                "<li>\n" +
                "<a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a id='start-street-view'>Street View (Start)</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a id='end-street-view'>Street View (End)</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href='/settings/privacy'>Add Privacy Zone</a>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "\n" +
                "<a class='button' id='toggle-fullscreen'></a>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</script>\n" +
                "<script id='custom-map-controls-show-privacy-template' type='text/template'>\n" +
                "<div id='map-control-container' style='padding: 5px'>\n" +
                "<div class='js-map-control' id='map-control-container' index='1'>\n" +
                "<div class='inline-inputs' id='strava-map-controls'>\n" +
                "\n" +
                "\n" +
                "<div class='drop-down-menu' id='map-type-control'>\n" +
                "<a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a>\n" +
                "<ul class='options'>\n" +
                "<li>\n" +
                "<a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a id='start-street-view'>Street View (Start)</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a id='end-street-view'>Street View (End)</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<label>\n" +
                "<input id='privacy_toggle' type='checkbox'>Show Privacy Zone</input>\n" +
                "</label>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</script>\n" +
                "<script id='custom-map-controls-suggest-privacy-template' type='text/template'>\n" +
                "<div id='map-control-container' style='padding: 5px'>\n" +
                "<div class='js-map-control' id='map-control-container' index='1'>\n" +
                "<div class='inline-inputs' id='strava-map-controls'>\n" +
                "\n" +
                "\n" +
                "<div class='drop-down-menu' id='map-type-control'>\n" +
                "<a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a>\n" +
                "<ul class='options'>\n" +
                "<li>\n" +
                "<a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a id='start-street-view'>Street View (Start)</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a id='end-street-view'>Street View (End)</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href='/settings/privacy'>Add Privacy Zone</a>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</script>\n" +
                "<script id='custom-map-controls-fullscreen-template' type='text/template'>\n" +
                "<div id='map-control-container' style='padding: 5px'>\n" +
                "<div class='js-map-control' id='map-control-container' index='1'>\n" +
                "<div class='inline-inputs' id='strava-map-controls'>\n" +
                "\n" +
                "\n" +
                "<div class='drop-down-menu' id='map-type-control'>\n" +
                "<a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a>\n" +
                "<ul class='options'>\n" +
                "<li>\n" +
                "<a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a id='start-street-view'>Street View (Start)</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a id='end-street-view'>Street View (End)</a>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "\n" +
                "<a class='button' id='toggle-fullscreen'></a>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</script>\n" +
                "<script id='custom-map-controls-template' type='text/template'>\n" +
                "<div id='map-control-container' style='padding: 5px'>\n" +
                "<div class='js-map-control' id='map-control-container' index='1'>\n" +
                "<div class='inline-inputs' id='strava-map-controls'>\n" +
                "\n" +
                "\n" +
                "<div class='drop-down-menu' id='map-type-control'>\n" +
                "<a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a>\n" +
                "<ul class='options'>\n" +
                "<li>\n" +
                "<a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a id='start-street-view'>Street View (Start)</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a id='end-street-view'>Street View (End)</a>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</script>\n" +
                "\n" +
                "<div class='container'>\n" +
                "<div class='section row' id='segment'>\n" +
                "<div class='segment-heading col-md-12'>\n" +
                "<div class='segment-name col-md-8'>\n" +
                "<div class='name'>\n" +
                "<h1 class='mb-0'>\n" +
                "<button class='btn btn-xs btn-icon-only btn-icon btn-unstyled starred' data-segment-id='740569'>\n" +
                "<span class=\"app-icon-wrapper  \"><span class=\"app-icon icon-star icon-lg icon-dark\"></span></span>\n" +
                "</button>\n" +
                "<span data-full-name='Rain Creek Fun Climb - LONG' id='js-full-name'>Rain Creek Fun Climb - LONG</span>\n" +
                "</h1>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class='segment-action col-md-4'>\n" +
                "</div>\n" +
                "<div class='col-md-8'>\n" +
                "<div class='location'>\n" +
                "<strong>Ride Segment</strong>\n" +
                "Austin, TX\n" +
                "</div>\n" +
                "<ul class='list-stats inline-stats stats-lg mt-md'>\n" +
                "<li><div class=\"stat\"><span class=\"stat-subtext\">Distance</span><b class=\"stat-text\">0.61<abbr class='unit' title='kilometers'>km</abbr></b></div></li>\n" +
                "<li><div class=\"stat\"><span class=\"stat-subtext\">Avg Grade</span><b class=\"stat-text\">9.7<abbr class='unit' title='percent'>%</abbr></b></div></li>\n" +
                "<li><div class=\"stat\"><span class=\"stat-subtext\">Lowest Elev</span><b class=\"stat-text\">204<abbr class='unit' title='meters'>m</abbr></b></div></li>\n" +
                "<li><div class=\"stat\"><span class=\"stat-subtext\">Highest Elev</span><b class=\"stat-text\">264<abbr class='unit' title='meters'>m</abbr></b></div></li>\n" +
                "<li><div class=\"stat\"><span class=\"stat-subtext\">Elev Difference</span><b class=\"stat-text\">59<abbr class='unit' title='meters'>m</abbr></b></div></li>\n" +
                "<li><div class=\"stat attempts\"><span class=\"stat-subtext\">13,294 Attempts By 2,233 People</span><b class=\"stat-text\"></b></div></li>\n" +
                "</ul>\n" +
                "\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class='row'>\n" +
                "<div class='col-md-8'>\n" +
                "<div class='map-container map-large' id='map_canvas'></div>\n" +
                "<div class='elevation-chart chart-container mb-sm mt-sm' id='chart-container'>\n" +
                "<div id='elev-chart'></div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class='sidebar col-md-4'>\n" +
                "<div data-react-class=\"SegmentDetailsSideBar\" data-react-props=\"{&quot;loggedIn&quot;:false,&quot;sideBarProps&quot;:{&quot;segmentId&quot;:740569,&quot;fastestTimes&quot;:{&quot;overall&quot;:{&quot;id&quot;:259263,&quot;name&quot;:&quot;Lawson Craddock&quot;,&quot;stats&quot;:[{&quot;label&quot;:&quot;KOM&quot;,&quot;value&quot;:&quot;1:16&quot;}],&quot;date&quot;:&quot;Jan 25, 2021&quot;,&quot;profile&quot;:&quot;https://dgalywyr863hv.cloudfront.net/pictures/athletes/259263/8482802/1/large.jpg&quot;,&quot;segmentEffortId&quot;:2788868943008474270,&quot;activityId&quot;:4680594591},&quot;women&quot;:{&quot;id&quot;:119668,&quot;name&quot;:&quot;Chuck Angrea F.&quot;,&quot;stats&quot;:[{&quot;label&quot;:&quot;QOM&quot;,&quot;value&quot;:&quot;2:00&quot;}],&quot;date&quot;:&quot;Aug 31, 2016&quot;,&quot;profile&quot;:&quot;https://d3nn82uaxijpm6.cloudfront.net/assets/avatar/athlete/large-800a7033cc92b2a5548399e26b1ef42414dd1a9cb13b99454222d38d58fd28ef.png&quot;,&quot;segmentEffortId&quot;:17069533442,&quot;activityId&quot;:696241539}},&quot;localLegend&quot;:{&quot;id&quot;:86665167,&quot;name&quot;:&quot;Tyson Nordmann&quot;,&quot;stats&quot;:[{&quot;label&quot;:&quot;Local Legend&quot;,&quot;value&quot;:&quot;19 efforts&quot;}],&quot;mayorEffortCount&quot;:19,&quot;profile&quot;:&quot;https://d3nn82uaxijpm6.cloudfront.net/assets/avatar/athlete/large-800a7033cc92b2a5548399e26b1ef42414dd1a9cb13b99454222d38d58fd28ef.png&quot;}}}\"></div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class='row' id='segment-leaderboard'>\n" +
                "<div class='col-sm-6'>\n" +
                "<h2 class='text-title1'>Overall Leaderboard</h2>\n" +
                "<table class='table table-striped table-leaderboard'>\n" +
                "<thead>\n" +
                "<tr>\n" +
                "<th>Rank</th>\n" +
                "<th class='name'>Name</th>\n" +
                "<th class='speed hidden-xs'>Speed</th>\n" +
                "<th class='power hidden-xs'>Power</th>\n" +
                "<th class='time last-child'>Time</th>\n" +
                "</tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>1</td>\n" +
                "<td>Lawson Craddock</td>\n" +
                "<td class='hidden-xs'>29.0<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "633<abbr class='unit' title='watts'>W</abbr>\n" +
                "<img class=\"power-meter\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/powermeter-45fa2f9f06528441cf847a5b702990fbba60cde11a1ef1716feec0cd46a0dd69.png\" />\n" +
                "</td>\n" +
                "<td><a href=\"/activities/4680594591\">1:16</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>2</td>\n" +
                "<td>Eli H.</td>\n" +
                "<td class='hidden-xs'>28.3<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "564<abbr class='unit' title='watts'>W</abbr>\n" +
                "<img class=\"power-meter\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/powermeter-45fa2f9f06528441cf847a5b702990fbba60cde11a1ef1716feec0cd46a0dd69.png\" />\n" +
                "</td>\n" +
                "<td><a href=\"/activities/2074181929\">1:18</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>3</td>\n" +
                "<td>Bryan M.</td>\n" +
                "<td class='hidden-xs'>26.9<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "560<abbr class='unit' title='watts'>W</abbr>\n" +
                "<img class=\"power-meter\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/powermeter-45fa2f9f06528441cf847a5b702990fbba60cde11a1ef1716feec0cd46a0dd69.png\" />\n" +
                "</td>\n" +
                "<td><a href=\"/activities/128277404\">1:22</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>4</td>\n" +
                "<td>Travis Turner</td>\n" +
                "<td class='hidden-xs'>24.5<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "595<abbr class='unit' title='watts'>W</abbr>\n" +
                "</td>\n" +
                "<td><a href=\"/activities/54156980\">1:30</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>5</td>\n" +
                "<td>&lt;         Brendo &gt;</td>\n" +
                "<td class='hidden-xs'>24.0<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "608<abbr class='unit' title='watts'>W</abbr>\n" +
                "</td>\n" +
                "<td><a href=\"/activities/94786400\">1:32</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>5</td>\n" +
                "<td>Tyson Nordmann</td>\n" +
                "<td class='hidden-xs'>24.0<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "-\n" +
                "</td>\n" +
                "<td><a href=\"/activities/5463380904\">1:32</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>7</td>\n" +
                "<td>Todd C.</td>\n" +
                "<td class='hidden-xs'>22.7<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "413<abbr class='unit' title='watts'>W</abbr>\n" +
                "</td>\n" +
                "<td><a href=\"/activities/64678396\">1:37</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>7</td>\n" +
                "<td>Kory W.</td>\n" +
                "<td class='hidden-xs'>22.7<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "487<abbr class='unit' title='watts'>W</abbr>\n" +
                "<img class=\"power-meter\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/powermeter-45fa2f9f06528441cf847a5b702990fbba60cde11a1ef1716feec0cd46a0dd69.png\" />\n" +
                "</td>\n" +
                "<td><a href=\"/activities/1868048271\">1:37</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>9</td>\n" +
                "<td>Jeff Stuesser</td>\n" +
                "<td class='hidden-xs'>21.8<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "477<abbr class='unit' title='watts'>W</abbr>\n" +
                "</td>\n" +
                "<td><a href=\"/activities/118259807\">1:41</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>9</td>\n" +
                "<td>Zeddy He</td>\n" +
                "<td class='hidden-xs'>21.8<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "528<abbr class='unit' title='watts'>W</abbr>\n" +
                "</td>\n" +
                "<td><a href=\"/activities/546235277\">1:41</a></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "\n" +
                "</div>\n" +
                "<div class='col-sm-6'>\n" +
                "<h2 class='text-title1'>Women&#39;s Leaderboard</h2>\n" +
                "<table class='table table-striped table-leaderboard'>\n" +
                "<thead>\n" +
                "<tr>\n" +
                "<th>Rank</th>\n" +
                "<th class='name'>Name</th>\n" +
                "<th class='speed hidden-xs'>Speed</th>\n" +
                "<th class='power hidden-xs'>Power</th>\n" +
                "<th class='time last-child'>Time</th>\n" +
                "</tr>\n" +
                "</thead>\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>1</td>\n" +
                "<td>Chuck Angrea F.</td>\n" +
                "<td class='hidden-xs'>18.4<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "293<abbr class='unit' title='watts'>W</abbr>\n" +
                "</td>\n" +
                "<td><a href=\"/activities/696241539\">2:00</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>1</td>\n" +
                "<td>Amy Antonio Stallion C.</td>\n" +
                "<td class='hidden-xs'>18.4<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "302<abbr class='unit' title='watts'>W</abbr>\n" +
                "</td>\n" +
                "<td><a href=\"/activities/696629660\">2:00</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>3</td>\n" +
                "<td>Erica &quot;Wrecka  H.</td>\n" +
                "<td class='hidden-xs'>16.7<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "282<abbr class='unit' title='watts'>W</abbr>\n" +
                "</td>\n" +
                "<td><a href=\"/activities/712684884\">2:12</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>4</td>\n" +
                "<td>Chicken R.</td>\n" +
                "<td class='hidden-xs'>15.8<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "261<abbr class='unit' title='watts'>W</abbr>\n" +
                "<img class=\"power-meter\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/powermeter-45fa2f9f06528441cf847a5b702990fbba60cde11a1ef1716feec0cd46a0dd69.png\" />\n" +
                "</td>\n" +
                "<td><a href=\"/activities/696248611\">2:20</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>4</td>\n" +
                "<td>MizKansas .</td>\n" +
                "<td class='hidden-xs'>15.8<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "279<abbr class='unit' title='watts'>W</abbr>\n" +
                "<img class=\"power-meter\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/powermeter-45fa2f9f06528441cf847a5b702990fbba60cde11a1ef1716feec0cd46a0dd69.png\" />\n" +
                "</td>\n" +
                "<td><a href=\"/activities/842651298\">2:20</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>6</td>\n" +
                "<td>Trahanimal .</td>\n" +
                "<td class='hidden-xs'>15.6<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "234<abbr class='unit' title='watts'>W</abbr>\n" +
                "<img class=\"power-meter\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/powermeter-45fa2f9f06528441cf847a5b702990fbba60cde11a1ef1716feec0cd46a0dd69.png\" />\n" +
                "</td>\n" +
                "<td><a href=\"/activities/1576995611\">2:21</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>7</td>\n" +
                "<td>Marla B.</td>\n" +
                "<td class='hidden-xs'>15.4<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "-\n" +
                "</td>\n" +
                "<td><a href=\"/activities/48665689\">2:23</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>8</td>\n" +
                "<td>Lauren V.</td>\n" +
                "<td class='hidden-xs'>15.2<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "316<abbr class='unit' title='watts'>W</abbr>\n" +
                "<img class=\"power-meter\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/powermeter-45fa2f9f06528441cf847a5b702990fbba60cde11a1ef1716feec0cd46a0dd69.png\" />\n" +
                "</td>\n" +
                "<td><a href=\"/activities/171256292\">2:25</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>9</td>\n" +
                "<td>Christie Tracy</td>\n" +
                "<td class='hidden-xs'>15.1<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "299<abbr class='unit' title='watts'>W</abbr>\n" +
                "<img class=\"power-meter\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/powermeter-45fa2f9f06528441cf847a5b702990fbba60cde11a1ef1716feec0cd46a0dd69.png\" />\n" +
                "</td>\n" +
                "<td><a href=\"/activities/3746687297\">2:26</a></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>10</td>\n" +
                "<td>J Mix</td>\n" +
                "<td class='hidden-xs'>15.0<abbr class='unit' title='kilometers per hour'>km/h</abbr></td>\n" +
                "<td class='hidden-xs text-nowrap'>\n" +
                "295<abbr class='unit' title='watts'>W</abbr>\n" +
                "<img class=\"power-meter\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/powermeter-45fa2f9f06528441cf847a5b702990fbba60cde11a1ef1716feec0cd46a0dd69.png\" />\n" +
                "</td>\n" +
                "<td><a href=\"/activities/29185595\">2:27</a></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class='alert alert-info'>\n" +
                "<p><a href='/register/free?cta=join-strava&amp;element=link&amp;placement=bottom&amp;source=segments_show'>Join Strava</a> to see the full leaderboard</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "\n" +
                "<footer id='global-footer'>\n" +
                "<div class='container-fluid'>\n" +
                "<div class='row footer-nav footer-row'>\n" +
                "<div class='footer-nav-wrapper'>\n" +
                "<div class='footer-nav-menu col-lg-2 col-md-3 col-sm-3 col-xs-6'>\n" +
                "<h4 tabindex='0'>\n" +
                "Our Community\n" +
                "</h4>\n" +
                "<ul class='footer-nav-submenu list-unstyled text-left'>\n" +
                "<li>\n" +
                "<a href='/about'>\n" +
                "About\n" +
                "</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href='/community-standards'>\n" +
                "Strava Community Standards\n" +
                "</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href='/careers'>\n" +
                "Careers\n" +
                "</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href='/pros'>\n" +
                "Pros on Strava\n" +
                "</a>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "<div class='footer-nav-menu col-lg-2 col-md-3 col-sm-3 col-xs-6'>\n" +
                "<h4 tabindex='0'>\n" +
                "Follow\n" +
                "</h4>\n" +
                "<ul class='footer-nav-submenu list-unstyled text-left'>\n" +
                "<li>\n" +
                "<a href='http://blog.strava.com/' target='_blank'>\n" +
                "Blog\n" +
                "</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href='https://www.facebook.com/Strava/' target='_blank'>\n" +
                "Facebook\n" +
                "</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href='https://instagram.com/strava' target='_blank'>\n" +
                "Instagram\n" +
                "</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href='http://twitter.com/strava' target='_blank'>\n" +
                "Twitter\n" +
                "</a>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "<div class='footer-nav-menu col-lg-2 col-md-3 col-sm-3 col-xs-6'>\n" +
                "<h4 tabindex='0'>\n" +
                "Support\n" +
                "</h4>\n" +
                "<ul class='footer-nav-submenu list-unstyled text-left'>\n" +
                "<li>\n" +
                "<a href='https://strava.zendesk.com/entries/61608110-How-to-contact-Strava-Support-Submit-a-ticket'>\n" +
                "Contact\n" +
                "</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href='https://strava.zendesk.com/entries/46363890-About-Strava-Maps'>\n" +
                "Community Forum\n" +
                "</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href='https://strava.zendesk.com/entries/46363890-About-Strava-Maps'>\n" +
                "About Our Maps\n" +
                "</a>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "<div class='footer-nav-menu col-lg-2 col-md-3 col-sm-3 col-xs-6 visible-lg-block'>\n" +
                "<h4>\n" +
                "<a href='https://business.strava.com'>\n" +
                "Business\n" +
                "</a>\n" +
                "</h4>\n" +
                "</div>\n" +
                "<div class='footer-nav-menu col-lg-2 col-md-3 col-sm-3 col-xs-6 visible-lg-block'>\n" +
                "<h4>\n" +
                "<a href='/mobile'>\n" +
                "Mobile\n" +
                "</a>\n" +
                "</h4>\n" +
                "</div>\n" +
                "<div class='footer-nav-menu col-lg-2 col-md-3 col-sm-3 col-xs-6 visible-lg-block'>\n" +
                "<h4>\n" +
                "<a href='/premium'>\n" +
                "Subscription\n" +
                "</a>\n" +
                "</h4>\n" +
                "</div>\n" +
                "<div class='footer-nav-menu col-md-3 col-sm-3 col-xs-6 hidden-lg'>\n" +
                "<h4 class='more'>\n" +
                "<span class=\"app-icon-wrapper  \"><span class=\"app-icon icon-ellipsis icon-xl\">More</span></span>\n" +
                "</h4>\n" +
                "<ul class='footer-nav-submenu list-unstyled text-left'>\n" +
                "<li>\n" +
                "<a href='/mobile'>\n" +
                "Mobile\n" +
                "</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href='/premium'>\n" +
                "Subscription\n" +
                "</a>\n" +
                "</li>\n" +
                "<li>\n" +
                "<a href='https://business.strava.com'>\n" +
                "Business\n" +
                "</a>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class='row footer-other'>\n" +
                "<div class='col-md-12'>\n" +
                "<div class='privacy-terms'>\n" +
                "<a href='/legal/privacy'>\n" +
                "Privacy Policy\n" +
                "</a>\n" +
                "|\n" +
                "<a href='/legal/terms'>\n" +
                "Terms and Conditions\n" +
                "</a>\n" +
                "</div>\n" +
                "<div class='copyright'>\n" +
                "<p>© 2021 Strava</p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</footer>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/mapbox-04f75a62080d3f801750d3a4bb3973962d34f816d78f8997797210e678695808.js\"></script>\n" +
                "<script>\n" +
                "  window._maps_api = \"pk.eyJ1Ijoic3RyYXZhIiwiYSI6IlpoeXU2U0UifQ.c7yhlZevNRFCqHYm6G6Cyg\"\n" +
                "  jQuery(document).ready(function(){\n" +
                "    // TODO: no need for locale\n" +
                "    Strava.Maps.Mapbox.Base.setMapIds({\"terrain_id\":\"mapbox/dark-v10\",\"satellite_id\":\"mapbox/satellite-v9\",\"standard_id\":\"strava/ck2gt6oil0c7y1cnvlz1uphnu\"});\n" +
                "  });\n" +
                "</script>\n" +
                "<script id='lightbox-template' type='text/template'>\n" +
                "<div class='lightbox-window modal-content'>\n" +
                "<div class='close-lightbox'>\n" +
                "<button class='btn btn-unstyled btn-close'>\n" +
                "<div class='app-icon icon-close icon-xs icon-white'></div>\n" +
                "</button>\n" +
                "</div>\n" +
                "</div>\n" +
                "</script>\n" +
                "<script id='popover-template' type='text/template'>\n" +
                "<div class='popover'></div>\n" +
                "</script>\n" +
                "<script>\n" +
                "  window._asset_host = \"https://d3nn82uaxijpm6.cloudfront.net\";\n" +
                "  window._measurement_preference = \"meters\";\n" +
                "  window._date_preference = \"%m/%d/%Y\";\n" +
                "  window._datepicker_preference_format = \"mm/dd/yy\";\n" +
                "  \n" +
                "  jQuery(document).ready(function() {\n" +
                "    Strava.Util.EventLogging.createInstance(\"https://analytics.strava.com\",\"7215fa60b5f01ecc3967543619f7e3d9\", null);\n" +
                "  });\n" +
                "</script>\n" +
                "<script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava/i18n/locales/en-US-313e5a8461f12e2606812a6e1363ff33661ec2421a695217be495415c6931ebf.js\"></script>\n" +
                "<script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/application-d3053c351c87aec78aff8255ce329a35c9c3a3916646079e6ddb52c21d8d8bf1.js\"></script>\n" +
                "\n" +
                "<script src=\"https://www.strava.com/cookie-banner\"></script>\n" +
                "<script>\n" +
                "  jQuery(document).ready(function(){\n" +
                "    typeof StravaCookieBanner !== 'undefined' && StravaCookieBanner.render();\n" +
                "  });\n" +
                "</script>\n" +
                "\n" +
                "\n" +
                "<div id='fb-root'></div>\n" +
                "<script>\n" +
                "  window.fbAsyncInit = function() {\n" +
                "    FB.init({\n" +
                "      appId: \"284597785309\",\n" +
                "      status: true,\n" +
                "      cookie: true,\n" +
                "      xfbml: true,\n" +
                "      version: \"v3.2\"\n" +
                "    });\n" +
                "    Strava.Facebook.PermissionsManager.getInstance().facebookReady();\n" +
                "    jQuery('#fb-root').trigger('facebook:init');\n" +
                "  };\n" +
                "  (function(d){\n" +
                "    var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];\n" +
                "    if (d.getElementById(id)) {return;}\n" +
                "    js = d.createElement('script'); js.id = id; js.async = true;\n" +
                "    js.src = \"//connect.facebook.net/en_US/sdk.js\";\n" +
                "    ref.parentNode.insertBefore(js, ref);\n" +
                "  }(document));\n" +
                "</script>\n" +
                "\n" +
                "\n" +
                "<script>\n" +
                "  jQuery('document').ready(function() {\n" +
                "    setTimeout(function() {\n" +
                "      jQuery('#face-pile').css('visibility', 'visible');\n" +
                "    }, 300);\n" +
                "  });\n" +
                "  var ua = navigator.userAgent;\n" +
                "  var checker = {\n" +
                "    ios: ua.match(/(iPhone|iPod|iPad)/),\n" +
                "    android: ua.match(/Android/)\n" +
                "  };\n" +
                "  \n" +
                "  var regButton = jQuery('#get-started-button');\n" +
                "  if (checker.android || checker.ios) {\n" +
                "    regUrl = regButton.attr('href');\n" +
                "    if (checker.android) {\n" +
                "      regUrl = 'https://market.android.com/details?id=com.strava';\n" +
                "    } else if (checker.ios) {\n" +
                "      regUrl = 'http://itunes.apple.com/app/strava-cycling/id426826309';\n" +
                "    }\n" +
                "    regButton.attr('href', regUrl).text('Get the App');\n" +
                "  }\n" +
                "  regButton.css('visibility', 'visible');\n" +
                "</script>\n" +
                "<script>\n" +
                "  var currentAthlete = new Strava.Models.CurrentAthlete({\"logged_in\":false});\n" +
                "  HAML.globals = function() {\n" +
                "    return {\n" +
                "      currentAthlete: currentAthlete,\n" +
                "      renderPartial: function(name, context) {\n" +
                "        if (context == null) {\n" +
                "          context = this;\n" +
                "        }\n" +
                "        return JST[name](context);\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "</script>\n" +
                "\n" +
                "<script>\n" +
                "  new Strava.Initializer();\n" +
                "</script>\n" +
                "<script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava/maps/mapbox/manifest-4c3d9e8417e78e497f9ae969774af73af9e1a2fc36b4a555d06b60a9aacbf53f.js\"></script>\n" +
                "<script src=\"https://d3nn82uaxijpm6.cloudfront.net/packs/js/segments-5b6fe50df3219d5ce7b4.chunk.js\"></script>\n" +
                "<script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava/segments/manifest-2cd1a0531d1e1c72bad52a75a2e90f9575398d694407c1739493550d8e972956.js\"></script>\n" +
                "<script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava/segments/history/manifest-31cd8f62449bb3f7c0241666000452997e7e7b7f9458ea2d44ae4805b4a926e3.js\"></script>\n" +
                "<script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava/directory/manifest-5cc467395d7ea4e8c5100545f2d51fa1eb72477e9b0ad32291d3be507f2dbd7c.js\"></script>\n" +
                "<script>\n" +
                "  jQuery(document).ready(function() {\n" +
                "    Strava.ExternalAnalytics.trackV2({\n" +
                "      category: 'segments',\n" +
                "      page: 'segment_detail_compare_efforts_upsell',\n" +
                "      action: 'screen_enter',\n" +
                "      properties: {\n" +
                "        segment_id: 740569,\n" +
                "      }\n" +
                "    });\n" +
                "    jQuery('#recent-efforts-cta').on('click', function() {\n" +
                "      Strava.ExternalAnalytics.trackV2({\n" +
                "        category: 'segments',\n" +
                "        page: 'segment_detail_compare_efforts_upsell',\n" +
                "        action: 'click',\n" +
                "        element: 'subscribe_button',\n" +
                "        properties: {\n" +
                "          segment_id: 740569\n" +
                "        }\n" +
                "      });\n" +
                "    });\n" +
                "  });\n" +
                "</script>\n" +
                "<script>\n" +
                "  jQuery(document).ready(function() {\n" +
                "    jQuery('#compare-efforts-cta').on('click', function() {\n" +
                "      Strava.ExternalAnalytics.trackV2({\n" +
                "        category: 'segments',\n" +
                "        page: 'segment_detail',\n" +
                "        action: 'click',\n" +
                "        element: 'compare_efforts',\n" +
                "        properties: {\n" +
                "          segment_id: 740569\n" +
                "        }\n" +
                "      });\n" +
                "    });\n" +
                "  \n" +
                "    new Strava.Segments.Initializer({\n" +
                "      segmentId: 740569,\n" +
                "      segmentName: \"Rain Creek Fun Climb - LONG\",\n" +
                "      segmentHazard: false,\n" +
                "      segmentHazardWaived: false,\n" +
                "      showWaiver: false,\n" +
                "      canStarSegment: false\n" +
                "    });\n" +
                "  });\n" +
                "</script>\n" +
                "<script>\n" +
                "  if ('serviceWorker' in navigator) {\n" +
                "    window.addEventListener('load', function() {\n" +
                "      navigator.serviceWorker.register(\"/service_worker.js?v=dLlWydWlG8\").then(function(registration) {\n" +
                "      }, function(err) {\n" +
                "        console.log('ServiceWorker registration failed: ', err);\n" +
                "      });\n" +
                "    });\n" +
                "  }\n" +
                "</script>\n" +
                "<script>\n" +
                "  jQuery(document).ready(function() {\n" +
                "    // Scroll Tracking\n" +
                "    jQuery(document).one('scroll', function(){\n" +
                "      Strava.ExternalAnalytics.trackV2({\n" +
                "        category: 'page_scrolled'\n" +
                "      });\n" +
                "    });\n" +
                "  });\n" +
                "</script>\n" +
                "<script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava/ui/views/SmartbannerOrionView-14369f065f3110607a3aec2fd1775faf1804cc5c5857ef5790a321e0f190d8e8.js\"></script>\n" +
                "<script>\n" +
                "  jQuery(document).ready(function() {\n" +
                "    new Strava.Ui.Views.SmartbannerOrionView();\n" +
                "  });\n" +
                "</script>\n" +
                "<script>\n" +
                "  // Mobile Menu transition handler\n" +
                "  jQuery('.collapsable-nav #container-nav')\n" +
                "    .on('show.bs.collapse', function(){\n" +
                "      jQuery('.smartbanner-content').slideUp(100);\n" +
                "      jQuery('html').addClass('mobile-menu-open');\n" +
                "    })\n" +
                "    .on('hidden.bs.collapse', function(){\n" +
                "      jQuery('.smartbanner-content').slideDown(100);\n" +
                "      jQuery('html').removeClass('mobile-menu-open');\n" +
                "    });\n" +
                "</script>\n" +
                "\n" +
                "<script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/bootstrap.min-55483ca093070244e24730190b707a18467cb78d3262a0133d34b80fc82c8636.js\"></script>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n");
    }

    public HttpRequestResponse getValidRunningLeaderboardResponse() {
        return new HttpRequestResponse(0, "", "<!-- Orion-App Layout --><!DOCTYPE html><html class='logged-out responsive feed3p0 old-login strava-orion responsive' dir='ltr' lang='en-US' xmlns:fb='http://www.facebook.com/2008/fbml' xmlns:og='http://opengraphprotocol.org/schema/' xmlns='http://www.w3.org/TR/html5'><!--layout orion app--><head><meta charset='UTF-8'><meta content='width = device-width, initial-scale = 1, maximum-scale = 1' name='viewport'><style type='text/css'>.spinner, .spinner .status {  position: relative;}.spinner {  margin-top: 1em;  margin-bottom: 1em;}.spinner .status {  top: 2px;  margin-left: 0.5em;}.spinner .status:empty {  display: none;}.spinner.lg .graphic {  border-width: 3px;  height: 32px;  width: 32px;}.spinner.tiny {  height: 10px;  width: 10px;}.spinner.centered, .spinner.vcentered {  box-sizing: border-box;  width: 100%;}.spinner.vcentered {  left: 0;  margin-top: -12px;  position: absolute;  right: 0;  text-align: center;  top: 50%;}.spinner .graphic, .ajax-loading-image {  animation: spin 1.2s infinite linear;  box-sizing: border-box;  border-color: #eee;  border-radius: 50%;  border-style: solid;  border-top-color: #666;  border-top-style: solid;  border-width: 2px;  content: \"\";  display: inline-block;  height: 20px;  position: relative;  vertical-align: middle;  width: 20px;}@keyframes spin {  from {    transform: rotate(0deg);  }  to {    transform: rotate(359deg);  }}</style><link rel=\"stylesheet\" media=\"screen\" href=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava-app-icons-3c700abaef13ef589985c6f87b2b337dd65cd5ac974c579f81af520a6c645fc2.css\" /><link rel=\"stylesheet\" media=\"screen\" href=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava-orion-4783bef7f9985616cf3b1e3b54ad34b1787a18699058391a8a8404ab658f03e9.css\" /><link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-180x180.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='180x180'><link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-152x152.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='152x152'><link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-144x144.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='144x144'><link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-120x120.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='120x120'><link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-114x114.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='114x114'><link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-76x76.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='76x76'><link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-72x72.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='72x72'><link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-60x60.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='60x60'><link href='https://d3nn82uaxijpm6.cloudfront.net/apple-touch-icon-57x57.png?v=dLlWydWlG8' rel='apple-touch-icon' sizes='57x57'><link href='https://d3nn82uaxijpm6.cloudfront.net/favicon-32x32.png?v=dLlWydWlG8' rel='icon' sizes='32x32' type='image/png'><link href='https://d3nn82uaxijpm6.cloudfront.net/icon-strava-chrome-192.png?v=dLlWydWlG8' rel='icon' sizes='192x192' type='image/png'><link href='https://d3nn82uaxijpm6.cloudfront.net/favicon-96x96.png?v=dLlWydWlG8' rel='icon' sizes='96x96' type='image/png'><link href='https://d3nn82uaxijpm6.cloudfront.net/favicon-16x16.png?v=dLlWydWlG8' rel='icon' sizes='16x16' type='image/png'><link href='/manifest.json?v=dLlWydWlG8' rel='manifest'><meta content='#FC5200' name='msapplication-TileColor'><meta content='https://d3nn82uaxijpm6.cloudfront.net/mstile-144x144.png?v=dLlWydWlG8' name='msapplication-TileImage'><meta content='#FC5200' name='theme-color'><meta content='Strava' name='apple-mobile-web-app-title'><meta content='Strava' name='application-name'><meta content='yes' name='apple-mobile-web-app-capable'><meta content='black' name='apple-mobile-web-app-status-bar-style'><script type='application/ld+json'>{  \"@context\": \"http://schema.org\",  \"@type\": \"Organization\",  \"name\": \"Strava\",  \"url\": \"https://www.strava.com/\",  \"logo\": \"https://d3nn82uaxijpm6.cloudfront.net/assets/brands/strava/logo-strava-lg-5671105ffddb86e437bb68503a4973e8bf07e2a41c0b332d3e3bced21d537e98.png\",  \"sameAs\": [    \"https://facebook.com/Strava\",    \"https://twitter.com/strava\",    \"https://instagram.com/strava\",    \"https://youtube.com/stravainc\",    \"https://blog.strava.com\",    \"https://github.com/strava\",    \"https://medium.com/strava-engineering\"  ]}</script><meta name=\"csrf-param\" content=\"authenticity_token\" /><meta name=\"csrf-token\" content=\"HQoC5opJPqFeiMJilK6ixBUbVkLFiCyV4OSiMk4HGIGchm+EP08RuBGNhGvK7W78iXhrO3HT/afdUkOmwU2uRw==\" /><script src=\"https://d3nn82uaxijpm6.cloudfront.net/packs/js/chunking_runtime-51cdbf03f1053b16e02e.js\"></script><link rel=\"stylesheet\" media=\"screen\" href=\"https://d3nn82uaxijpm6.cloudfront.net/packs/css/global-bf8d3a4b.chunk.css\" /><script src=\"https://d3nn82uaxijpm6.cloudfront.net/packs/js/global-1370b3a44b6063ad272f.chunk.js\"></script><script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava-head-d0f4c1f1472bbfd71048d6bbbe5e3b9041ec1330343ab67cfc3a6099b8fee09e.js\"></script><link rel=\"stylesheet\" media=\"screen\" href=\"https://d3nn82uaxijpm6.cloudfront.net/assets/segments/show-deb34c5603f8040e3db057abb7c6464cb9e3a5eb5e7f3fd39545e353f4ccee2a.css\" /><link rel=\"stylesheet\" media=\"screen\" href=\"https://d3nn82uaxijpm6.cloudfront.net/packs/css/segments-2b4164d4.chunk.css\" /><title>Damn Hill Repeat Segment | Strava Run Segment in Cedar Park, TX</title><meta content='View bike ride segment, 0.6 kilometers long, starting in Cedar Park, TX, with 0 meters in elevation gain.' type='description'><link href='https://www.strava.com/segments/3388435' rel='canonical'><link href='https://www.strava.com/segments/3388435' rel='canonical'><link href='https://www.strava.com/segments/3388435' hreflang='x-default' rel='alternate'><link href='https://www.strava.com/segments/3388435' hreflang='en-US' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=en-GB' hreflang='en-GB' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=fr-FR' hreflang='fr-FR' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=de-DE' hreflang='de-DE' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=pt-BR' hreflang='pt-BR' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=es-ES' hreflang='es-ES' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=it-IT' hreflang='it-IT' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=ru-RU' hreflang='ru-RU' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=es-419' hreflang='es-419' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=ja-JP' hreflang='ja-JP' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=ko-KR' hreflang='ko-KR' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=nl-NL' hreflang='nl-NL' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=zh-TW' hreflang='zh-TW' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=pt-PT' hreflang='pt-PT' rel='alternate'><link href='https://www.strava.com/segments/3388435?hl=zh-CN' hreflang='zh-CN' rel='alternate'><script>  !function(options){    window.Strava = window.Strava || {};    var _enabled = true;    var _options = options;    var _snowplowReady = null;      window.Strava.ExternalAnalytics = window.Strava.ExternalAnalytics || (      {        isEnabled: function() {          return _enabled;        },        isDebug: function() {          return _options.debug;        },        track: function() {        },        trackV2: function(event) {          var eventData = {            'category': event.category,            'page': event.page,            'action': event.action,            'element': event.element || null,            'properties': event.properties || {}          }          if (this.isEnabled()) {            snowplow('trackSelfDescribingEvent', {              schema: 'iglu:com.strava/track/jsonschema/1-0-0',              data: eventData            });          } else {            !!console.table && console.table(eventData);          }        },        trackLink: function() {        },        trackForm: function() {        },        identifyV2: function () {        },        page: function(pageProperties) {          if(this.isEnabled()) {            snowplow('trackPageView');          }        },        identify: function(athleteId, options, eventName) {          if (this.isEnabled()) {            var properties = options || {}            properties.athlete_id = athleteId;            var eventData = {              'category': 'identify',              'page': null,              'action': eventName,              'element': null,              'properties': properties            };            snowplow('trackSelfDescribingEvent', {              schema: 'iglu:com.strava/track/jsonschema/1-0-0',              data: eventData            });          }        },        reset: function() {          if(this.isEnabled()) {            snowplow('setUserId', null)            var spCookie = document.cookie.match('_sp_id\\\\.[a-f0-9]+')            if(spCookie != null) {              document.cookie = spCookie[0] + \"= ; expires = Thu, 01 Jan 1970 00:00:00 GMT\"            }          }        },        setupSnowplow: function(id) {          if(this.isEnabled()) {            snowplow(\"newTracker\", \"cf\", \"c.strava.com\", {              appId: \"strava-web\",              platform: \"web\"            });            snowplow('setUserId', id);            snowplow('enableFormTracking');          }        },        getDomainUserId: function() {          var d = jQuery.Deferred();          if (this.isEnabled()) {            if (!_snowplowReady) {              _snowplowReady = jQuery.Deferred();              snowplow(function(){                _snowplowReady.resolve(this.cf.getDomainUserId());              });            }            _snowplowReady.always(function(getDomainUserId){              d.resolve(getDomainUserId);            });          } else {            d.reject(null);          }          return d;        },        log: function(message, values) {          if(this.isDebug()) {            console.log(message, 'background-color: yellow; color: blue; font-size: medium;', values);          }        },        debug: function(value) {          _options.debug = value;        }      }    )  }({    is_mobile: false,    os: \"\",    debug: false,    athlete_id: null,    locale: \"en-US\"  });</script><script>  !function(){    var analytics = window.analytics = window.analytics || [];    if(analytics.invoked) {      window.console && console.error && console.error(\"Segment snippet included twice.\");    } else {      (function(p,l,o,w,i,n,g){if(!p[i]){p.GlobalSnowplowNamespace=p.GlobalSnowplowNamespace||[];p.GlobalSnowplowNamespace.push(i);p[i]=function(){(p[i].q=p[i].q||[]).push(arguments)};p[i].q=p[i].q||[];n=l.createElement(o);g=l.getElementsByTagName(o)[0];n.async=1;n.src=w;g.parentNode.insertBefore(n,g)}}(window,document,\"script\",\"https://dy9z4910shqac.cloudfront.net/1oG5icild0laCtJMi45LjA.js\",\"snowplow\"));      Strava.ExternalAnalytics.setupSnowplow();        Strava.ExternalAnalytics.page(null);    }  }();</script><script>  !function(debug){    window.Strava = window.Strava || {};    var _enabled = false;    var _debug = !!debug;    var _branchData = null;      window.Strava.BranchIO = window.Strava.BranchIO || (      {        isEnabled: function() {          return _enabled;        },        isDebug: function() {          return _debug;        },        dataToLocalStorage: function() {          if (!_branchData) {            _branchData = new Strava.BranchAnalytics.BranchData();          }            var d = this.data()          var that = this;          d.done(function(data) {            that.log('storing data %o to local storage', data)            _branchData.data(data)          });          d.fail(function(message) {            that.log('failed to retrieve data from branch');            _branchData.data({})          });          return d;        },        createLink: function(options) {          var d = jQuery.Deferred();          var data = null;          const that = this;          var callback = function(e, l) {            if (!e) {              d.resolve(l);            } else {              d.reject(e);            }          }          if (options.peek_data) {            data = this.dataFromLocalStorage();            if (data && data.data_parsed && data.data_parsed['~referring_link']) {              d.resolve(data.data_parsed['~referring_link']);            } else {              d.reject();            }          } else {              Strava.ExternalAnalytics              .getDomainUserId()              .always(function(domainUserId){                if (domainUserId) {                  options.data['domainUserId'] = domainUserId;                }                  if(that.isEnabled()) {                  branch.link(options, callback);                };            });          }          return d;        },        dataFromLocalStorage: function() {          if (!_branchData) {            _branchData = new Strava.BranchAnalytics.BranchData();          }          return _branchData.data();        },        clearLocalStorage: function() {          if (!_branchData) {            _branchData = new Strava.BranchAnalytics.BranchData();          }          _branchData.data({});        },        data: function(checkLocalStorage) {          var d = jQuery.Deferred();          var that = this;          var c = function(message, meta_data) {            var storedData = null;              if(message) {              d.reject(message);            } else {              if (checkLocalStorage == true && (meta_data == null || meta_data.data == \"\" || meta_data.data == null)) {                storedData = that.dataFromLocalStorage();                that.clearLocalStorage();                  d.resolve(storedData);              } else {                d.resolve(meta_data);              }            }          };            if(this.isEnabled()) {            branch.data(c);            this.log('%cdata (branch enabled)');          } else {            this.log('%cdata (branch disabled)');            d.resolve({});          }          return d;        },        identify: function(athleteId) {          var callback = function(error, data) {            if (error) {              console.log(error);            }          }          if(this.isEnabled()) {            branch.setIdentity(athleteId, callback);          }        },        track: function(eventName, metaData) {          var callback = function(error, data) {            if (error) {              console.log(error);            }          }          if(this.isEnabled()) {            branch.track(eventName, metaData, callback);          }        },        log: function(message, values) {          if(this.isDebug()) {            console.log(message, 'background-color: yellow; color: blue; font-size: medium;', values);          }        },        debug: function(value) {          _debug = value;        }      }    )  }(false);</script></head><body><link rel=\"stylesheet\" media=\"screen\" href=\"https://d3nn82uaxijpm6.cloudfront.net/assets/common/smartbanner_orion-2d815ff185df733b835fcc83c93080835593cb5aa031e983fbc59b3e5f8244bc.css\" /><div class='container smartbanner-content pt-md pb-md' id='smartbanner-orion'><div class='row'><div class='col-xs-12'><img class=\"app-icon\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/activities/icon-ios-app-fdd1ed3da3ce334990af99517dc8f735832f6e5a3bb97feff3abdbd091caffba.svg\" /><div class='app-info mt-xs'><div class='app-name'>Strava</div><div class='app-subtitle'>Free app for Android and iPhone</div></div><div class='text-right mt-xs'><a href=\"https://www.strava.com/mobile\" class=\"btn btn-primary btn-outline btn-cta text-uppercase\" role=\"button\">Download</a></div></div></div></div><header id='global-header'><!--deploy: 88b2f6e354ceedda72e421458b0d42e0e22329fc--><!--[if lte IE 8]><div class='alert alert-warning message warning mb-0 text-center'><p>It looks like you're using a version of Internet Explorer that Strava no longer supports. Please <a href='http://www.microsoft.com/en-us/download/ie.aspx?q=internet+explorer'>upgrade your web browser</a> &mdash; <a href='https://strava.zendesk.com/entries/20420212-Supported-Browsers-on-Strava'>Learn more</a>.</p></div><![endif]--><nav class='nav-bar container collapsable-nav' role='navigation'><div title=\"Return to the Strava home page\" class=\"branding\"><a class=\"branding-content\" href=\"/\"><span class=\"sr-only\">Strava</span></a></div><!-- / Nav Menu Button --><a href=\"#container-nav\" aria-expanded=\"false\" aria-controls=\"container-nav\" data-toggle=\"collapse\" class=\"btn btn-default btn-mobile-menu\" role=\"button\">Menu</a><div class='nav-container collapse' id='container-nav'><ul class='user-nav nav-group'><li class='nav-item'><a class=\"nav-link\" href=\"/mobile\">Mobile</a></li><li class='nav-item'><a class=\"nav-link\" href=\"/features\">Features</a></li><li class='nav-item'><a class=\"nav-link\" href=\"/premium?cta=premium&amp;element=nav&amp;source=global-header\">Subscription</a></li><li class='nav-item'><a class=\"nav-link\" href=\"http://blog.strava.com/?utm_source=website-header\">Blog</a></li><li class='nav-object-group'><div class='nav-item logged_out_nav'><a class='btn btn-primary btn-signup' data-segioevent='{&quot;name&quot;:&quot;Signup Flow&quot;,&quot;method&quot;:&quot;web&quot;,&quot;source&quot;:&quot;Global Header&quot;}' href='/register?cta=sign-up&amp;element=global-header&amp;plan=free&amp;source=segments_show'>Sign Up</a></div><div class='nav-item logged_out_nav'><a class='btn btn-default btn-login' data-segioevent='{&quot;name&quot;:&quot;Login Flow&quot;,&quot;method&quot;:&quot;web&quot;,&quot;source&quot;:&quot;Global Header&quot;}' href='https://www.strava.com/login?cta=log-in&amp;element=global-header&amp;source=segments_show'>Log In</a></div></li></ul></div></nav></header><!-- / CF todo: remove old privacy templates after 'map-visibility' feature is rolled out --><script id='custom-map-controls-show-privacy-fullscreen-template' type='text/template'><div id='map-control-container' style='padding: 5px'><div class='js-map-control' id='map-control-container' index='1'><div class='inline-inputs' id='strava-map-controls'><div class='drop-down-menu' id='map-type-control'><a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a><ul class='options'><li><a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a></li><li><a id='start-street-view'>Street View (Start)</a></li><li><a id='end-street-view'>Street View (End)</a></li><li><label><input id='privacy_toggle' type='checkbox'>Show Privacy Zone</input></label></li></ul></div><a class='button' id='toggle-fullscreen'></a></div></div></div></script><script id='custom-map-controls-suggest-privacy-fullscreen-template' type='text/template'><div id='map-control-container' style='padding: 5px'><div class='js-map-control' id='map-control-container' index='1'><div class='inline-inputs' id='strava-map-controls'><div class='drop-down-menu' id='map-type-control'><a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a><ul class='options'><li><a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a></li><li><a id='start-street-view'>Street View (Start)</a></li><li><a id='end-street-view'>Street View (End)</a></li><li><a href='/settings/privacy'>Add Privacy Zone</a></li></ul></div><a class='button' id='toggle-fullscreen'></a></div></div></div></script><script id='custom-map-controls-show-privacy-template' type='text/template'><div id='map-control-container' style='padding: 5px'><div class='js-map-control' id='map-control-container' index='1'><div class='inline-inputs' id='strava-map-controls'><div class='drop-down-menu' id='map-type-control'><a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a><ul class='options'><li><a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a></li><li><a id='start-street-view'>Street View (Start)</a></li><li><a id='end-street-view'>Street View (End)</a></li><li><label><input id='privacy_toggle' type='checkbox'>Show Privacy Zone</input></label></li></ul></div></div></div></div></script><script id='custom-map-controls-suggest-privacy-template' type='text/template'><div id='map-control-container' style='padding: 5px'><div class='js-map-control' id='map-control-container' index='1'><div class='inline-inputs' id='strava-map-controls'><div class='drop-down-menu' id='map-type-control'><a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a><ul class='options'><li><a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a></li><li><a id='start-street-view'>Street View (Start)</a></li><li><a id='end-street-view'>Street View (End)</a></li><li><a href='/settings/privacy'>Add Privacy Zone</a></li></ul></div></div></div></div></script><script id='custom-map-controls-fullscreen-template' type='text/template'><div id='map-control-container' style='padding: 5px'><div class='js-map-control' id='map-control-container' index='1'><div class='inline-inputs' id='strava-map-controls'><div class='drop-down-menu' id='map-type-control'><a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a><ul class='options'><li><a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a></li><li><a id='start-street-view'>Street View (Start)</a></li><li><a id='end-street-view'>Street View (End)</a></li></ul></div><a class='button' id='toggle-fullscreen'></a></div></div></div></script><script id='custom-map-controls-template' type='text/template'><div id='map-control-container' style='padding: 5px'><div class='js-map-control' id='map-control-container' index='1'><div class='inline-inputs' id='strava-map-controls'><div class='drop-down-menu' id='map-type-control'><a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a><ul class='options'><li><a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a></li><li><a id='start-street-view'>Street View (Start)</a></li><li><a id='end-street-view'>Street View (End)</a></li></ul></div></div></div></div></script><script id='custom-map-controls-show-privacy-legend-fullscreen-template' type='text/template'><div id='map-control-container' style='padding: 5px'><div class='js-map-control' id='map-control-container' index='1'><div class='inline-inputs' id='strava-map-controls'><div class='drop-down-menu' id='map-type-control'><a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a><ul class='options'><li><a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a></li><li><a id='start-street-view'>Street View (Start)</a></li><li><a id='end-street-view'>Street View (End)</a></li></ul></div><a class='button' id='toggle-fullscreen'></a></div><div id='privacy-legend'><div class='legend-options'><div class='legend-line' id='visible-line'></div><div class='legend-label'>Visible</div></div><div class='legend-options'><div class='legend-line' id='hidden-line'></div><div class='legend-label'>Hidden</div></div></div></div></div></script><script id='custom-map-controls-show-privacy-legend-template' type='text/template'><div id='map-control-container' style='padding: 5px'><div class='js-map-control' id='map-control-container' index='1'><div class='inline-inputs' id='strava-map-controls'><div class='drop-down-menu' id='map-type-control'><a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a><ul class='options'><li><a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a></li><li><a id='start-street-view'>Street View (Start)</a></li><li><a id='end-street-view'>Street View (End)</a></li></ul></div></div><div id='privacy-legend'><div class='legend-options'><div class='legend-line' id='visible-line'></div><div class='legend-label'>Visible</div></div><div class='legend-options'><div class='legend-line' id='hidden-line'></div><div class='legend-label'>Hidden</div></div></div></div></div></script><script id='custom-map-controls-pzones-admin-fullscreen-template' type='text/template'><div id='map-control-container' style='padding: 5px'><div class='js-map-control' id='map-control-container' index='1'><div class='inline-inputs' id='strava-map-controls'><div class='drop-down-menu' id='map-type-control'><a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a><ul class='options'><li><a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a></li><li><a id='start-street-view'>Street View (Start)</a></li><li><a id='end-street-view'>Street View (End)</a></li><li><label><input id='privacy_toggle' type='checkbox'>Show Privacy Zone</input></label></li></ul></div><a class='button' id='toggle-fullscreen'></a></div><div id='privacy-legend'><div class='legend-options'><div class='legend-line' id='visible-line'></div><div class='legend-label'>Visible</div></div><div class='legend-options'><div class='legend-line' id='hidden-line'></div><div class='legend-label'>Hidden</div></div></div></div></div></script><script id='custom-map-controls-pzones-admin-template' type='text/template'><div id='map-control-container' style='padding: 5px'><div class='js-map-control' id='map-control-container' index='1'><div class='inline-inputs' id='strava-map-controls'><div class='drop-down-menu' id='map-type-control'><a class='selection' data-map-type-id='standard' id='selected-map'>Standard Map</a><ul class='options'><li><a class='map-type-selector' data-map-type-id='satellite'>Satellite Map</a></li><li><a id='start-street-view'>Street View (Start)</a></li><li><a id='end-street-view'>Street View (End)</a></li><li><label><input id='privacy_toggle' type='checkbox'>Show Privacy Zone</input></label></li></ul></div></div><div id='privacy-legend'><div class='legend-options'><div class='legend-line' id='visible-line'></div><div class='legend-label'>Visible</div></div><div class='legend-options'><div class='legend-line' id='hidden-line'></div><div class='legend-label'>Hidden</div></div></div></div></div></script><div class='container'><div class='section row' id='segment'><div class='segment-heading col-md-12'><div class='segment-name col-md-8'><div class='name'><h1 class='mb-0'><button class='btn btn-xs btn-icon-only btn-icon btn-unstyled starred' data-segment-id='3388435'><span class=\"app-icon-wrapper  \"><span class=\"app-icon icon-star icon-lg icon-dark\"></span></span></button><span data-full-name='Damn Hill Repeat Segment' id='js-full-name'>Damn Hill Repeat Segment</span></h1></div></div><div class='segment-action col-md-4'></div><div class='col-md-8'><div class='location'><strong>Run Segment</strong>Cedar Park, TX</div><ul class='list-stats inline-stats stats-lg mt-md'><li><div class=\"stat\"><span class=\"stat-subtext\">Distance</span><b class=\"stat-text\">0.60<abbr class='unit' title='kilometers'>km</abbr></b></div></li><li><div class=\"stat\"><span class=\"stat-subtext\">Avg Grade</span><b class=\"stat-text\">2.3<abbr class='unit' title='percent'>%</abbr></b></div></li><li><div class=\"stat\"><span class=\"stat-subtext\">Lowest Elev</span><b class=\"stat-text\">236<abbr class='unit' title='meters'>m</abbr></b></div></li><li><div class=\"stat\"><span class=\"stat-subtext\">Highest Elev</span><b class=\"stat-text\">250<abbr class='unit' title='meters'>m</abbr></b></div></li><li><div class=\"stat\"><span class=\"stat-subtext\">Elev Difference</span><b class=\"stat-text\">14<abbr class='unit' title='meters'>m</abbr></b></div></li><li><div class=\"stat attempts\"><span class=\"stat-subtext\">31,378 Attempts By 3,418 People</span><b class=\"stat-text\"></b></div></li></ul></div></div></div><div class='row'><div class='col-md-8'><div class='map-container map-large' id='map_canvas'></div><div class='elevation-chart chart-container mb-sm mt-sm' id='chart-container'><div id='elev-chart'></div></div></div><div class='sidebar col-md-4'><div data-react-class=\"SegmentDetailsSideBar\" data-react-props=\"{&quot;loggedIn&quot;:false,&quot;sideBarProps&quot;:{&quot;segmentId&quot;:3388435,&quot;fastestTimes&quot;:{&quot;overall&quot;:{&quot;id&quot;:25537590,&quot;name&quot;:&quot;Logan Patete&quot;,&quot;stats&quot;:[{&quot;label&quot;:&quot;CR&quot;,&quot;value&quot;:&quot;20\\u003cabbr class=&#39;unit&#39; title=&#39;second&#39;\\u003es\\u003c/abbr\\u003e&quot;}],&quot;date&quot;:&quot;Mar 31, 2020&quot;,&quot;profile&quot;:&quot;https://dgalywyr863hv.cloudfront.net/pictures/athletes/25537590/7362691/2/large.jpg&quot;,&quot;segmentEffortId&quot;:2692193511784042489,&quot;activityId&quot;:3394422491},&quot;women&quot;:{&quot;id&quot;:709300,&quot;name&quot;:&quot;Kelli Hughes&quot;,&quot;stats&quot;:[{&quot;label&quot;:&quot;CR&quot;,&quot;value&quot;:&quot;1:29&quot;}],&quot;date&quot;:&quot;Sep 18, 2020&quot;,&quot;profile&quot;:&quot;https://dgalywyr863hv.cloudfront.net/pictures/athletes/709300/524660/3/large.jpg&quot;,&quot;segmentEffortId&quot;:2742065739973920730,&quot;activityId&quot;:4078047577}},&quot;localLegend&quot;:{&quot;id&quot;:38019403,&quot;name&quot;:&quot;TJ Smyrson&quot;,&quot;stats&quot;:[{&quot;label&quot;:&quot;Local Legend&quot;,&quot;value&quot;:&quot;55 efforts&quot;}],&quot;mayorEffortCount&quot;:55,&quot;profile&quot;:&quot;https://dgalywyr863hv.cloudfront.net/pictures/athletes/38019403/20361514/1/large.jpg&quot;}}}\"></div></div></div><div class='row' id='segment-leaderboard'><div class='col-sm-6'><h2 class='text-title1'>Overall Leaderboard</h2><table class='table table-striped table-leaderboard'><thead><tr><th>Rank</th><th class='name'>Name</th><th class='pace hidden-xs'>Pace</th><th class='time last-child'>Time</th></tr></thead><tbody><tr><td>1</td><td>Logan Patete</td><td class='hidden-xs'>33s<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/3394422491\">20<abbr class='unit' title='second'>s</abbr></a></td></tr><tr><td>2</td><td>Chris Schaecher</td><td class='hidden-xs'>1:56<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/2546683254\">1:10</a></td></tr><tr><td>3</td><td>Max Masgras</td><td class='hidden-xs'>1:58<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/5065021400\">1:11</a></td></tr><tr><td>4</td><td>Luke Hein</td><td class='hidden-xs'>2:18<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/3940402441\">1:23</a></td></tr><tr><td>5</td><td>Chris T.</td><td class='hidden-xs'>2:26<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/12927995\">1:28</a></td></tr><tr><td>5</td><td>Oscar Orellana</td><td class='hidden-xs'>2:26<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/4370485532\">1:28</a></td></tr><tr><td>7</td><td>Kelli Hughes</td><td class='hidden-xs'>2:27<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/4078047577\">1:29</a></td></tr><tr><td>7</td><td>Kiran Josyula</td><td class='hidden-xs'>2:27<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/5682185692\">1:29</a></td></tr><tr><td>9</td><td>Bryce McAndrew</td><td class='hidden-xs'>2:34<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/1531691080\">1:33</a></td></tr><tr><td>9</td><td>Sean Flynn</td><td class='hidden-xs'>2:34<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/3863145494\">1:33</a></td></tr></tbody></table></div><div class='col-sm-6'><h2 class='text-title1'>Women&#39;s Leaderboard</h2><table class='table table-striped table-leaderboard'><thead><tr><th>Rank</th><th class='name'>Name</th><th class='pace hidden-xs'>Pace</th><th class='time last-child'>Time</th></tr></thead><tbody><tr><td>1</td><td>Kelli Hughes</td><td class='hidden-xs'>2:27<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/4078047577\">1:29</a></td></tr><tr><td>2</td><td>Lily Horvath</td><td class='hidden-xs'>2:57<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/5245871272\">1:47</a></td></tr><tr><td>3</td><td>Sophie Zook</td><td class='hidden-xs'>2:59<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/1996816551\">1:48</a></td></tr><tr><td>4</td><td>Julie W.</td><td class='hidden-xs'>3:04<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/1727007109\">1:51</a></td></tr><tr><td>5</td><td>Lilli Humphries</td><td class='hidden-xs'>3:06<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/3445067471\">1:52</a></td></tr><tr><td>5</td><td>Megan Wagenaar</td><td class='hidden-xs'>3:06<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/4302208307\">1:52</a></td></tr><tr><td>5</td><td>lauren willson</td><td class='hidden-xs'>3:06<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/5479643554\">1:52</a></td></tr><tr><td>8</td><td>Jennifer Knox</td><td class='hidden-xs'>3:11<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/1881730708\">1:55</a></td></tr><tr><td>9</td><td>Juliet C.</td><td class='hidden-xs'>3:14<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/1680580547\">1:57</a></td></tr><tr><td>9</td><td>Ashley R.</td><td class='hidden-xs'>3:14<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/2341229087\">1:57</a></td></tr></tbody></table></div></div><div class='alert alert-info'><p><a href='/register/free?cta=join-strava&amp;element=link&amp;placement=bottom&amp;source=segments_show'>Join Strava</a> to see the full leaderboard</p></div></div><footer id='global-footer'><div class='container-fluid'><div class='row footer-nav footer-row'><div class='footer-nav-wrapper'><div class='footer-nav-menu col-lg-2 col-md-3 col-sm-3 col-xs-6'><h4 tabindex='0'>Our Community</h4><ul class='footer-nav-submenu list-unstyled text-left'><li><a href='/about'>About</a></li><li><a href='/community-standards'>Strava Community Standards</a></li><li><a href='/careers'>Careers</a></li><li><a href='/pros'>Pros on Strava</a></li></ul></div><div class='footer-nav-menu col-lg-2 col-md-3 col-sm-3 col-xs-6'><h4 tabindex='0'>Follow</h4><ul class='footer-nav-submenu list-unstyled text-left'><li><a href='http://blog.strava.com/' target='_blank'>Blog</a></li><li><a href='https://www.facebook.com/Strava/' target='_blank'>Facebook</a></li><li><a href='https://instagram.com/strava' target='_blank'>Instagram</a></li><li><a href='http://twitter.com/strava' target='_blank'>Twitter</a></li></ul></div><div class='footer-nav-menu col-lg-2 col-md-3 col-sm-3 col-xs-6'><h4 tabindex='0'>Support</h4><ul class='footer-nav-submenu list-unstyled text-left'><li><a href='https://strava.zendesk.com/entries/61608110-How-to-contact-Strava-Support-Submit-a-ticket'>Contact</a></li><li><a href='https://strava.zendesk.com/entries/46363890-About-Strava-Maps'>Community Forum</a></li><li><a href='https://strava.zendesk.com/entries/46363890-About-Strava-Maps'>About Our Maps</a></li></ul></div><div class='footer-nav-menu col-lg-2 col-md-3 col-sm-3 col-xs-6 visible-lg-block'><h4><a href='https://business.strava.com'>Business</a></h4></div><div class='footer-nav-menu col-lg-2 col-md-3 col-sm-3 col-xs-6 visible-lg-block'><h4><a href='/mobile'>Mobile</a></h4></div><div class='footer-nav-menu col-lg-2 col-md-3 col-sm-3 col-xs-6 visible-lg-block'><h4><a href='/premium'>Subscription</a></h4></div><div class='footer-nav-menu col-md-3 col-sm-3 col-xs-6 hidden-lg'><h4 class='more'><span class=\"app-icon-wrapper  \"><span class=\"app-icon icon-ellipsis icon-xl\">More</span></span></h4><ul class='footer-nav-submenu list-unstyled text-left'><li><a href='/mobile'>Mobile</a></li><li><a href='/premium'>Subscription</a></li><li><a href='https://business.strava.com'>Business</a></li></ul></div></div></div><div class='row footer-other'><div class='col-md-12'><div class='privacy-terms'><a href='/legal/privacy'>Privacy Policy</a>|<a href='/legal/terms'>Terms and Conditions</a></div><div class='copyright'><p>© 2021 Strava</p></div></div></div></div></footer><script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/mapbox-04f75a62080d3f801750d3a4bb3973962d34f816d78f8997797210e678695808.js\"></script><script>  window._maps_api = \"pk.eyJ1Ijoic3RyYXZhIiwiYSI6IlpoeXU2U0UifQ.c7yhlZevNRFCqHYm6G6Cyg\"  jQuery(document).ready(function(){    // TODO: no need for locale    Strava.Maps.Mapbox.Base.setMapIds({\"terrain_id\":\"mapbox/dark-v10\",\"satellite_id\":\"mapbox/satellite-v9\",\"standard_id\":\"strava/ck2gt6oil0c7y1cnvlz1uphnu\"});  });</script><script id='lightbox-template' type='text/template'><div class='lightbox-window modal-content'><div class='close-lightbox'><button class='btn btn-unstyled btn-close'><div class='app-icon icon-close icon-xs icon-white'></div></button></div></div></script><script id='popover-template' type='text/template'><div class='popover'></div></script><script>  window._asset_host = \"https://d3nn82uaxijpm6.cloudfront.net\";  window._measurement_preference = \"meters\";  window._date_preference = \"%m/%d/%Y\";  window._datepicker_preference_format = \"mm/dd/yy\";    jQuery(document).ready(function() {    Strava.Util.EventLogging.createInstance(\"https://analytics.strava.com\",\"7215fa60b5f01ecc3967543619f7e3d9\", null);  });</script><script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava/i18n/locales/en-US-702026fef7c0db96daa9e9c1053241b38f9aa712aabba4769a96cdd867db27ba.js\"></script><script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/application-6cdc4fed7c4fe3d1a6e3d39a0fde55f80e7c09f792c7cf358f222c0118dd0125.js\"></script><script src=\"https://www.strava.com/cookie-banner\"></script><script>  jQuery(document).ready(function(){    typeof StravaCookieBanner !== 'undefined' && StravaCookieBanner.render();  });</script><div id='fb-root'></div><script>  window.fbAsyncInit = function() {    FB.init({      appId: \"284597785309\",      status: true,      cookie: true,      xfbml: true,      version: \"v3.2\"    });    Strava.Facebook.PermissionsManager.getInstance().facebookReady();    jQuery('#fb-root').trigger('facebook:init');  };  (function(d){    var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];    if (d.getElementById(id)) {return;}    js = d.createElement('script'); js.id = id; js.async = true;    js.src = \"//connect.facebook.net/en_US/sdk.js\";    ref.parentNode.insertBefore(js, ref);  }(document));</script><script>  jQuery('document').ready(function() {    setTimeout(function() {      jQuery('#face-pile').css('visibility', 'visible');    }, 300);  });  var ua = navigator.userAgent;  var checker = {    ios: ua.match(/(iPhone|iPod|iPad)/),    android: ua.match(/Android/)  };    var regButton = jQuery('#get-started-button');  if (checker.android || checker.ios) {    regUrl = regButton.attr('href');    if (checker.android) {      regUrl = 'https://market.android.com/details?id=com.strava';    } else if (checker.ios) {      regUrl = 'http://itunes.apple.com/app/strava-cycling/id426826309';    }    regButton.attr('href', regUrl).text('Get the App');  }  regButton.css('visibility', 'visible');</script><script>  var currentAthlete = new Strava.Models.CurrentAthlete({\"logged_in\":false});  HAML.globals = function() {    return {      currentAthlete: currentAthlete,      renderPartial: function(name, context) {        if (context == null) {          context = this;        }        return JST[name](context);      }    }  }</script><script>  new Strava.Initializer();</script><script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava/maps/mapbox/manifest-418c4fa1eff6bd8a383a7aa4db01815543cbc59c06301baa21fc92c67f7cce27.js\"></script><script src=\"https://d3nn82uaxijpm6.cloudfront.net/packs/js/segments-379718cd589f746f8308.chunk.js\"></script><script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava/segments/manifest-ef4ec395935304954aafe9f2bf22d67367e76b87d3a3f9f57976ac8357aefd4e.js\"></script><script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava/segments/history/manifest-31cd8f62449bb3f7c0241666000452997e7e7b7f9458ea2d44ae4805b4a926e3.js\"></script><script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava/directory/manifest-5cc467395d7ea4e8c5100545f2d51fa1eb72477e9b0ad32291d3be507f2dbd7c.js\"></script><script>  jQuery(document).ready(function() {    Strava.ExternalAnalytics.trackV2({      category: 'segments',      page: 'segment_detail_compare_efforts_upsell',      action: 'screen_enter',      properties: {        segment_id: 3388435,      }    });    jQuery('#recent-efforts-cta').on('click', function() {      Strava.ExternalAnalytics.trackV2({        category: 'segments',        page: 'segment_detail_compare_efforts_upsell',        action: 'click',        element: 'subscribe_button',        properties: {          segment_id: 3388435        }      });    });  });</script><script>  jQuery(document).ready(function() {    jQuery('#compare-efforts-cta').on('click', function() {      Strava.ExternalAnalytics.trackV2({        category: 'segments',        page: 'segment_detail',        action: 'click',        element: 'compare_efforts',        properties: {          segment_id: 3388435        }      });    });      new Strava.Segments.Initializer({      segmentId: 3388435,      segmentName: \"Damn Hill Repeat Segment\",      segmentHazard: false,      segmentHazardWaived: false,      showWaiver: false,      canStarSegment: false    });  });</script><script>  if ('serviceWorker' in navigator) {    window.addEventListener('load', function() {      navigator.serviceWorker.register(\"/service_worker.js?v=dLlWydWlG8\").then(function(registration) {      }, function(err) {        console.log('ServiceWorker registration failed: ', err);      });    });  }</script><script>  jQuery(document).ready(function() {    // Scroll Tracking    jQuery(document).one('scroll', function(){      Strava.ExternalAnalytics.trackV2({        category: 'page_scrolled'      });    });  });</script><script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/strava/ui/views/SmartbannerOrionView-14369f065f3110607a3aec2fd1775faf1804cc5c5857ef5790a321e0f190d8e8.js\"></script><script>  jQuery(document).ready(function() {    new Strava.Ui.Views.SmartbannerOrionView();  });</script><script>  // Mobile Menu transition handler  jQuery('.collapsable-nav #container-nav')    .on('show.bs.collapse', function(){      jQuery('.smartbanner-content').slideUp(100);      jQuery('html').addClass('mobile-menu-open');    })    .on('hidden.bs.collapse', function(){      jQuery('.smartbanner-content').slideDown(100);      jQuery('html').removeClass('mobile-menu-open');    });</script><script src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/bootstrap.min-55483ca093070244e24730190b707a18467cb78d3262a0133d34b80fc82c8636.js\"></script></body></html>");
    }
    
    private HttpRequestResponse getValidRunningSegmentResponse() {
        return new HttpRequestResponse(0, "", "{\n" +
                "    \"segments\": [{\n" +
                "        \"id\": 10727602,\n" +
                "        \"resource_state\": 2,\n" +
                "        \"name\": \"400 at WilCo Park's Track \",\n" +
                "        \"climb_category\": 0,\n" +
                "        \"climb_category_desc\": \"NC\",\n" +
                "        \"avg_grade\": 0.0,\n" +
                "        \"start_latlng\": [30.560495, -97.76596],\n" +
                "        \"end_latlng\": [30.560493, -97.765893],\n" +
                "        \"elev_difference\": 3.1,\n" +
                "        \"distance\": 446.8,\n" +
                "        \"points\": \"azoyDf|usQVBnAC~@FTC\\\\KJGJMHY@u@GWSY_@OqDC_@DQPMTGZ?ZDZHNNHTJ\",\n" +
                "        \"starred\": false,\n" +
                "        \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/GFSH6LF6K2DPYOTJM3VHGEBK2DU4XYNQKJOOHJXTGOQYBTOHRMEKTDVT4232A4TT6MC3LAHACG4S7IQZKOL2S===\",\n" +
                "        \"local_legend_enabled\": true\n" +
                "    }, {\n" +
                "        \"id\": 26074032,\n" +
                "        \"resource_state\": 2,\n" +
                "        \"name\": \"West Pecan Trails 10K - 2020\",\n" +
                "        \"climb_category\": 0,\n" +
                "        \"climb_category_desc\": \"NC\",\n" +
                "        \"avg_grade\": -0.0,\n" +
                "        \"start_latlng\": [30.440073, -97.621],\n" +
                "        \"end_latlng\": [30.440687, -97.620465],\n" +
                "        \"elev_difference\": 28.6,\n" +
                "        \"distance\": 9960.6,\n" +
                "        \"points\": \"mixxDfryrQv@wCTo@ZiAVk@PWXq@JQN?TDXPb@Pl@ZZHD?DINi@Re@FIFEXANDJ?n@J@@NHlBXVJj@JhA^nAl@vAx@xAr@z@p@tBv@h@JXHpBhAfBz@pC~APLRZRNpB`ADH?P_@bAALBRPj@?tACXWjAkAfDq@vCILE?MG_@]OKy@Sc@AWBOFIAKGE?WLG?MEE@YZc@x@WLWVIEO]OIWE]AQEUA_@GMDO\\\\[T_@l@yBhFeArC{AvCW\\\\}BvBk@d@QFi@LqARMHQZ_@P}A|C_ApA}@bAiBbCUTw@Zs@t@]v@e@~AKNk@X]Ja@Jo@Fe@AsBi@QDIDOTOl@_AdCaDlIIDOKKAgAh@Q@SMUUOSw@}A]a@]g@S_ASeAQ_@_@e@u@]KKAM@Kf@aAp@_B@AHBWCOI[W]Q{@o@iAm@s@q@}@sA]m@}@oBOSSOGKs@sBKi@OScAaAm@_@{Ae@q@Iy@Ce@?u@HuA^gAVeADmAEs@Oq@Yu@SsAy@HQBS\\\\w@V}@TkADa@FaABwCEwAGq@[kBwAuEi@wBa@iAMu@Ui@Ow@Gu@@c@EaAL{B?o@Eq@Ms@e@oAUa@Qg@q@iAsBwEq@gBQaAAw@HcCBQCFBEAw@JqBCs@OmAEy@CeABe@BGHAVAf@HZJb@RpCbB~C~Af@HX@b@GVKb@[d@k@Ri@bAeCz@eCx@qB^gAj@uAVe@\\\\_@TOVKvA]n@Sd@KVAT?VD`@P\\\\RfAr@~@^nEfChAh@hAr@NF~BtAx@`@DBC?jCxAJHLVBPOb@CZER{BtFi@`BGfACfA@l@FNLDhE^zBBHBDFBVKPQFEADIFATBb@P~@JlAFh@Hb@CxBf@HD@NUx@KN}@~Bm@pAq@lBsAdDEVDNZRrAh@tC~ArDfBRHHALQpBkFBKAUPMJUVk@Le@BQLS?KKM_CmA\",\n" +
                "        \"starred\": false,\n" +
                "        \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/H4U42XCCV33GDAQS3DVA3QBMM6E2EYOYL4BD63IWUVUJDFVQ7M7GPAICUGJR5RMEVTITHVKZDYNHTIV4CGP2O===\",\n" +
                "        \"local_legend_enabled\": true\n" +
                "    }, {\n" +
                "        \"id\": 3388435,\n" +
                "        \"resource_state\": 2,\n" +
                "        \"name\": \"Damn Hill Repeat Segment\",\n" +
                "        \"climb_category\": 0,\n" +
                "        \"climb_category_desc\": \"NC\",\n" +
                "        \"avg_grade\": 2.3,\n" +
                "        \"start_latlng\": [30.50847471691668, -97.7593353856355],\n" +
                "        \"end_latlng\": [30.50786920823157, -97.76374904438853],\n" +
                "        \"elev_difference\": 13.6,\n" +
                "        \"distance\": 603.5,\n" +
                "        \"points\": \"}teyDzrtsQBDH\\\\KbC?p@Dv@Fj@BzAFl@Hf@FjA?|AJ`EKhA@`@\\\\lAPOHiBNi@\",\n" +
                "        \"starred\": false,\n" +
                "        \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/3PFN7QL2ABMUAWRVHMWV246DC4LVMYYQZL67EJ7FHM4JGIP3BL662FHKLWXBOCQPL73R23SXGZXU745BTILQ====\",\n" +
                "        \"local_legend_enabled\": true\n" +
                "    }, {\n" +
                "        \"id\": 10732476,\n" +
                "        \"resource_state\": 2,\n" +
                "        \"name\": \"BCRT: Pool to Champion Park\",\n" +
                "        \"climb_category\": 0,\n" +
                "        \"climb_category_desc\": \"NC\",\n" +
                "        \"avg_grade\": 0.3,\n" +
                "        \"start_latlng\": [30.519168, -97.738558],\n" +
                "        \"end_latlng\": [30.512155, -97.756739],\n" +
                "        \"elev_difference\": 8.5,\n" +
                "        \"distance\": 2031.5,\n" +
                "        \"points\": \"wwgyD~ppsQEd@U~AInADdARt@Hd@Fv@RjAHdADtAEz@JnAB`A?VEh@?rADz@Ht@@|@Hf@?h@Fb@N`@d@p@Nt@GhAWn@APBH\\\\d@fA`Cb@h@\\\\`Af@x@j@r@JRRNPTBJ@RBL`@v@JZP^FTRb@^p@X\\\\R\\\\Zx@`AnAFR@TBHn@rAV`ABL?b@Ff@ZhAXlAj@~ATb@\\\\b@\\\\p@`@fAVXd@r@l@bAl@h@v@~@h@b@d@Zn@^TT^P\",\n" +
                "        \"starred\": false,\n" +
                "        \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/GDSMFEUBT2A5542EZRFTJ2NZEBYZIW6NQIQE6QXJVFYBBEJK7TGKWRT3P4DVDXWKESYAKU4I56MFOOLQAO4Q====\",\n" +
                "        \"local_legend_enabled\": true\n" +
                "    }, {\n" +
                "        \"id\": 25864886,\n" +
                "        \"resource_state\": 2,\n" +
                "        \"name\": \"McNeil 5k xc  Course\",\n" +
                "        \"climb_category\": 0,\n" +
                "        \"climb_category_desc\": \"NC\",\n" +
                "        \"avg_grade\": 0.0,\n" +
                "        \"start_latlng\": [30.533049, -97.631479],\n" +
                "        \"end_latlng\": [30.533096, -97.630674],\n" +
                "        \"elev_difference\": 13.7,\n" +
                "        \"distance\": 4976.7,\n" +
                "        \"points\": \"onjyDvs{rQP^r@rBTx@Px@t@fC@PGLEBQBgBFcACgBQ_@IyB_@wAa@g@_@U]G[KwBAUBGGeB@m@L}@Z{@^_@NEf@EbAATMP[Dc@CYCEMAq@H_AR]@_A?ODKJCNEh@Ah@JpF?zBDdACl@CPGNeAxBKJSFwBAa@@cACm@?oAGqABuCBGCCIAIJQnEgEn@a@TI\\\\Eh@BpEZ~@ALCBEBMMmC?kBHwC?sBCYKMMEUCiBHuCH_B?cAI_AAy@F{EN]DgAXc@DiDEsAIi@@e@EwBGwAFo@Ly@IQ?g@Fu@Vc@DKAEECG?I@INK|@GTGPOFUFILDVTb@V\\\\Jv@Ib@IpBk@TE\\\\Al@FVHZZx@C|A@vANrAoAv@_@Re@LQRSRItACdB@PBPLRHtATvAZz@BZEfAWn@c@h@e@d@UZEvA^hE`@h@NRJTVRX\\\\r@b@rAr@pC\",\n" +
                "        \"starred\": false,\n" +
                "        \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/NXKPEEBBCRY7Z4T4MJNO4URSYYF6ZVJE2XKKQWKX56VWSGRFD2FOH6ZJRQH4LOSJHLA2VOMZANZ44GTZEYWA====\",\n" +
                "        \"local_legend_enabled\": true\n" +
                "    }, {\n" +
                "        \"id\": 7659873,\n" +
                "        \"resource_state\": 2,\n" +
                "        \"name\": \"Brushy Creek Kenney Fort to Rabb House\",\n" +
                "        \"climb_category\": 0,\n" +
                "        \"climb_category_desc\": \"NC\",\n" +
                "        \"avg_grade\": 0.3,\n" +
                "        \"start_latlng\": [30.513927, -97.63715],\n" +
                "        \"end_latlng\": [30.514522, -97.650741],\n" +
                "        \"elev_difference\": 6.3,\n" +
                "        \"distance\": 1399.8,\n" +
                "        \"points\": \"_wfyDdw|rQ@TLd@P`@?CJnA?l@Il@Mh@S`@Qf@Kf@o@fAOd@AzAGh@}@|CGj@Mh@KpAIj@?l@El@_@rAYvAm@|B_@lASd@Kf@Eh@B`BNh@X\\\\Ph@?fCHtALtAZ`CRh@ZnABxAFj@R`@XrAZ`@Ph@\",\n" +
                "        \"starred\": false,\n" +
                "        \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/GS3YOXTKSDPI3VLKJUXKRX7KBAD4RYG4VZBCJRHLSDGXN32ALVA3U5UCDWLKDRXTVUW7EAJSBHXTXAXLWGAFW===\",\n" +
                "        \"local_legend_enabled\": true\n" +
                "    }, {\n" +
                "        \"id\": 10254017,\n" +
                "        \"resource_state\": 2,\n" +
                "        \"name\": \"Sendero Trail Hill Climb\",\n" +
                "        \"climb_category\": 0,\n" +
                "        \"climb_category_desc\": \"NC\",\n" +
                "        \"avg_grade\": 4.3,\n" +
                "        \"start_latlng\": [30.546272, -97.731408],\n" +
                "        \"end_latlng\": [30.546535, -97.734921],\n" +
                "        \"elev_difference\": 15.5,\n" +
                "        \"distance\": 362.5,\n" +
                "        \"points\": \"eamyDhdosQ_@x@a@dBK|@Q`AAn@MnAFV^l@FRFj@Xx@Cd@If@GP\",\n" +
                "        \"starred\": false,\n" +
                "        \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/RYYLPOWV4NVCZBJYYCN43GNCIVBS6BPMITTHLSK5GZHEXSXO3RBZQ35VDOCHP2KCFAR4JE2ERXOABJTVM7DO6===\",\n" +
                "        \"local_legend_enabled\": true\n" +
                "    }, {\n" +
                "        \"id\": 9000845,\n" +
                "        \"resource_state\": 2,\n" +
                "        \"name\": \"WB Trail Loop\",\n" +
                "        \"climb_category\": 0,\n" +
                "        \"climb_category_desc\": \"NC\",\n" +
                "        \"avg_grade\": 0.0,\n" +
                "        \"start_latlng\": [30.442028, -97.677553],\n" +
                "        \"end_latlng\": [30.442094, -97.677574],\n" +
                "        \"elev_difference\": 29.5,\n" +
                "        \"distance\": 3570.5,\n" +
                "        \"points\": \"suxxDvsdsQHCNMJY?y@HMDM?]Lq@COMS@QFIRKjAqAVOPGhA}@r@_@n@e@d@If@Ut@i@RWRM\\\\MH@PCn@S^GXBVEXS^KFQ@_@Rc@Z]JQf@W@MRUFCZAnAg@p@@FAr@Yh@Qr@JVLt@JTHHCTQvAm@NCvA}@ZKP?TJ\\\\DLL`@n@b@X`@z@Jn@HFJXNRRVFBRF`@FTLl@Th@Jp@Ff@Kd@SLCNBFAXKHAf@Ff@?hATNHTZ?HQr@W`BKHCHBTAJUn@Ed@S^I\\\\Yd@M^Q\\\\Ob@QVo@rBCb@IZWX_@`ASVQHS@MJIN_@Ro@Aq@HQC[Bs@Nm@Cc@N[CAGWGIKIC[I]CGMCSACWMMOc@SM?]QSAi@TI?MHG@KIG?u@r@y@Xw@\\\\KHSH]^m@b@O`@SVKFo@z@a@XW^IDO?GBGPQP_@Ha@Pg@Bm@Ni@GM?_@EUI[EgA@gAHa@Ie@O_@_@MI]cAMWUaAs@k@QGWMYUGICMDg@KOUUi@u@Uo@EE\",\n" +
                "        \"starred\": false,\n" +
                "        \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/R6T5WTEE5SPUPCFLJZJCT5SR5TEL52FPKVQONZCXGQCFXCXCANEI2EKF2CKVODPGW5J45JQFTBJKQUGW3QSJC===\",\n" +
                "        \"local_legend_enabled\": true\n" +
                "    }, {\n" +
                "        \"id\": 18400174,\n" +
                "        \"resource_state\": 2,\n" +
                "        \"name\": \"Paloma Lake Blvd to Infinity Pool \",\n" +
                "        \"climb_category\": 0,\n" +
                "        \"climb_category_desc\": \"NC\",\n" +
                "        \"avg_grade\": -0.4,\n" +
                "        \"start_latlng\": [30.563588, -97.616185],\n" +
                "        \"end_latlng\": [30.56404, -97.611768],\n" +
                "        \"elev_difference\": 3.1,\n" +
                "        \"distance\": 790.4,\n" +
                "        \"points\": \"kmpyDdtxrQy@Fo@CMICS?[BKZa@pAqB@SKSk@]_@Y{AgBq@i@_@OcBQm@KIEEOF_DFk@HYLEp@CbAI^?ZEj@Yp@u@ZW\",\n" +
                "        \"starred\": false,\n" +
                "        \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/JC6MJALA44PND4FR4SLC4BEWIG5TYQBKBCX5JUDJ7RQMD3RHKFSLGFJKWMQK7ALI43Y5A3GV2HSABUK7M6NLW===\",\n" +
                "        \"local_legend_enabled\": true\n" +
                "    }, {\n" +
                "        \"id\": 23699090,\n" +
                "        \"resource_state\": 2,\n" +
                "        \"name\": \"Bison Stampede 10K - 2000\",\n" +
                "        \"climb_category\": 0,\n" +
                "        \"climb_category_desc\": \"NC\",\n" +
                "        \"avg_grade\": -0.0,\n" +
                "        \"start_latlng\": [30.43671, -97.753868],\n" +
                "        \"end_latlng\": [30.436747, -97.753407],\n" +
                "        \"elev_difference\": 32.3,\n" +
                "        \"distance\": 10060.4,\n" +
                "        \"points\": \"mtwxDtpssQG|A@n@Cv@Iz@Kd@OZSp@MJUASIMKY[mAkBw@qBQSOMe@SQEu@C_@F_@NGHB@KLCH?RJp@XbAXtAPhAn@zBJt@Px@B\\\\X~Ad@lBN^ZpANdAhAfFF\\\\CJOJeA\\\\kBd@wBp@wAf@{Bl@s@d@WZQ\\\\WRG@KC_@Mg@KkAIq@MSM_@[Q]Oe@m@mC_@iAS[{@s@u@u@a@g@[[i@c@k@m@m@e@iCqC_C}Be@m@_@q@Uo@Ie@Ie@Ew@@gA\\\\mED}@Cu@Im@kAaD_@qAS{@m@mBuBaFYk@u@qA}@mAaAeAEICMLYV[b@[j@SrAq@b@OrDeAtDwALILL?B@?LZZb@\\\\n@PTd@bAL`@BZN^XvAf@jAj@h@n@`@pB`@ZBb@?`BS`CaAZMJKf@Mz@a@dA[r@[x@[pAk@b@YVYT_@rBaFH[Fk@?o@Ea@Oc@aAuAs@wAq@}AWq@EQ@KPQZMh@QrAOxCIpBa@HGDKASc@eCQy@i@kDkAwDKc@Eq@AoAEeA?WBOTQn@Gd@M^UZ_@nDeGrCsCHGJCX@VJtBpBVP^f@RL\\\\b@\\\\Vd@Vh@RXDJCDCDSBoAH{@RoAf@wBFm@ZaGN}At@yCz@}CPa@HKDC~@DZHPHb@Zf@f@VXJHjBtBVb@j@zAJb@Ft@@jACh@OfAW`AyAnCqCnEY^ETFNvA`CjAbARRNV`@X`@\\\\t@dA`@`@^h@tAdAPHZJPN`@Rp@r@FVVf@RTFJFb@Rp@Lx@ZjAHh@IhA?^GX@LKx@Cf@Sz@I~@Kd@Et@QhAKT?ZIrAGVW~BMf@?ZKt@W~@IfAGRIp@AXI^CXBdBJp@@h@Nf@IPYJSFUA]YgAqA}@q@e@We@Qk@Qy@OgAEuBCuC?_CD{@Ee@FeBAS@[Dq@LSHoAl@a@N_@Ti@RQPq@`@Y^MXMb@It@@h@Et@?x@BlADf@Et@\",\n" +
                "        \"starred\": false,\n" +
                "        \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/5JGNYRSJJEKJDEXRWB6CSJBSXTDYUZQP6RYM36NFSB3CNFKHE6MOTHMJJ4L4MN6YVCMZTOWVUUQONUJW7LI5W===\",\n" +
                "        \"local_legend_enabled\": false\n" +
                "    }]\n" +
                "}");
    }

    private HttpRequestResponse getValidCyclingSegmentSearchResponse() {
        return new HttpRequestResponse(0, "", "{\n" +
                "    \"segments\": [\n" +
                "        {\n" +
                "            \"id\": 12800003,\n" +
                "            \"resource_state\": 2,\n" +
                "            \"name\": \"Jesse's hill\",\n" +
                "            \"climb_category\": 0,\n" +
                "            \"climb_category_desc\": \"NC\",\n" +
                "            \"avg_grade\": 3.4,\n" +
                "            \"start_latlng\": [\n" +
                "                30.434003,\n" +
                "                -97.55872\n" +
                "            ],\n" +
                "            \"end_latlng\": [\n" +
                "                30.435337,\n" +
                "                -97.561555\n" +
                "            ],\n" +
                "            \"elev_difference\": 10.6,\n" +
                "            \"distance\": 310.2,\n" +
                "            \"points\": \"ocwxD~lmrQwB~F[`AsAfDa@lA\",\n" +
                "            \"starred\": false,\n" +
                "            \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/P2HLNTEBFL6TGQKC36FEMRQTLRD4XDFERPXHZQ6AXQ6OVXDKRWEK3VKNHRLXL4ECIHENT7RPB6UOHQXHY56A====\",\n" +
                "            \"local_legend_enabled\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 11730849,\n" +
                "            \"resource_state\": 2,\n" +
                "            \"name\": \"Weiss Ln: Jesse Bohls to Via Sorento\",\n" +
                "            \"climb_category\": 0,\n" +
                "            \"climb_category_desc\": \"NC\",\n" +
                "            \"avg_grade\": 1.6,\n" +
                "            \"start_latlng\": [\n" +
                "                30.438137,\n" +
                "                -97.56736\n" +
                "            ],\n" +
                "            \"end_latlng\": [\n" +
                "                30.442859,\n" +
                "                -97.564285\n" +
                "            ],\n" +
                "            \"elev_difference\": 10.6,\n" +
                "            \"distance\": 605.6,\n" +
                "            \"points\": \"i}wxD~borQsGuD{SoL\",\n" +
                "            \"starred\": false,\n" +
                "            \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/P4RAHN4MAEXF7UGQWGFSYK4FKF7F4GHPFMH6H4P6TOYIRQYJI4ZMIKU7TVFLEBSLJXVNOST4AWVHV5DZP3JQ====\",\n" +
                "            \"local_legend_enabled\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 16790944,\n" +
                "            \"resource_state\": 2,\n" +
                "            \"name\": \"Give it beans!\",\n" +
                "            \"climb_category\": 0,\n" +
                "            \"climb_category_desc\": \"NC\",\n" +
                "            \"avg_grade\": -0.5,\n" +
                "            \"start_latlng\": [\n" +
                "                30.453969,\n" +
                "                -97.557251\n" +
                "            ],\n" +
                "            \"end_latlng\": [\n" +
                "                30.449656,\n" +
                "                -97.559986\n" +
                "            ],\n" +
                "            \"elev_difference\": 2.6,\n" +
                "            \"distance\": 545.6,\n" +
                "            \"points\": \"g`{xDzcmrQ|Y`P\",\n" +
                "            \"starred\": false,\n" +
                "            \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/GU56XSGJR5DISZJ55O63NAOD62U3HVGSP23SKH2RAPLCD4I4SAM6WPPYGUAL4M3JXDHKI6WTVL6JK5OZFTOA====\",\n" +
                "            \"local_legend_enabled\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 15273874,\n" +
                "            \"resource_state\": 2,\n" +
                "            \"name\": \"LPTri2017 Loop\",\n" +
                "            \"climb_category\": 0,\n" +
                "            \"climb_category_desc\": \"NC\",\n" +
                "            \"avg_grade\": -0.0,\n" +
                "            \"start_latlng\": [\n" +
                "                30.443208,\n" +
                "                -97.564072\n" +
                "            ],\n" +
                "            \"end_latlng\": [\n" +
                "                30.442555,\n" +
                "                -97.564537\n" +
                "            ],\n" +
                "            \"elev_difference\": 35.0,\n" +
                "            \"distance\": 20633.5,\n" +
                "            \"points\": \"_}xxDnnnrQqM_HcGiDgHyDkDiBuJsFeGaDqQ{JkFsCeByAg@{@a@]kAq@aBw@uB{@wBq@wBw@wP_JcAm@[a@ESASHi@Ry@?i@Oc@[Y}GoDkJgF{HcEi_@qSwZiPcEuBOSCMHyAGe@U]MISGoAKiLuAcHs@WQIY?QV{BZ_Ep@gJJkALe@JWZ_@hAy@dAq@dAu@^]PYJ]PeB\\\\kEtBgZPeBN[XQ\\\\AzIjAvANvALvQvBXGPWVqB|A_T\\\\{D^kG`A_L\\\\_F`@{DjCw]zDgf@`AiKNqBFURI`AHbH`AtJlAdT`CpAVh@VNNRXPr@BT?l@EfAs@vICdAHx@HVLRNPf@VfANfAHbPfBjBN`AZHFRPT`@JZB\\\\A`A{@xJMhC@`@Hp@L^PXVTXNZHjBVtJ~@dBNh]pD~E`@hEd@nAPrA^nAn@hOpI`DlBtEdCjCtAvCfBPXF^C`@iA~CaAdCe@tAC`@B`@Ln@PXRTd@Z|KdGlCzApE`C`E~B~DtBbH~D~BlAdEfCv@`@nF~Cv@`@zEjChBfAxJtFzC~ANPBXgG|O_DtIo@bBUr@Ix@Bd@Hb@P\\\\RXb@\\\\nJ`FlBnAd@r@DLJn@@`@WfBiFhN{BrFaAtBUd@mAlBaI|Lm@hAy@nBmC`HkCnHqFjNuA|DaEzJ_EpKaB`E{Nr`@]dAEHQFUImAw@aQsJkEyB\",\n" +
                "            \"starred\": false,\n" +
                "            \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/FHZLD46IUNYEH2UWSKX7CBQ27ZBOBHKVPSOYNMOVYPY5OW76NWWXUE63HRDTOCE7U4KB25GBFVBYQKRDYCKMQ===\",\n" +
                "            \"local_legend_enabled\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 17497583,\n" +
                "            \"resource_state\": 2,\n" +
                "            \"name\": \"Falcon Run\",\n" +
                "            \"climb_category\": 0,\n" +
                "            \"climb_category_desc\": \"NC\",\n" +
                "            \"avg_grade\": -0.8,\n" +
                "            \"start_latlng\": [\n" +
                "                30.458928,\n" +
                "                -97.586201\n" +
                "            ],\n" +
                "            \"end_latlng\": [\n" +
                "                30.457228,\n" +
                "                -97.577405\n" +
                "            ],\n" +
                "            \"elev_difference\": 10.6,\n" +
                "            \"distance\": 925.8,\n" +
                "            \"points\": \"g_|xDxxrrQM_@?OFM`BgAd@i@Ta@Pa@Nc@Lw@De@?i@OmLDiBDi@N_AlAuEX_BFcBCkAGo@[yAAUBUJOb@Q\",\n" +
                "            \"starred\": false,\n" +
                "            \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/UZFLXFS6RNJQIDWNOVNSTDHLDGI65O6W66RIT7WTRNMV6XUTROLSGA773VTCAKMS4GDWKMAKRSKZHUV2BWHW2===\",\n" +
                "            \"local_legend_enabled\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 27487636,\n" +
                "            \"resource_state\": 2,\n" +
                "            \"name\": \"Nope, no Jesse's Hill today...\",\n" +
                "            \"climb_category\": 0,\n" +
                "            \"climb_category_desc\": \"NC\",\n" +
                "            \"avg_grade\": 0.6,\n" +
                "            \"start_latlng\": [\n" +
                "                30.433409,\n" +
                "                -97.556785\n" +
                "            ],\n" +
                "            \"end_latlng\": [\n" +
                "                30.446955,\n" +
                "                -97.560807\n" +
                "            ],\n" +
                "            \"elev_difference\": 11.8,\n" +
                "            \"distance\": 2112.5,\n" +
                "            \"points\": \"w_wxD|`mrQqAw@w@]aIwBeAUQIGEEWTuA?YCKIGi@MAMAAGAOBICwDgA}Bg@cD_AyMaDoA_@eAWaAG[B]Fg@Py@f@e@j@]p@mA~CUv@QlACvBGp@Ib@Mb@iBdFe@jAa@hA[t@eCxG_@l@UZOPoAdAWZS^\",\n" +
                "            \"starred\": false,\n" +
                "            \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/UZUE3ZVLYID5W4S3RYOSFTAN26LM6757FC4S3SHP37COEBDPYIL3V32L7T7IGBT2PDQKN2BL24ZQFXURHIZA====\",\n" +
                "            \"local_legend_enabled\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 17627839,\n" +
                "            \"resource_state\": 2,\n" +
                "            \"name\": \"C for chicane\",\n" +
                "            \"climb_category\": 0,\n" +
                "            \"climb_category_desc\": \"NC\",\n" +
                "            \"avg_grade\": -0.1,\n" +
                "            \"start_latlng\": [\n" +
                "                30.464474,\n" +
                "                -97.549455\n" +
                "            ],\n" +
                "            \"end_latlng\": [\n" +
                "                30.461936,\n" +
                "                -97.552278\n" +
                "            ],\n" +
                "            \"elev_difference\": 1.8,\n" +
                "            \"distance\": 438.6,\n" +
                "            \"points\": \"}a}xDbskrQvDpB\\\\VLLHPDR?TUdACVAXDXHTLP`@ZbFhC\",\n" +
                "            \"starred\": false,\n" +
                "            \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/X5I76S3SZ6F63LEYOCXIPWUUEHJR3XOSKTYUDQZWMKDQGOAQEC7VQPFXA24Z4Q5ZJ32QGEDHF7NQDWHHRWN4E===\",\n" +
                "            \"local_legend_enabled\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 26846609,\n" +
                "            \"resource_state\": 2,\n" +
                "            \"name\": \"Climb to Falcon Pointe\",\n" +
                "            \"climb_category\": 0,\n" +
                "            \"climb_category_desc\": \"NC\",\n" +
                "            \"avg_grade\": 1.1,\n" +
                "            \"start_latlng\": [\n" +
                "                30.441648,\n" +
                "                -97.578987\n" +
                "            ],\n" +
                "            \"end_latlng\": [\n" +
                "                30.449416,\n" +
                "                -97.589343\n" +
                "            ],\n" +
                "            \"elev_difference\": 15.8,\n" +
                "            \"distance\": 1392.8,\n" +
                "            \"points\": \"gsxxDtkqrQoE@m@DcAT_AXi@Vu@h@aAhAq@nAgApCsBxEmCvGkAnC_KfVaB`Dc@p@_@r@\",\n" +
                "            \"starred\": false,\n" +
                "            \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/PROKLQ3MZI5YBEO6T3ZKAL6WRR477OWMBOLECSXSLXVDYIECKNBVHV245F3PUXROAH4RCWAV6ME3ZMRYF7I6M===\",\n" +
                "            \"local_legend_enabled\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 17533938,\n" +
                "            \"resource_state\": 2,\n" +
                "            \"name\": \"Backdoor Falcon Pointe\",\n" +
                "            \"climb_category\": 0,\n" +
                "            \"climb_category_desc\": \"NC\",\n" +
                "            \"avg_grade\": 0.1,\n" +
                "            \"start_latlng\": [\n" +
                "                30.449531,\n" +
                "                -97.589492\n" +
                "            ],\n" +
                "            \"end_latlng\": [\n" +
                "                30.459024,\n" +
                "                -97.5861\n" +
                "            ],\n" +
                "            \"elev_difference\": 10.2,\n" +
                "            \"distance\": 1230.7,\n" +
                "            \"points\": \"qdzxDjmsrQMCeAo@o@Yq@Ou@IaCVg@JcALyB\\\\gALc@@yAGgDc@}BUw@Ce@?s@Fg@F}@VeAd@K@IE_@{@Wa@mAsAmB}Bc@o@eBsDo@eAOa@Wg@\",\n" +
                "            \"starred\": false,\n" +
                "            \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/O6MNIRLXWY3VRKYFW7ESOPE57JEBNLI4JEU3A2ZWTVUU2JPOC3FTAYCQGSSSY2G34QZ7DC7YVI2LZ53VJF2VG===\",\n" +
                "            \"local_legend_enabled\": true\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 25307868,\n" +
                "            \"resource_state\": 2,\n" +
                "            \"name\": \"Hidden Lake Crossing East\",\n" +
                "            \"climb_category\": 0,\n" +
                "            \"climb_category_desc\": \"NC\",\n" +
                "            \"avg_grade\": -0.7,\n" +
                "            \"start_latlng\": [\n" +
                "                30.457252,\n" +
                "                -97.577446\n" +
                "            ],\n" +
                "            \"end_latlng\": [\n" +
                "                30.447441,\n" +
                "                -97.561989\n" +
                "            ],\n" +
                "            \"elev_difference\": 13.4,\n" +
                "            \"distance\": 1939.5,\n" +
                "            \"points\": \"yt{xD`bqrQt@U~DiB\\\\Wb@k@f@kA|BuG~BcGj@aAr@s@dAw@dB{ArC{Bt@o@bAu@zL{Jr@w@v@kAf@eAJ]XsALeAH{AToOFeAPcANo@bByE\",\n" +
                "            \"starred\": false,\n" +
                "            \"elevation_profile\": \"https://d3o5xota0a1fcr.cloudfront.net/v6/charts/JTNHSSOF46O2ADQGEAX6H3VQB6JIRT3MXP4PNVTWST4F6BSI5C3R5MATLDK5R7KRYGW22VD6PEAXMH7TPMYKS===\",\n" +
                "            \"local_legend_enabled\": true\n" +
                "        }\n" +
                "    ]\n" +
                "}");
    }

    private HttpRequestResponse getExpiredTokenResponse() {
        return new HttpRequestResponse(401, "Unauthorized", "{\n" +
                "    \"message\": \"Authorization Error\",\n" +
                "    \"errors\": [\n" +
                "        {\n" +
                "            \"resource\": \"Application\",\n" +
                "            \"field\": \"\",\n" +
                "            \"code\": \"invalid\"\n" +
                "        }\n" +
                "    ]\n" +
                "}");
    }
}
