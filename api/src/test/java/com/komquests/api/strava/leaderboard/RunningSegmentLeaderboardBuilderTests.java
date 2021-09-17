package com.komquests.api.strava.leaderboard;

import com.komquests.api.models.strava.segment.leaderboard.RunningSegmentLeaderboardEntry;
import com.komquests.api.models.strava.segment.leaderboard.SegmentLeaderboard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RunningSegmentLeaderboardBuilderTests {
    @Test
    public void testCanBuildRunningLeaderboard() {
        RunningSegmentLeaderboardBuilder segmentLeaderboardBuilder = new RunningSegmentLeaderboardBuilder();
        SegmentLeaderboard<RunningSegmentLeaderboardEntry> segmentLeaderboard = segmentLeaderboardBuilder.build(getMockRunningSegmentLeaderboardResponse());

        assertEquals(segmentLeaderboard.getFirstPlace().getPace(), "4:15");
        assertEquals(segmentLeaderboard.getLastPlace().getPace(), "5:42");
    }

    private String getMockRunningSegmentLeaderboardResponse() {
        return "<table class='table table-striped table-leaderboard'><thead><tr><th>Rank</th><th class='name'>Name</th><th class='pace hidden-xs'>Pace</th><th class='time last-child'>Time</th></tr></thead><tbody><tr><td>1</td><td>Donnie ONeal</td><td class='hidden-xs'>4:15<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/5150877504\">42:17</a></td></tr><tr><td>2</td><td>Tyler Burkett</td><td class='hidden-xs'>4:28<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/4573252453\">44:34</a></td></tr><tr><td>3</td><td>Chad Andersen</td><td class='hidden-xs'>5:07<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/5109584637\">51:02</a></td></tr><tr><td>4</td><td>Statesman Capitol 10,000</td><td class='hidden-xs'>5:16<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/4199097233\">52:29</a></td></tr><tr><td>4</td><td>Cathy T.</td><td class='hidden-xs'>5:16<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/5108875588\">52:29</a></td></tr><tr><td>6</td><td>Christopher Thibert</td><td class='hidden-xs'>5:17<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/4199246289\">52:33</a></td></tr><tr><td>6</td><td>Christopher Thibert</td><td class='hidden-xs'>5:17<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/4199091668\">52:33</a></td></tr><tr><td>8</td><td>Chris Howard</td><td class='hidden-xs'>5:18<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/4370907976\">52:49</a></td></tr><tr><td>9</td><td>Mark Holcomb</td><td class='hidden-xs'>5:28<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/4343186819\">54:25</a></td></tr><tr><td>10</td><td>Matt Russo</td><td class='hidden-xs'>5:42<abbr class='unit' title='minutes per kilometer'>/km</abbr></td><td><a href=\"/activities/4356406275\">56:45</a></td></tr></tbody></table>";
    }
}
