package com.arkyper.journalApp.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arkyper.journalApp.entity.JournalEntry;
import com.arkyper.journalApp.repository.JournalEntryRepository;

@Service
public class JournalEntryService {
    
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public JournalEntry getEntryById(ObjectId id) {
        return journalEntryRepository.findById(id).orElse(null);
    }

    // public JournalEntry updateEntryById(ObjectId id, JournalEntry newEntry) {
    //     JournalEntry oldEntry = journalEntryRepository.findById(id).orElse(null);
    //     if (oldEntry != null) {
    //         oldEntry.setTitle(newEntry.getTitle() != null && newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
    //         oldEntry.setContent(newEntry.getContent() != null && newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());
                   
    //     }
    //     journalEntryRepository.save(oldEntry);
    //     return oldEntry;
    // }

    public void deleteEntryById(ObjectId id) {
        journalEntryRepository.deleteById(id);
    }
}
