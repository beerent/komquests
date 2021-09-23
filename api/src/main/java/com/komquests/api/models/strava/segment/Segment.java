package com.komquests.api.models.strava.segment;

public class Segment {
    private int id;
    private String name;
    private Double distance;
    private Double avg_grade;
    private Double elev_difference;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDistance() {
        return this.distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getAverageGrade() {
        return this.avg_grade;
    }

    public void setAverageGrade(Double averageGrade) {
        this.avg_grade = averageGrade;
    }

    public Double getElevationDifference() {
        return this.elev_difference;
    }

    public void setElevationDifference(Double elev_difference) {
        this.elev_difference = elev_difference;
    }
}
