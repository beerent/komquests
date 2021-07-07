package com.komquests.api.strava.coordinates;

import com.komquests.api.models.strava.location.Coordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoordinatesRangeCalculatorTests {
    @Test
    public void testCanCalculateSouthWestCoordinates() {
        double originLatitude = 40.714;
        double originLongitude = -74.006;
        double miles = 1.75;

        Coordinates coordinates = new Coordinates(originLatitude, originLongitude);
        Coordinates updatedCoordinates = new CoordinatesRangeCalculator().calculateSouthWestCoordinates(coordinates, miles);
        assertTrue(updatedCoordinates.getLatitude() < coordinates.getLatitude());
        assertTrue(updatedCoordinates.getLongitude() < coordinates.getLongitude());
    }

    @Test
    public void testCanCalculateNorthEastCoordinates() {
        double originLatitude = 40.714;
        double originLongitude = -74.006;
        double miles = 1.75;

        Coordinates coordinates = new Coordinates(originLatitude, originLongitude);
        Coordinates updatedCoordinates = new CoordinatesRangeCalculator().calculateNorthEastCoordinates(coordinates, miles);
        assertTrue(updatedCoordinates.getLatitude() > coordinates.getLatitude());
        assertTrue(updatedCoordinates.getLongitude() > coordinates.getLongitude());
    }
}
