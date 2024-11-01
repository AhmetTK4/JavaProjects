package com.example.playwithcaches.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    @Cacheable(value = "dataCache")
    public String getData(String param) {
        simulateSlowService();
        return "Data for: " + param;
    }

    private void simulateSlowService() {
        try {
            Thread.sleep(3000); // 3 saniye bekleterek yavaş bir işlemi simüle ediyoruz
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
