package com.komquests.api.rest;

import com.komquests.api.models.rest.ApiToken;
import com.komquests.api.models.rest.HttpRequestResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class StravaApiTokenRetrieverTests {
    @Test
    public void testReceiverCanFetchApiToken() {
        String unexpectedApiToken = "unexpected_api_token";
        String expectedApiToken = "expected_api_token";

        ApiToken apiToken = new ApiToken(unexpectedApiToken, AuthenticationType.BEARER);
        RestService restService = Mockito.spy(new RestService(apiToken));
        Mockito.doReturn(new HttpRequestResponse(0, "", getFetchApiResponse())).when(restService).post(Mockito.anyString(), Mockito.anyMap());

        StravaApiTokenRetriever stravaApiTokenRetriever = new StravaApiTokenRetriever(restService, "", "", "");
        String updatedApiToken = stravaApiTokenRetriever.fetch();
        assertEquals(expectedApiToken, updatedApiToken);
    }

    private String getFetchApiResponse() {
        return "{\n" +
                "    \"token_type\": \"Bearer\",\n" +
                "    \"access_token\": \"expected_api_token\",\n" +
                "    \"expires_at\": 1626489954,\n" +
                "    \"expires_in\": 20679,\n" +
                "    \"refresh_token\": \"fa6c2f26147f691389dbe04c33bf9b58b5bab622\"\n" +
                "}";
    }
}
