package com.komquests.api.models.rest;

public class HttpRequestResponse {
    private Integer code;
    private String message;
    private String body;

    public HttpRequestResponse(Integer code, String message, String body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getBody() {
        return this.body;
    }

}
