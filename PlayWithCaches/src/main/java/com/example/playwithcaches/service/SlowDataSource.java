package com.example.playwithcaches.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simulates an expensive backend call (database, remote API...).
 */
@Component
public class SlowDataSource {

    private final Duration delay;

    public SlowDataSource(@Value("${app.data.simulated-delay:PT3S}") Duration delay) {
        this.delay = delay;
    }

    /** Values written through {@link #save}; other keys get a generated value. */
    private final Map<String, String> stored = new ConcurrentHashMap<>();

    public String load(String param) {
        simulateLatency();
        return stored.getOrDefault(param, "Data for: " + param);
    }

    public void save(String param, String value) {
        simulateLatency();
        stored.put(param, value);
    }

    /** Reference data (e.g. country names) that rarely changes. */
    public String loadReference(String key) {
        simulateLatency();
        return "Reference value for: " + key;
    }

    private void simulateLatency() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
