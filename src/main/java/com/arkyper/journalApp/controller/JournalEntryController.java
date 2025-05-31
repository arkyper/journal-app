package com.arkyper.journalApp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.arkyper.journalApp.entity.JournalEntry;

@RestController
@RequestMapping("/_journal")
@Tag(name = "Journal-V1 APIs", description = "Get All Entries, Create Entry, Get Entry By Id, Update Entry, Delete Entry")
public class JournalEntryController {
    
    private Map<Long, JournalEntry> journalEntries = new HashMap<>();
    
    @GetMapping
    @Operation(summary = "Get All Entries", description = "Get All Entries")
    public List<JournalEntry> getAll() {
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping
    @Operation(summary = "Create Entry", description = "Create Entry")
    public boolean createEntry(@RequestBody JournalEntry myEntry) {
        // journalEntries.put(myEntry.getId(), myEntry);
        return true;
    }

    @GetMapping("/id/{myId}")
    @Operation(summary = "Get Entry By Id", description = "Get Entry By Id")
    public JournalEntry getJournalEntryById(@PathVariable long myId) {
        return journalEntries.get(myId);
    }

    @PutMapping("/id/{myId}")
    @Operation(summary = "Update Entry By Id", description = "Update Entry By Id")
    public boolean updateEntry(@PathVariable long myId, @RequestBody JournalEntry journalEntry) {
        journalEntries.put(myId, journalEntry);
        return true;
    }

    @DeleteMapping("/id/{myId}")
    @Operation(summary = "Delete Entry By Id", description = "Delete Entry By Id")
    public boolean deleteEntry(@PathVariable long myId) {
        journalEntries.remove(myId);
        return true;
    }

}
