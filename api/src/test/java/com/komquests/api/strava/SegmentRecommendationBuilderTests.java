package com.komquests.api.strava;

import com.komquests.api.models.strava.segment.Segment;
import com.komquests.api.models.strava.segment.leaderboard.SegmentLeaderboard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SegmentRecommendationBuilderTests {
    @Test
    public void testCanBuildSegmentRecommendations() {
        SegmentRecommendationBuilder segmentRecommendationBuilder = new SegmentRecommendationBuilder();
        List<Segment> segments = segmentRecommendationBuilder.build(getValidSegmentSearchResponse());

        assertEquals(10, segments.size());
        assertEquals(segments.get(0).getId(), 12800003);
        assertEquals(segments.get(9).getId(), 25307868);
    }

    private String getValidSegmentSearchResponse() {
        return "{\n" +
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
                "}";
    }
}
