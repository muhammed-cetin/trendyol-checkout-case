package com.trendyol.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OutputWriter {

    /**
     * Writes the line to the file (append mode)
     *
     * @param filePath File path
     * @param line     Line to write
     */
    public void writeLine(String filePath, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to output file: " + filePath, e);
        }
    }

    /**
     * Clears the file (deletes its contents if any)
     *
     * @param filePath File path
     */
    public void clearFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to clear output file: " + filePath, e);
        }
    }
}
