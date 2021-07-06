package com.komquests.api.google;

import com.komquests.api.models.strava.location.Coordinates;
import com.komquests.api.rest.RestService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GeocodeConnectorTests {
    @Test
    public void testGeocodeConnectorReceivesCoordinatesFromValidAddress() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(getValidResponse());

        GeocodeConnector geocodeConnector = new GeocodeConnector(restService);

        String address = "78660";
        Coordinates coordinates = geocodeConnector.getCoordinates(address);
        assertEquals(coordinates.getLongitude(), "12345.54321");
        assertEquals(coordinates.getLatitude(), "-12345.54321");
    }

    @Test
    public void testGeocodeConnectorReceivesCoordinatesFromInvalidAddress() {
        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.get(Mockito.anyString())).thenReturn(getInvalidResponse());

        GeocodeConnector geocodeConnector = new GeocodeConnector(restService);

        String address = "-1";
        Coordinates coordinates = geocodeConnector.getCoordinates(address);
        assertNull(coordinates);
    }

    private String getValidResponse() {
        return "{\n" +
                "    \"results\": [\n" +
                "        {\n" +
                "            \"address_components\": [\n" +
                "                {\n" +
                "                    \"long_name\": \"78660\",\n" +
                "                    \"short_name\": \"78660\",\n" +
                "                    \"types\": [\n" +
                "                        \"postal_code\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"long_name\": \"Pflugerville\",\n" +
                "                    \"short_name\": \"Pflugerville\",\n" +
                "                    \"types\": [\n" +
                "                        \"locality\",\n" +
                "                        \"political\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"long_name\": \"Travis County\",\n" +
                "                    \"short_name\": \"Travis County\",\n" +
                "                    \"types\": [\n" +
                "                        \"administrative_area_level_2\",\n" +
                "                        \"political\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"long_name\": \"Texas\",\n" +
                "                    \"short_name\": \"TX\",\n" +
                "                    \"types\": [\n" +
                "                        \"administrative_area_level_1\",\n" +
                "                        \"political\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"long_name\": \"United States\",\n" +
                "                    \"short_name\": \"US\",\n" +
                "                    \"types\": [\n" +
                "                        \"country\",\n" +
                "                        \"political\"\n" +
                "                    ]\n" +
                "                }\n" +
                "            ],\n" +
                "            \"formatted_address\": \"Pflugerville, TX 78660, USA\",\n" +
                "            \"geometry\": {\n" +
                "                \"bounds\": {\n" +
                "                    \"northeast\": {\n" +
                "                        \"lat\": 30.501825,\n" +
                "                        \"lng\": -97.5218208\n" +
                "                    },\n" +
                "                    \"southwest\": {\n" +
                "                        \"lat\": 30.3782629,\n" +
                "                        \"lng\": -97.6701679\n" +
                "                    }\n" +
                "                },\n" +
                "                \"location\": {\n" +
                "                    \"lat\": -12345.54321,\n" +
                "                    \"lng\": 12345.54321\n" +
                "                },\n" +
                "                \"location_type\": \"APPROXIMATE\",\n" +
                "                \"viewport\": {\n" +
                "                    \"northeast\": {\n" +
                "                        \"lat\": 30.501825,\n" +
                "                        \"lng\": -97.5218208\n" +
                "                    },\n" +
                "                    \"southwest\": {\n" +
                "                        \"lat\": 30.3782629,\n" +
                "                        \"lng\": -97.6701679\n" +
                "                    }\n" +
                "                }\n" +
                "            },\n" +
                "            \"place_id\": \"ChIJGebrSXjFRIYRszNVuetxeAI\",\n" +
                "            \"types\": [\n" +
                "                \"postal_code\"\n" +
                "            ]\n" +
                "        }\n" +
                "    ],\n" +
                "    \"status\": \"OK\"\n" +
                "}";
    }

    private String getInvalidResponse() {
        return "{\n" +
                "    \"results\": [],\n" +
                "    \"status\": \"ZERO_RESULTS\"\n" +
                "}";
    }
}
