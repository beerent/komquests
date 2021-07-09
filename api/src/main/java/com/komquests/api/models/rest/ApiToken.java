package com.komquests.api.models.rest;

import com.komquests.api.rest.AuthenticationType;

public class ApiToken {
    private String token;
    private final AuthenticationType authenticationType;

    public ApiToken(String token, AuthenticationType authenticationType) {
        this.token = token;
        this.authenticationType = authenticationType;
    }

    public String getToken() {
        return this.token;
    }

    public AuthenticationType getAuthenticationType() {
        return this.authenticationType;
    }

    public void setApiToken(String token) {
        this.token = token;
    }
}
