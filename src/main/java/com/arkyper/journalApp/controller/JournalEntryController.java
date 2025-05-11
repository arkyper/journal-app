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

import com.arkyper.journalApp.entity.JournalEntry;

@RestController
@RequestMapping("/_journal")
public class JournalEntryController {
    
    private Map<Long, JournalEntry> journalEntries = new HashMap<>();
    
    @GetMapping
    public List<JournalEntry> getAll() {
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry myEntry) {
        // journalEntries.put(myEntry.getId(), myEntry);
        return true;
    }

    @GetMapping("/id/{myId}")
    public JournalEntry getJournalEntryById(@PathVariable long myId) {
        return journalEntries.get(myId);
    }

    @PutMapping("/id/{myId}")
    public boolean updateEntry(@PathVariable long myId, @RequestBody JournalEntry journalEntry) {
        journalEntries.put(myId, journalEntry);
        return true;
    }

    @DeleteMapping("/id/{myId}")
    public boolean deleteEntry(@PathVariable long myId) {
        journalEntries.remove(myId);
        return true;
    }

}
