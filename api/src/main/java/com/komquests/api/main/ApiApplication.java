package com.komquests.api.main;

import com.google.gson.Gson;
import com.komquests.api.models.strava.segment.leaderboard.SegmentRecommendation;
import com.komquests.api.rest.endpoints.EndpointController;

import java.util.List;

public class ApiApplication {

    public static void main(String[] args) throws Exception {
        int watts = Integer.valueOf(args[0]);
        String address = args[1];

        List<SegmentRecommendation> recommendations = EndpointController.recommend(watts, address);
        System.out.println(new Gson().toJsonTree(recommendations));
    }
}
