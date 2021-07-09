package com.komquests.api.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class StravaApiTokenRetrieverTests {
    @Test
    public void testReceiverCanFetchApiToken() {
        String expectedApiToken = "expected_api_token";

        RestService restService = Mockito.mock(RestService.class);
        Mockito.when(restService.post(Mockito.anyString())).thenReturn(expectedApiToken);

        StravaApiTokenRetriever stravaApiTokenRetriever = new StravaApiTokenRetriever(restService);
        String apiToken = stravaApiTokenRetriever.fetch();
        assertEquals(expectedApiToken, apiToken);
    }

    @Test
    public void testReceiverCanIdentifyExpiredToken() {
        RestService restService = Mockito.mock(RestService.class);
        StravaApiTokenRetriever stravaApiTokenRetriever = new StravaApiTokenRetriever(restService);

        assertTrue(stravaApiTokenRetriever.isExpired(getExpiredTokenResponse()));
    }

    @Test
    public void testReceiverCanIdentifyUnexpiredToken() {
        RestService restService = Mockito.mock(RestService.class);
        StravaApiTokenRetriever stravaApiTokenRetriever = new StravaApiTokenRetriever(restService);

        assertFalse(stravaApiTokenRetriever.isExpired(""));
    }

    private String getExpiredTokenResponse() {
        return "{\n" +
                "    \"message\": \"Authorization Error\",\n" +
                "    \"errors\": [\n" +
                "        {\n" +
                "            \"resource\": \"Application\",\n" +
                "            \"field\": \"\",\n" +
                "            \"code\": \"invalid\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
