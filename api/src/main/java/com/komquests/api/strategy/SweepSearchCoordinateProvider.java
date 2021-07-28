package com.komquests.api.strategy;

import com.komquests.api.models.strava.location.Coordinates;
import com.komquests.api.models.strava.segment.SegmentSearchRequest;
import com.komquests.api.strava.coordinates.CoordinatesRangeCalculator;

import java.util.ArrayList;
import java.util.List;

public class SweepSearchCoordinateProvider {
    public List<SegmentSearchRequest> search(Coordinates initialCoordinates, double gridMiles, int gridCount) throws Exception {
        if (!isPerfectSquare(gridCount) || !isOddNumber(gridCount)) {
            throw new Exception("grid count is not an odd perfect square.");
        }

        List<SegmentSearchRequest> griddedSearchRequests = gridSearchRequests(initialCoordinates, gridMiles, gridCount);
        return griddedSearchRequests;
    }

    private List<SegmentSearchRequest> gridSearchRequests(Coordinates initialCoordinates, double gridMiles, int gridCount) {
        int columns = (int) Math.sqrt(gridCount);
        int columnsOnEachSide = (columns - 1) / 2;

        List<Coordinates> coordinates = new ArrayList<>();

        Coordinates currentCoordinates = initialCoordinates;
        coordinates.add(currentCoordinates);
        for (int i = 0; i < columnsOnEachSide; i++) {
            currentCoordinates = new CoordinatesRangeCalculator().calculateWestCoordinates(currentCoordinates, gridMiles);
            coordinates.add(0, currentCoordinates);
        }

        currentCoordinates = initialCoordinates;
        for (int i = 0; i < columnsOnEachSide; i++) {
            currentCoordinates = new CoordinatesRangeCalculator().calculateEastCoordinates(currentCoordinates, gridMiles);
            coordinates.add(currentCoordinates);
        }

        List<Coordinates> coordinatesAbove = new ArrayList<>();
        for (Coordinates coordinate : coordinates) {
        currentCoordinates = coordinate;
            for (int i = 0; i < columnsOnEachSide; i++) {
                currentCoordinates = new CoordinatesRangeCalculator().calculateNorthCoordinates(currentCoordinates, gridMiles);
                coordinatesAbove.add(currentCoordinates);
            }
        }

        List<Coordinates> coordinatesBelow = new ArrayList<>();
        for (Coordinates coordinate : coordinates) {
            currentCoordinates = coordinate;
            for (int i = 0; i < columnsOnEachSide; i++) {
                currentCoordinates = new CoordinatesRangeCalculator().calculateSouthCoordinates(currentCoordinates, gridMiles);
                coordinatesBelow.add(currentCoordinates);
            }
        }

        List<SegmentSearchRequest> segmentSearchRequests = new ArrayList<>();
        for (Coordinates coordinate : coordinatesAbove) {
            segmentSearchRequests.add(new SegmentSearchRequest(coordinate, gridMiles));
        }

        for (Coordinates coordinate : coordinates) {
            segmentSearchRequests.add(new SegmentSearchRequest(coordinate, gridMiles));
        }

        for (Coordinates coordinate : coordinatesBelow) {
            segmentSearchRequests.add(new SegmentSearchRequest(coordinate, gridMiles));
        }

        return segmentSearchRequests;
    }

    private boolean isPerfectSquare(int gridCount) {
        return Math.pow(Math.sqrt(gridCount), 2) == gridCount;
    }

    private boolean isOddNumber(int gridCount) {
        return gridCount % 2 == 1;
    }
}
