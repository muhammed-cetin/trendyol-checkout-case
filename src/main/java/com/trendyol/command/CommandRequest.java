package com.trendyol.command;

import java.util.Map;

public class CommandRequest {
    private String command;
    private Map<String, Object> payload;

    public CommandRequest() {
    }

    public CommandRequest(String command, Map<String, Object> payload) {
        this.command = command;
        this.payload = payload;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    // Helper methods to get values from payload
    public Integer getIntValue(String key) {
        Object value = payload != null ? payload.get(key) : null;
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }

    public Double getDoubleValue(String key) {
        Object value = payload != null ? payload.get(key) : null;
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return null;
    }
}
