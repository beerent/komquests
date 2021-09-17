package com.komquests.api.strava.leaderboard;

import com.komquests.api.models.strava.segment.leaderboard.RunningSegmentLeaderboardEntry;
import com.komquests.api.models.strava.segment.leaderboard.SegmentLeaderboard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RunningSegmentLeaderboardBuilder {
    public SegmentLeaderboard build(String rawLeaderboardData) {
        Queue<String> lines = getLineQueue(rawLeaderboardData);

        List<RunningSegmentLeaderboardEntry> leaderboardEntries = new ArrayList<>();
        RunningSegmentLeaderboardEntry runningSegmentLeaderboardEntry = getNextSegmentLeaderboardEntry(lines);
        while (runningSegmentLeaderboardEntry != null) {
            leaderboardEntries.add(runningSegmentLeaderboardEntry);
            runningSegmentLeaderboardEntry = getNextSegmentLeaderboardEntry(lines);
        }

        return new SegmentLeaderboard(leaderboardEntries);
    }

    private RunningSegmentLeaderboardEntry getNextSegmentLeaderboardEntry(Queue<String> lines) {
        while (!lines.isEmpty()) {
            String line = lines.remove();

            if (!isStartLine(line)) {
                continue;
            }

            return getSegmentLeaderboardEntryFromStart(lines);
        }

        return null;
    }

    private RunningSegmentLeaderboardEntry getSegmentLeaderboardEntryFromStart(Queue<String> lines) {
        String nameRow = lines.remove();
        String paceRow = lines.remove();
        String timeRow = lines.remove();
        return buildLeaderboardFromRowData(nameRow, paceRow, timeRow);
    }

    private RunningSegmentLeaderboardEntry buildLeaderboardFromRowData(String nameRow, String speedRow, String timeRow) {
        String name = getNameFromNameRow(nameRow);
        String pace = getPaceFromPaceRow(speedRow);
        String time = getTimeFromTimeRow(timeRow);

        return new RunningSegmentLeaderboardEntry(name, pace, time);
    }

    private String getNameFromNameRow(String row) {
        String startTrim = "<td>";
        String endTrim = "</td>";

        return getElementFromRow(row, startTrim, endTrim);
    }

    private String getPaceFromPaceRow(String row) {
        String startTrim = "<td> class='hidden-xs'>";
        String endTrim = "<abbr class='unit' ";

        return getElementFromRow(row, startTrim, endTrim);
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
