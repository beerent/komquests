package com.komquests.api.strava;

public enum StravaActivity {
    INVALID,
    RUNNING,
    CYCLING;

    public static String getValue(StravaActivity stravaActivity) {
        if (stravaActivity == RUNNING)
        {
            return "running";
        }

        return "riding";
    }

    public static StravaActivity getEnum(String activity) {
        if (activity.equals(getValue(RUNNING)))
        {
            return RUNNING;
        }

        if (activity.equals(getValue(CYCLING)))
        {
            return CYCLING;
        }

        return INVALID;
    }
}
