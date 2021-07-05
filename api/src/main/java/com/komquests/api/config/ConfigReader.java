package com.komquests.api.config;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {
    private Map<String, String> contents;

    public ConfigReader(String contents) {
        this.contents = new HashMap<>();
        populateContents(contents);
    }

    public ConfigReader(File file) {
        this.contents = new HashMap<>();

        if (!file.exists()) {
            return;
        }

        String contents = readLines(file);
        populateContents(contents);
    }

    public String getValue(String key) {
        return this.contents.get(key);
    }

    private void populateContents(String contents) {
        String [] pairs = contents.split("\n");
        for (String pair : pairs) {
            if (!isValidConfigLine(pair)) {
                continue;
            }

            String[] splitPair = pair.split("=");
            String pairKey = splitPair[0];
            String pairValue = splitPair[1];
            this.contents.put(pairKey, pairValue);
        }
    }

    private boolean isValidConfigLine(String pair) {
        String[] splitPair = pair.split("=");
        return splitPair.length == 2;
    }

    private String readLines(File file) {
        StringBuilder lines = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines.toString();
    }
}
