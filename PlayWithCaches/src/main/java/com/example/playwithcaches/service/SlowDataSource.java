package com.example.playwithcaches.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Simulates an expensive backend call (database, remote API...).
 */
@Component
public class SlowDataSource {

    private final Duration delay;

    public SlowDataSource(@Value("${app.data.simulated-delay:PT3S}") Duration delay) {
        this.delay = delay;
    }

    public String load(String param) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Data for: " + param;
    }
}
