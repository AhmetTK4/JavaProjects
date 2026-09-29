package com.example.playwithjson.repository;

import com.example.playwithjson.model.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.jackson.databind.json.JsonMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class JsonFileEntryRepositoryTest {

    @TempDir
    Path tempDir;

    private Path dataFile;
    private JsonFileEntryRepository repository;

    @BeforeEach
    void setUp() {
        dataFile = tempDir.resolve("nested/entries.json");
        repository = new JsonFileEntryRepository(JsonMapper.builder().build(), dataFile);
    }

    @Test
    void createsDataFileFromSeedOnFirstStart() {
        assertTrue(Files.exists(dataFile));
        assertEquals(3, repository.findAll().size());
    }

    @Test
    void addedEntriesArePersistedAcrossInstances() {
        Entry added = repository.add("Zeynep");
        JsonFileEntryRepository reopened = new JsonFileEntryRepository(JsonMapper.builder().build(), dataFile);
        assertTrue(reopened.findAll().contains(added));
    }

    @Test
    void deleteReportsWhetherEntryExisted() {
        Entry added = repository.add("Zeynep");
        assertTrue(repository.deleteById(added.id()));
        assertFalse(repository.deleteById(added.id()));
        assertFalse(repository.findAll().contains(added));
    }

    @Test
    void concurrentAddsDoNotLoseUpdates() throws Exception {
        int writers = 50;
        try (ExecutorService executor = Executors.newFixedThreadPool(8)) {
            List<Future<Entry>> futures = IntStream.range(0, writers)
                    .mapToObj(i -> executor.submit(() -> repository.add("user-" + i)))
                    .toList();
            for (Future<Entry> future : futures) {
                future.get();
            }
        }
        assertEquals(3 + writers, repository.findAll().size());
    }
}
