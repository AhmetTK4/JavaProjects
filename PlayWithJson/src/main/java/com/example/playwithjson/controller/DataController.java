package com.example.playwithjson.controller;




import com.example.playwithjson.model.Entry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/data")
public class DataController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String DATA_FILE = "src/main/resources/data.json";

    @GetMapping
    public List<Entry> getAllEntries() throws IOException {
        return objectMapper.readValue(new File(DATA_FILE), new TypeReference<List<Entry>>() {});
    }

    @PostMapping
    public Entry addEntry(@RequestBody Entry entry) throws IOException {
        List<Entry> entries = getAllEntries();
        entry.setId(UUID.randomUUID().toString());
        entries.add(entry);
        objectMapper.writeValue(new File(DATA_FILE), entries);
        return entry;
    }

    @DeleteMapping("/{id}")
    public void deleteEntry(@PathVariable String id) throws IOException {
        List<Entry> entries = getAllEntries();
        entries.removeIf(entry -> entry.getId().equals(id));
        objectMapper.writeValue(new File(DATA_FILE), entries);
    }
}
