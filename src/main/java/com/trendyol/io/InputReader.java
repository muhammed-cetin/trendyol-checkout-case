package com.trendyol.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputReader {

    /**
     * Reads all lines from file
     *
     * @param filePath File path
     * @return List of lines
     */
    public List<String> readLines(String filePath) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip blank lines
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read input file: " + filePath, e);
        }

        return lines;
    }
}
