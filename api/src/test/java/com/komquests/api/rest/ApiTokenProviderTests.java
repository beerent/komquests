package com.komquests.api.rest;

import org.junit.jupiter.api.Test;

public class ApiTokenProviderTests {
    @Test
    public void testProviderCanReturnApiKey() {
        String token = "api_token";
        ApiTokenProvider apiTokenProvider = new ApiTokenProvider(token);
        
    }
}
