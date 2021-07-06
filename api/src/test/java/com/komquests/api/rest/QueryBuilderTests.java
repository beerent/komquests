package com.komquests.api.rest;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryBuilderTests {
    @Test
    public void testQueryBuilderAddZeroParametersToUrl() {
        String url = "url";
        Map<String, String> params = new HashMap<>();
        QueryBuilder queryBuilder = new QueryBuilder();
        String urlWithQuery = queryBuilder.addQueryParamsToUrl(url, params);

        assertEquals(url, urlWithQuery);
    }

    @Test
    public void testQueryBuilderAddOneParameterToUrl() {
        String url = "url";
        Map<String, String> params  = new HashMap<String, String>() {{ put("key", "value"); }};
        QueryBuilder queryBuilder = new QueryBuilder();
        String urlWithQuery = queryBuilder.addQueryParamsToUrl(url, params);

        assertEquals("url?key=value", urlWithQuery);
    }

    @Test
    public void testQueryBuilderAddMultipleParameterToUrl() {
        String url = "url";
        Map<String, String> params  = new HashMap<String, String>() {{ put("key1", "value1"); put("key2", "value2"); }};
        QueryBuilder queryBuilder = new QueryBuilder();
        String urlWithQuery = queryBuilder.addQueryParamsToUrl(url, params);

        assertEquals("url?key1=value1&key2=value2", urlWithQuery);
    }
}
