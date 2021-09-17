package com.komquests.api.models.strava.segment.leaderboard;

public class RunningSegmentLeaderboardEntry {
    private final String name;
    private final String pace;
    private final String time;

    public RunningSegmentLeaderboardEntry(String name, String pace, String time) {
        this.name = name;
        this.pace = pace;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getPace() {
        return pace;
    }
    public String getTime() {
        return this.time;
    }
}
