package com.komquests.api.strava.models.segment_leaderboard;

import java.util.List;

public class SegmentLeaderboard {
    List<SegmentLeaderboardEntry> leaderboardEntries;

    public SegmentLeaderboard(List<SegmentLeaderboardEntry> leaderboardEntries) {
        this.leaderboardEntries = leaderboardEntries;
    }

    public SegmentLeaderboardEntry getFirstPlace() {
        return this.leaderboardEntries.get(0);
    }

    public SegmentLeaderboardEntry getLastPlace() {
        return this.leaderboardEntries.get(this.leaderboardEntries.size() - 1);
    }
}
