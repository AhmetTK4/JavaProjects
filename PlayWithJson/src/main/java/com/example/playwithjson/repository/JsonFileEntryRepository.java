package com.example.playwithjson.repository;

import com.example.playwithjson.model.Entry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Stores entries in a JSON file outside the application's sources.
 * A read/write lock makes each read-modify-write cycle atomic within this process,
 * and writes go to a temporary file that replaces the data file in one move.
 */
@Repository
public class JsonFileEntryRepository {

    private static final TypeReference<List<Entry>> ENTRY_LIST = new TypeReference<>() {
    };

    private final JsonMapper jsonMapper;
    private final Path dataFile;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public JsonFileEntryRepository(JsonMapper jsonMapper, @Value("${app.data-file}") Path dataFile) {
        this.jsonMapper = jsonMapper;
        this.dataFile = dataFile.toAbsolutePath();
        initialise();
    }

    public List<Entry> findAll() {
        lock.readLock().lock();
        try {
            return List.copyOf(read());
        } finally {
            lock.readLock().unlock();
        }
    }

    public Entry add(String name) {
        lock.writeLock().lock();
        try {
            List<Entry> entries = new ArrayList<>(read());
            Entry entry = new Entry(UUID.randomUUID().toString(), name);
            entries.add(entry);
            write(entries);
            return entry;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * @return {@code true} if an entry with this id existed and was removed
     */
    public boolean deleteById(String id) {
        lock.writeLock().lock();
        try {
            List<Entry> entries = new ArrayList<>(read());
            boolean removed = entries.removeIf(entry -> id.equals(entry.id()));
            if (removed) {
                write(entries);
            }
            return removed;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /** Creates the data file from the bundled seed data on first start. */
    private void initialise() {
        if (Files.exists(dataFile)) {
            return;
        }
        try (InputStream seed = new ClassPathResource("seed-data.json").getInputStream()) {
            Files.createDirectories(dataFile.getParent());
            Files.copy(seed, dataFile);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot create data file " + dataFile, e);
        }
    }

    private List<Entry> read() {
        return jsonMapper.readValue(dataFile.toFile(), ENTRY_LIST);
    }

    private void write(List<Entry> entries) {
        try {
            Path temp = Files.createTempFile(dataFile.getParent(), "entries", ".tmp");
            jsonMapper.writeValue(temp.toFile(), entries);
            Files.move(temp, dataFile, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write data file " + dataFile, e);
        }
    }
}
