package com.komquests.api.models.strava.segment;

import com.komquests.api.models.strava.location.Coordinates;
import com.komquests.api.strava.coordinates.CoordinatesRangeCalculator;

public class SegmentSearchRequest {
    private Coordinates coordinates;
    private Double range;

    public SegmentSearchRequest(Coordinates coordinates, Double range) {
        this.coordinates = coordinates;
        this.range = range;
    }

    public Coordinates getSouthWestCoordinates() {
        return new CoordinatesRangeCalculator().calculateSouthWestCoordinates(this.coordinates, this.range);
    }

    public Coordinates getNorthEastCoordinates() {
        return new CoordinatesRangeCalculator().calculateNorthEastCoordinates(this.coordinates, this.range);
    }
}
