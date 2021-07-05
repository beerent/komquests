package com.komquests.api.strava;

import com.komquests.api.strava.models.segment_leaderboard.SegmentLeaderboard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SegmentLeaderboardBuilderTests {

    @Test
    public void testCanBuildLeaderboard() {
        SegmentLeaderboardBuilder segmentLeaderboardBuilder = new SegmentLeaderboardBuilder();
        SegmentLeaderboard segmentLeaderboard = segmentLeaderboardBuilder.build(getSegmentLeaderboardResponse());

        assertEquals(segmentLeaderboard.getFirstPlace().getPower(), 633);
        assertEquals(segmentLeaderboard.getLastPlace().getPower(), 528);
    }


    private String getSegmentLeaderboardResponse() {
        return "\n" +
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
                "\n";
    }
}
