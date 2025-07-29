package net.sparkly.server.config;

import java.util.Map;

public enum Config {
    
    SERVER("server.yml"),
    MESSAGES("messages.yml");
    
    private final String path;
    private Map<String, Object> configValues;
    
    Config(String path) {
        this.path = path;
    }
    
    public String path() {
        return path;
    }
    
    public Map<String, Object> configValues() {
        return configValues;
    }
    
    public void loadValues(Map<String, Object> configValues) {
        this.configValues = configValues;
    }
}
