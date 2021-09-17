package com.komquests.api.models.strava.segment.leaderboard;

import java.util.List;

public class SegmentLeaderboard <T> {
    List<T> leaderboardEntries;

    public SegmentLeaderboard(List<T> leaderboardEntries) {
        this.leaderboardEntries = leaderboardEntries;
    }

    public T getFirstPlace() {
        return this.leaderboardEntries.get(0);
    }

    public T getLastPlace() {
        return this.leaderboardEntries.get(this.leaderboardEntries.size() - 1);
    }
}
