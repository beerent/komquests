package com.komquests.api.strava;

import com.google.gson.Gson;
import com.komquests.api.models.strava.segment.Segment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SegmentRecommendationBuilder {
    public List<Segment> build(String response) {
        List<Segment> segments = new ArrayList<>();

        Map jsonMap = new Gson().fromJson(response, Map.class);
        List<Object> segmentObjects = (List<Object>) jsonMap.get("segments");
        for (int i = 0; i < segmentObjects.size(); i++) {
            Segment segment = createSegment((Map<Object, Object>) segmentObjects.get(i));
            segments.add(segment);
        }

        return segments;
    }

    private Segment createSegment(Map<Object, Object> segment) {
        return new Gson().fromJson(new Gson().toJson(segment), Segment.class);
    }
}
