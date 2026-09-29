package com.example.playwithjson.controller;

import com.example.playwithjson.model.Entry;
import com.example.playwithjson.model.EntryRequest;
import com.example.playwithjson.repository.JsonFileEntryRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataController {

    private final JsonFileEntryRepository repository;

    public DataController(JsonFileEntryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Entry> getAllEntries() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Entry> addEntry(@Valid @RequestBody EntryRequest request) {
        Entry entry = repository.add(request.name().trim());
        return ResponseEntity
                .created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(entry.id()))
                .body(entry);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntry(@PathVariable String id) {
        if (!repository.deleteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry " + id + " not found");
        }
    }
}
