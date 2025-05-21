package com.arkyper.journalApp.repository;

import com.arkyper.journalApp.entity.JournalEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
public class JournalEntryRepositoryTest {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    private JournalEntry testEntry;

    @BeforeEach
    void setUp() {
        testEntry = new JournalEntry();
        testEntry.setTitle("Test Title");
        testEntry.setContent("Test Content");
        testEntry.setDate(LocalDateTime.now());
        journalEntryRepository.save(testEntry);
    }

    @AfterEach
    void tearDown() {
        journalEntryRepository.deleteAll();
    }

    @Test
    void testSaveJournalEntry() {
        JournalEntry newEntry = new JournalEntry();
        newEntry.setTitle("New Title");
        newEntry.setContent("New Content");
        newEntry.setDate(LocalDateTime.now());
        JournalEntry savedEntry = journalEntryRepository.save(newEntry);

        assertNotNull(savedEntry.getId());
        assertEquals("New Title", savedEntry.getTitle());
        assertEquals("New Content", savedEntry.getContent());
    }

    @Test
    void testFindById() {
        Optional<JournalEntry> foundEntry = journalEntryRepository.findById(testEntry.getId());

        assertTrue(foundEntry.isPresent());
        assertEquals(testEntry.getTitle(), foundEntry.get().getTitle());
        assertEquals(testEntry.getContent(), foundEntry.get().getContent());
    }

    @Test
    void testFindAll() {
        JournalEntry secondEntry = new JournalEntry();
        secondEntry.setTitle("Second Title");
        secondEntry.setContent("Second Content");
        secondEntry.setDate(LocalDateTime.now());
        journalEntryRepository.save(secondEntry);

        List<JournalEntry> entries = journalEntryRepository.findAll();

        assertEquals(2, entries.size());
        assertTrue(entries.stream().anyMatch(entry -> entry.getTitle().equals("Test Title")));
        assertTrue(entries.stream().anyMatch(entry -> entry.getTitle().equals("Second Title")));
    }

    @Test
    void testDeleteById() {
        journalEntryRepository.deleteById(testEntry.getId());

        Optional<JournalEntry> foundEntry = journalEntryRepository.findById(testEntry.getId());
        assertFalse(foundEntry.isPresent());
    }

    @Test
    void testUpdateJournalEntry() {
        testEntry.setTitle("Updated Title");
        journalEntryRepository.save(testEntry);

        Optional<JournalEntry> updatedEntry = journalEntryRepository.findById(testEntry.getId());

        assertTrue(updatedEntry.isPresent());
        assertEquals("Updated Title", updatedEntry.get().getTitle());
    }
}