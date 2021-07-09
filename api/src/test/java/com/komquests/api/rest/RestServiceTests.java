package com.komquests.api.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RestServiceTests {
    @Test
    public void testRestServiceRetriesWhenExpiredTokenFoundAndRefreshIsSupported() {
        //ApiTokenRetriever apiTokenRetriever = Mockito.mock(ApiTokenRetriever.class);
        //ApiTokenProvider apiTokenProvider = new ApiTokenProvider(apiTokenRetriever, AuthenticationType.QUERY);

        //RestService restService = Mockito.spy(new RestService(apiTokenProvider));
        //Mockito.when(restService.get(Mockito.anyString())).thenReturn(getInvalid)
    }
}
