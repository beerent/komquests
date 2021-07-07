package com.komquests.api.strava.coordinates;

import com.komquests.api.models.strava.location.Coordinates;
import org.geotools.referencing.GeodeticCalculator;

import java.awt.geom.Point2D;

public class CoordinatesRangeCalculator {
    private static double NORTH_AZIMUTH = 0;
    private static double EAST_AZIMUTH = 90;
    private static double SOUTH_AZIMUTH = 180;
    private static double WEST_AZIMUTH = 270;


    public Coordinates calculateSouthWestCoordinates(Coordinates coordinates, double miles) {
        double meters = milesToMeters(miles);

        Coordinates coordinatesUpdatedNorth = moveCoordinatesInDirection(coordinates, meters, SOUTH_AZIMUTH);
        Coordinates coordinatesUpdatedWest = moveCoordinatesInDirection(coordinatesUpdatedNorth, meters, WEST_AZIMUTH);

        return coordinatesUpdatedWest;
    }

    public Coordinates calculateNorthEastCoordinates(Coordinates coordinates, double miles) {
        double meters = milesToMeters(miles);

        Coordinates coordinatesUpdatedNorth = moveCoordinatesInDirection(coordinates, meters, NORTH_AZIMUTH);
        Coordinates coordinatesUpdatedWest = moveCoordinatesInDirection(coordinatesUpdatedNorth, meters, EAST_AZIMUTH);

        return coordinatesUpdatedWest;
    }

    public Coordinates calculateWestCoordinates(Coordinates coordinates, double miles) {
        double meters = milesToMeters(miles);
        return moveCoordinatesInDirection(coordinates, meters, WEST_AZIMUTH);
    }

    private Coordinates moveCoordinatesInDirection(Coordinates startCoordinates, double distance, double direction) {
        Double latitude = startCoordinates.getLatitude();
        Double longitude = startCoordinates.getLongitude();

        GeodeticCalculator calc = new GeodeticCalculator();
        calc.setStartingGeographicPoint(longitude, latitude);
        calc.setDirection(direction, distance);
        Point2D dest = calc.getDestinationGeographicPoint();

        return new Coordinates(dest.getY(), dest.getX());
    }

    private double milesToMeters(double miles) {
        return miles * 1609.34;
    }
}
