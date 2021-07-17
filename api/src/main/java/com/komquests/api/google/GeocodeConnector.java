package com.komquests.api.google;

import com.google.gson.Gson;
import com.komquests.api.models.rest.HttpRequestResponse;
import com.komquests.api.models.strava.location.Coordinates;
import com.komquests.api.rest.RestService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GeocodeConnector {
    private static final String GEOCODE_ENDPOINT = "https://maps.googleapis.com/maps/api/geocode/json";

    private RestService restService;

    public GeocodeConnector(RestService restService) {
        this.restService = restService;
    }

    public Coordinates getCoordinates(String address) {
        Map<String, String> params = buildParams(address);
        HttpRequestResponse httpRequestResponse = this.restService.get(GEOCODE_ENDPOINT, params);
        return getCoordinatesFromResponse(httpRequestResponse.getBody());
    }

    private Coordinates getCoordinatesFromResponse(String response) {
        Map jsonMap = new Gson().fromJson(response, Map.class);

        String status = String.valueOf(jsonMap.get("status"));
        if (!isValidResponse(status)) {
            return null;
        }

        return getCoordinatesFromJsonMap(jsonMap);
    }

    private Coordinates getCoordinatesFromJsonMap(Map jsonMap) {
        Map location = (Map) ((Map) ((Map) ((ArrayList) jsonMap.get("results")).get(0)).get("geometry")).get("location");
        Double latitude = (Double) location.get("lat");
        Double longitude = (Double) location.get("lng");

        return new Coordinates(latitude, longitude);
    }

    private boolean isValidResponse(String status) {
        return "OK".equals(status);
    }

    private Map<String, String> buildParams(String address) {
        return new HashMap<String, String>() {{ put("address", address); }};
    }
}
