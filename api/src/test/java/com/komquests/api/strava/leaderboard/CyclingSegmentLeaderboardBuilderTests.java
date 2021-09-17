package com.komquests.api.strava.leaderboard;

import com.komquests.api.models.strava.segment.leaderboard.CyclingSegmentLeaderboardEntry;
import com.komquests.api.models.strava.segment.leaderboard.SegmentLeaderboard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CyclingSegmentLeaderboardBuilderTests {

    @Test
    public void testCanBuildCyclingLeaderboard() {
        CyclingSegmentLeaderboardBuilder segmentLeaderboardBuilder = new CyclingSegmentLeaderboardBuilder();
        SegmentLeaderboard<CyclingSegmentLeaderboardEntry> segmentLeaderboard = segmentLeaderboardBuilder.build(getMockCyclingSegmentLeaderboardResponse());

        assertEquals(segmentLeaderboard.getFirstPlace().getPower(), 584);
        assertEquals(segmentLeaderboard.getLastPlace().getPower(), 366);
    }

    private String getMockCyclingSegmentLeaderboardResponse() {
        return "<table class='table table-striped table-leaderboard'><thead><tr><th>Rank</th><th class='name'>Name</th><th class='speed hidden-xs'>Speed</th><th class='power hidden-xs'>Power</th><th class='time last-child'>Time</th></tr></thead><tbody><tr><td>1</td><td>Ricardo C.</td><td class='hidden-xs'>50.3<abbr class='unit' title='kilometers per hour'>km/h</abbr></td><td class='hidden-xs text-nowrap'>584<abbr class='unit' title='watts'>W</abbr></td><td><a href=\"/activities/285620844\">23<abbr class='unit' title='second'>s</abbr></a></td></tr><tr><td>2</td><td>Scott M.</td><td class='hidden-xs'>48.2<abbr class='unit' title='kilometers per hour'>km/h</abbr></td><td class='hidden-xs text-nowrap'>443<abbr class='unit' title='watts'>W</abbr></td><td><a href=\"/activities/275103462\">24<abbr class='unit' title='second'>s</abbr></a></td></tr><tr><td>2</td><td>Shane M.</td><td class='hidden-xs'>48.2<abbr class='unit' title='kilometers per hour'>km/h</abbr></td><td class='hidden-xs text-nowrap'>450<abbr class='unit' title='watts'>W</abbr><img class=\"power-meter\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/powermeter-45fa2f9f06528441cf847a5b702990fbba60cde11a1ef1716feec0cd46a0dd69.png\" /></td><td><a href=\"/activities/2049098723\">24<abbr class='unit' title='second'>s</abbr></a></td></tr><tr><td>4</td><td>jade chavez</td><td class='hidden-xs'>44.5<abbr class='unit' title='kilometers per hour'>km/h</abbr></td><td class='hidden-xs text-nowrap'>-</td><td><a href=\"/activities/106181287\">26<abbr class='unit' title='second'>s</abbr></a></td></tr><tr><td>4</td><td>Simon Birk</td><td class='hidden-xs'>44.5<abbr class='unit' title='kilometers per hour'>km/h</abbr></td><td class='hidden-xs text-nowrap'>323<abbr class='unit' title='watts'>W</abbr></td><td><a href=\"/activities/2488280430\">26<abbr class='unit' title='second'>s</abbr></a></td></tr><tr><td>4</td><td>Cody Schroeder</td><td class='hidden-xs'>44.5<abbr class='unit' title='kilometers per hour'>km/h</abbr></td><td class='hidden-xs text-nowrap'>647<abbr class='unit' title='watts'>W</abbr></td><td><a href=\"/activities/3809570396\">26<abbr class='unit' title='second'>s</abbr></a></td></tr><tr><td>4</td><td>David Betterton</td><td class='hidden-xs'>44.5<abbr class='unit' title='kilometers per hour'>km/h</abbr></td><td class='hidden-xs text-nowrap'>588<abbr class='unit' title='watts'>W</abbr></td><td><a href=\"/activities/4583498154\">26<abbr class='unit' title='second'>s</abbr></a></td></tr><tr><td>8</td><td>• Dixon •</td><td class='hidden-xs'>42.8<abbr class='unit' title='kilometers per hour'>km/h</abbr></td><td class='hidden-xs text-nowrap'>-</td><td><a href=\"/activities/729340402\">27<abbr class='unit' title='second'>s</abbr></a></td></tr><tr><td>8</td><td>Cj H.</td><td class='hidden-xs'>42.8<abbr class='unit' title='kilometers per hour'>km/h</abbr></td><td class='hidden-xs text-nowrap'>297<abbr class='unit' title='watts'>W</abbr><img class=\"power-meter\" src=\"https://d3nn82uaxijpm6.cloudfront.net/assets/powermeter-45fa2f9f06528441cf847a5b702990fbba60cde11a1ef1716feec0cd46a0dd69.png\" /></td><td><a href=\"/activities/533772730\">27<abbr class='unit' title='second'>s</abbr></a></td></tr><tr><td>8</td><td>Jason Marlow</td><td class='hidden-xs'>42.8<abbr class='unit' title='kilometers per hour'>km/h</abbr></td><td class='hidden-xs text-nowrap'>366<abbr class='unit' title='watts'>W</abbr></td><td><a href=\"/activities/1576410\">27<abbr class='unit' title='second'>s</abbr></a></td></tr></tbody></table>";
    }
}
