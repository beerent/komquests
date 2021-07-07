package com.komquests.api.models.strava.segment.leaderboard;

public class SegmentLeaderboardEntry {
    String name;
    String speed;
    int power;
    String time;

    public SegmentLeaderboardEntry(String name, String speed, int power, String time) {
        this.name = name;
        this.speed = speed;
        this.power = power;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getSpeed() {
        return speed;
    }

    public int getPower() {
        return power;
    }

    public String getTime() {
        return time;
    }
}
