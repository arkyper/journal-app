package com.arkyper.journalApp.controller;

import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arkyper.journalApp.entity.JournalEntry;
import com.arkyper.journalApp.entity.User;
import com.arkyper.journalApp.service.JournalEntryService;
import com.arkyper.journalApp.service.UserService;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {
    
    @Autowired 
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.findByUserName(userName);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<JournalEntry> allEntries = user.getJournalEntries();
        if(allEntries != null && !allEntries.isEmpty())
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

       try {
        journalEntryService.saveEntry(myEntry, userName);
        return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
       } catch(Exception e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       } 
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId myId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.findByUserName(userName);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<JournalEntry> collectEntries = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).toList();
        if(!collectEntries.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findEntryById(myId);
            if(journalEntry.isPresent())
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);  
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId myId, @RequestBody JournalEntry newEntry) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        
        User user = userService.findByUserName(userName);
        List<JournalEntry> collectEntries = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).toList();
        if(!collectEntries.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findEntryById(myId);
            if(journalEntry.isPresent()) {
                JournalEntry oldEntry = journalEntry.get();
                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());        
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }            
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId myId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean removed = false;
        removed = journalEntryService.deleteEntryById(myId, userName);
        if (removed) 
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}
