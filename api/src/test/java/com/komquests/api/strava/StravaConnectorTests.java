package com.komquests.api.strava;

import com.komquests.api.rest.RestService;
import com.komquests.api.strava.models.segment.Segment;
import com.komquests.api.strava.models.segment_leaderboard.SegmentLeaderboard;
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

    @Test
    public void testGetSegmentLeaderboard() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(getValidLeaderboardResponse());

        StravaConnector stravaConnector = new StravaConnector(restService);
        SegmentLeaderboard segmentLeaderboard = stravaConnector.getSegmentLeaderboard(1);
        //assertNull(segment);
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

    public String getValidLeaderboardResponse() {
        return "<!-- Orion-App Layout -->\n" +
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
                "</html>\n";
    }
}
