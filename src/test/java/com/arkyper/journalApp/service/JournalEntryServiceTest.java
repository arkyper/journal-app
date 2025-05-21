package com.arkyper.journalApp.service;

import com.arkyper.journalApp.entity.JournalEntry;
import com.arkyper.journalApp.entity.User;
import com.arkyper.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JournalEntryServiceTest {

    @InjectMocks
    private JournalEntryService journalEntryService;

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEntryWithUser() {
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setTitle("Test Title");
        journalEntry.setContent("Test Content");

        User user = new User();
        user.setUserName("testuser");
        user.setJournalEntries(new ArrayList<>());

        when(userService.findByUserName("testuser")).thenReturn(user);
        when(journalEntryRepository.save(journalEntry)).thenReturn(journalEntry);

        journalEntryService.saveEntry(journalEntry, "testuser");

        verify(userService, times(1)).findByUserName("testuser");
        verify(journalEntryRepository, times(1)).save(journalEntry);
        verify(userService, times(1)).saveNewUser(user);
        assertEquals(1, user.getJournalEntries().size());
        assertTrue(user.getJournalEntries().contains(journalEntry));
        assertNotNull(journalEntry.getDate());
    }

    @Test
    void testSaveEntryWithoutUser() {
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setTitle("Test Title");
        journalEntry.setContent("Test Content");

        journalEntryService.saveEntry(journalEntry);

        verify(journalEntryRepository, times(1)).save(journalEntry);
        verifyNoInteractions(userService);
    }

    @Test
    void testGetAllEntries() {
        JournalEntry entry1 = new JournalEntry();
        entry1.setTitle("Title 1");
        JournalEntry entry2 = new JournalEntry();
        entry2.setTitle("Title 2");
        List<JournalEntry> journalEntries = Arrays.asList(entry1, entry2);

        when(journalEntryRepository.findAll()).thenReturn(journalEntries);

        List<JournalEntry> result = journalEntryService.getAllEntries();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(journalEntries));
        verify(journalEntryRepository, times(1)).findAll();
    }

    @Test
    void testFindEntryByIdFound() {
        ObjectId id = new ObjectId();
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setId(id);
        journalEntry.setTitle("Test Title");

        when(journalEntryRepository.findById(id)).thenReturn(Optional.of(journalEntry));

        Optional<JournalEntry> result = journalEntryService.findEntryById(id);

        assertTrue(result.isPresent());
        assertEquals(journalEntry, result.get());
        verify(journalEntryRepository, times(1)).findById(id);
    }

    @Test
    void testFindEntryByIdNotFound() {
        ObjectId id = new ObjectId();

        when(journalEntryRepository.findById(id)).thenReturn(Optional.empty());

        Optional<JournalEntry> result = journalEntryService.findEntryById(id);

        assertFalse(result.isPresent());
        verify(journalEntryRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteEntryByIdFoundAndRemoved() {
        ObjectId id = new ObjectId();
        String userName = "testuser";
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setId(id);

        User user = new User();
        user.setUserName(userName);
        user.setJournalEntries(new ArrayList<>(Arrays.asList(journalEntry)));

        when(userService.findByUserName(userName)).thenReturn(user);

        boolean result = journalEntryService.deleteEntryById(id, userName);

        assertTrue(result);
        verify(userService, times(1)).findByUserName(userName);
        verify(userService, times(1)).saveNewUser(user);
        verify(journalEntryRepository, times(1)).deleteById(id);
        assertEquals(0, user.getJournalEntries().size());
    }

    @Test
    void testDeleteEntryByIdFoundButNotRemovedFromUser() {
        ObjectId id = new ObjectId();
        String userName = "testuser";
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setId(new ObjectId()); // Different ID

        User user = new User();
        user.setUserName(userName);
        user.setJournalEntries(new ArrayList<>(Arrays.asList(journalEntry)));

        when(userService.findByUserName(userName)).thenReturn(user);

        boolean result = journalEntryService.deleteEntryById(id, userName);

        assertFalse(result);
        verify(userService, times(1)).findByUserName(userName);
        verify(userService, never()).saveNewUser(user);
        verify(journalEntryRepository, never()).deleteById(id);
        assertEquals(1, user.getJournalEntries().size());
    }

    @Test
    void testDeleteEntryByIdUserNotFound() {
        ObjectId id = new ObjectId();
        String userName = "nonexistentuser";

        when(userService.findByUserName(userName)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> journalEntryService.deleteEntryById(id, userName));

        verify(userService, times(1)).findByUserName(userName);
        verify(userService, never()).saveNewUser(any(User.class));
        verify(journalEntryRepository, never()).deleteById(any(ObjectId.class));
    }
}