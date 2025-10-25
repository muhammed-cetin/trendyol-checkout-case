package com.trendyol.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trendyol.command.CommandRequest;
import com.trendyol.command.CommandResponse;

public class JsonMapper {

    private final Gson gson;

    public JsonMapper() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    /**
     * JSON string to CommandRequest
     *
     * @param json JSON string
     * @return CommandRequest object
     */
    public CommandRequest parseRequest(String json) {
        try {
            return gson.fromJson(json, CommandRequest.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON request: " + e.getMessage(), e);
        }
    }

    /**
     * CommandResponse to JSON string
     *
     * @param response CommandResponse object
     * @return JSON string
     */
    public String toJson(CommandResponse response) {
        try {
            return gson.toJson(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert response to JSON: " + e.getMessage(), e);
        }
    }
}
