package com.komquests.api.strava.leaderboard;

import com.komquests.api.models.strava.segment.leaderboard.SegmentLeaderboard;
import com.komquests.api.models.strava.segment.leaderboard.CyclingSegmentLeaderboardEntry;

import java.util.*;

public class CyclingSegmentLeaderboardBuilder {
    public SegmentLeaderboard build(String rawLeaderboardData) {
        Queue<String> lines = getLineQueue(rawLeaderboardData);

        List<CyclingSegmentLeaderboardEntry> leaderboardEntries = new ArrayList<>();
        CyclingSegmentLeaderboardEntry cyclingSegmentLeaderboardEntry = getNextSegmentLeaderboardEntry(lines);
        while (cyclingSegmentLeaderboardEntry != null) {
            leaderboardEntries.add(cyclingSegmentLeaderboardEntry);
            cyclingSegmentLeaderboardEntry = getNextSegmentLeaderboardEntry(lines);
        }

        return new SegmentLeaderboard(leaderboardEntries);
    }

    private CyclingSegmentLeaderboardEntry getNextSegmentLeaderboardEntry(Queue<String> lines) {
        while (!lines.isEmpty()) {
            String line = lines.remove();

            if (!isStartLine(line)) {
                continue;
            }

            return getSegmentLeaderboardEntryFromStart(lines);
        }

        return null;
    }

    private CyclingSegmentLeaderboardEntry getSegmentLeaderboardEntryFromStart(Queue<String> lines) {
        String nameRow = lines.remove();
        String speedRow = lines.remove();
        String powerRow = lines.remove();
        String timeRow = lines.remove();
        return buildLeaderboardFromRowData(nameRow, speedRow, powerRow, timeRow);
    }

    private CyclingSegmentLeaderboardEntry buildLeaderboardFromRowData(String nameRow, String speedRow, String powerRow, String timeRow) {
        String name = getNameFromNameRow(nameRow);
        String speed = getSpeedFromSpeedRow(speedRow);
        int power = getPowerFromPowerRow(powerRow);
        String time = getTimeFromTimeRow(timeRow);

        return new CyclingSegmentLeaderboardEntry(name, speed, power, time);
    }

    private String getNameFromNameRow(String row) {
        String startTrim = "<td>";
        String endTrim = "</td>";

        return getElementFromRow(row, startTrim, endTrim);
    }

    private String getSpeedFromSpeedRow(String row) {
        String startTrim = "<td> class='hidden-xs'>";
        String endTrim = "<abbr class='unit' ";

        return getElementFromRow(row, startTrim, endTrim);
    }

    private int getPowerFromPowerRow(String row) {
        String startTrim = "<td> class='hidden-xs text-nowrap'>";
        String endTrim = "<abbr class='unit' ";

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
        String endTrim = "<abbr class='unit' title='second'>s</abbr></a></td></tr>";
        String element = getElementFromRow(row, startTrim, endTrim);
        if (element == null) {
            startTrim = "\">";
            endTrim = "</a></td></tr>";
            element = getElementFromRow(row, startTrim, endTrim);
        }

        return element;
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
        Queue<String> lines = new LinkedList<>();

        String[] rows = rawLeaderboardData.split("<tr");
        for (String row : rows) {

            String[] cells = row.split("<td");
            for (String cell : cells) {
                cell = "<td>" + cell;
                cell = cell.replace("<td>>", "<td>");
                lines.add(cell);
            }
        }

        return lines;
    }
}
