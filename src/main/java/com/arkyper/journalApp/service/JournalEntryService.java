package com.arkyper.journalApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.arkyper.journalApp.entity.JournalEntry;
import com.arkyper.journalApp.entity.User;
import com.arkyper.journalApp.repository.JournalEntryRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;


    // @Transactional - ensures that each step gets executed successfully if any
    // step fails then it'll rollback previous execution
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(savedEntry);
            userService.saveNewUser(user);
        } catch (Exception e) {
            log.error("Exception ", e);
        }
    }

    public void saveEntry(JournalEntry journalEntry) {
        try {
            journalEntryRepository.save(journalEntry);
        } catch (Exception e) {
            log.error("Exception ", e);
        }
    }

    public List<JournalEntry> getAllEntries() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findEntryById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteEntryById(ObjectId id, String userName) {
        boolean ifRemoved = false;
        try {
            User user = userService.findByUserName(userName);
            ifRemoved = user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));
            if(ifRemoved) {
                userService.saveNewUser(user);
                journalEntryRepository.deleteById(id);
            }    
        } catch(Exception e) {
            System.out.println(e);
            throw new RuntimeException("An Error occurred while deleting the entry.", e);
        }
        return ifRemoved;
    }

    
}
