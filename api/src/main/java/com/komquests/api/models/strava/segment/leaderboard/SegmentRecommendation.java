package com.komquests.api.models.strava.segment.leaderboard;

import com.komquests.api.models.strava.segment.Segment;

public class SegmentRecommendation {
    private final Segment segment;
    private final SegmentLeaderboard segmentLeaderboard;
    private final double miles;

    public SegmentRecommendation(Segment segment, SegmentLeaderboard segmentLeaderboard, double miles) {
        this.segment = segment;
        this.segmentLeaderboard = segmentLeaderboard;
        this.miles = miles;
    }
}
