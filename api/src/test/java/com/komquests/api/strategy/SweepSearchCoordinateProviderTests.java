package com.komquests.api.strategy;

import com.komquests.api.models.strava.location.Coordinates;
import com.komquests.api.models.strava.segment.SegmentSearchRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SweepSearchCoordinateProviderTests {
    @Test
    public void testSweepSearchThrowsExceptionForNonPerfectSquareGridCount() {
        SweepSearchCoordinateProvider sweepSearchCoordinateProvider = new SweepSearchCoordinateProvider();

        boolean exceptionThrown = false;
        try {
            sweepSearchCoordinateProvider.search(new Coordinates(100d, 100d), 2, 3);
        } catch(Exception e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    @Test
    public void testSweepSearchThrowsExceptionForEvenPerfectSquareGridCount() {
        SweepSearchCoordinateProvider sweepSearchCoordinateProvider = new SweepSearchCoordinateProvider();

        boolean exceptionThrown = false;
        try {
            sweepSearchCoordinateProvider.search(new Coordinates(100d, 100d), 2, 4);
        } catch(Exception e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    @Test
    public void testSweepSearchReturnsCorrectSizeDependingOnGridCount() throws Exception {
        SweepSearchCoordinateProvider sweepSearchCoordinateProvider = new SweepSearchCoordinateProvider();

        assertEquals(9, sweepSearchCoordinateProvider.search(new Coordinates(80d, 80d), 2, 9).size());
        assertEquals(25, sweepSearchCoordinateProvider.search(new Coordinates(80d, 80d), 2, 25).size());
    }

    @Test
    public void testSweepSearchReturnsCoordinatesWithExpectedIncreasingLatitude() throws Exception {
        SweepSearchCoordinateProvider sweepSearchCoordinateProvider = new SweepSearchCoordinateProvider();

        List<SegmentSearchRequest> segmentSearchRequests = sweepSearchCoordinateProvider.search(new Coordinates(80d, 80d), 2, 9);
        for (List<SegmentSearchRequest> list : partition(segmentSearchRequests, 3)) {
            SegmentSearchRequest previousSegmentSearchRequest = list.get(0);
            for (int i = 1; i < 3; i++) {
                SegmentSearchRequest currentSegmentSearchRequest = list.get(i);
                assertTrue(currentSegmentSearchRequest.getNorthEastCoordinates().getLongitude() > previousSegmentSearchRequest.getNorthEastCoordinates().getLongitude());
                previousSegmentSearchRequest = currentSegmentSearchRequest;
            }
        }
    }

    static <T> List<List<T>> partition(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<List<T>>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<T>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }
}
