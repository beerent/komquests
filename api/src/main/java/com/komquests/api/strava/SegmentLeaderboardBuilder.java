package com.komquests.api.strava;

import com.komquests.api.models.strava.segment_leaderboard.SegmentLeaderboard;
import com.komquests.api.models.strava.segment_leaderboard.SegmentLeaderboardEntry;

import java.util.*;

public class SegmentLeaderboardBuilder {
    public SegmentLeaderboard build(String rawLeaderboardData) {
        Queue<String> lines = getLineQueue(rawLeaderboardData);

        List<SegmentLeaderboardEntry> leaderboardEntries = new ArrayList<>();
        SegmentLeaderboardEntry segmentLeaderboardEntry = getNextSegmentLeaderboardEntry(lines);
        while (segmentLeaderboardEntry != null) {
            leaderboardEntries.add(segmentLeaderboardEntry);
            segmentLeaderboardEntry = getNextSegmentLeaderboardEntry(lines);
        }

        return new SegmentLeaderboard(leaderboardEntries);
    }

    private SegmentLeaderboardEntry getNextSegmentLeaderboardEntry(Queue<String> lines) {
        while (!lines.isEmpty()) {
            String line = lines.remove();

            if (!isStartLine(line)) {
                continue;
            }

            return getSegmentLeaderboardEntryFromStart(lines);
        }

        return null;
    }

    private SegmentLeaderboardEntry getSegmentLeaderboardEntryFromStart(Queue<String> lines) {
        String nameRow = lines.remove();
        String speedRow = lines.remove();
        lines.remove();
        String powerRow = lines.remove();
        if (lines.peek().contains("power-meter")) {
            lines.remove();
        }
        lines.remove();
        String timeRow = lines.remove();

        return buildLeaderboardFromRowData(nameRow, speedRow, powerRow, timeRow);
    }

    private SegmentLeaderboardEntry buildLeaderboardFromRowData(String nameRow, String speedRow, String powerRow, String timeRow) {
        String name = getNameFromNameRow(nameRow);
        String speed = getSpeedFromSpeedRow(speedRow);
        int power = getPowerFromPowerRow(powerRow);
        String time = getTimeFromTimeRow(timeRow);

        return new SegmentLeaderboardEntry(name, speed, power, time);
    }

    private String getNameFromNameRow(String row) {
        String startTrim = "<td>";
        String endTrim = "</td>";

        return getElementFromRow(row, startTrim, endTrim);
    }

    private String getSpeedFromSpeedRow(String row) {
        String startTrim = "<td class='hidden-xs'>";
        String endTrim = "<abbr class='unit' ";

        return getElementFromRow(row, startTrim, endTrim);
    }

    private int getPowerFromPowerRow(String row) {
        String startTrim = "";
        String endTrim = "<abbr class='unit' title='watts'>W</abbr>";

        String number = getElementFromRow(row, startTrim, endTrim);
        if (number == null) {
            return 0;
        }

        try {
            return Integer.parseInt(number);
        } catch (Exception e) {
        }

        return 0;
    }

    private String getTimeFromTimeRow(String row) {
        String startTrim = "\">";
        String endTrim = "</a></td>";

        return getElementFromRow(row, startTrim, endTrim);
    }

    private String getElementFromRow(String row, String startTrim, String endTrim) {
        if (!row.contains(startTrim) || !row.contains(endTrim)) {
            return null;
        }

        return row.substring(row.indexOf(startTrim) + startTrim.length(), row.indexOf(endTrim));
    }

    private boolean isStartLine(String line) {
        String startTrim = "<td>";
        String endTrim = "</td>";

        String number = getElementFromRow(line, startTrim, endTrim);
        if (number == null) {
            return false;
        }

        try {
            Integer.parseInt(number);
            return true;
        } catch (Exception e) {
        }

        return false;
    }

    private Queue<String> getLineQueue(String rawLeaderboardData) {
        String [] lines = rawLeaderboardData.split("\n");
        return new LinkedList<>(Arrays.asList(lines));
    }
}
