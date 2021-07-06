package com.komquests.api.rest;

import java.util.Map;

public class QueryBuilder {
    public String addQueryParamsToUrl(String targetUrl, Map<String, String> queryParams) {
        if (queryParams.isEmpty()) {
            return targetUrl;
        }

        return addQueryToTargetUrl(targetUrl, queryParams);
    }

    private String addQueryToTargetUrl(String targetUrl, Map<String, String> queryParams) {
        if (queryParams.isEmpty()) {
            return targetUrl;
        }

        String query = "?";
        for (String key : queryParams.keySet()) {
            String value = queryParams.get(key);

            String and = "";
            if (query.length() > 1) {
                and = "&";
            }

            query += String.format("%s%s=%s", and, key, value);
        }

        return targetUrl + query;
    }
}
