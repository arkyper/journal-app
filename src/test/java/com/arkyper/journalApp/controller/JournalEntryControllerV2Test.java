package com.arkyper.journalApp.controller;

import com.arkyper.journalApp.entity.JournalEntry;
import com.arkyper.journalApp.entity.User;
import com.arkyper.journalApp.service.JournalEntryService;
import com.arkyper.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class JournalEntryControllerV2Test {

    @InjectMocks
    private JournalEntryControllerV2 journalEntryController;

    @Mock
    private JournalEntryService journalEntryService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("testuser", "password", "ROLE_USER"));
    }

    @Test
    void testGetAllJournalEntriesOfUser_whenEntriesExist() {
        User user = new User();
        List<JournalEntry> entries = new ArrayList<>();
        entries.add(new JournalEntry());
        user.setJournalEntries(entries);

        when(userService.findByUserName("testuser")).thenReturn(user);

        ResponseEntity<?> response = journalEntryController.getAllJournalEntriesOfUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(entries, response.getBody());
    }

    @Test
    void testGetAllJournalEntriesOfUser_whenNoEntriesExist() {
        User user = new User();
        user.setJournalEntries(new ArrayList<>());

        when(userService.findByUserName("testuser")).thenReturn(user);

        ResponseEntity<?> response = journalEntryController.getAllJournalEntriesOfUser();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllJournalEntriesOfUser_whenUserNotFound() {
        when(userService.findByUserName("testuser")).thenReturn(null);

        ResponseEntity<?> response = journalEntryController.getAllJournalEntriesOfUser();

 assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateEntry_success() {
        JournalEntry entry = new JournalEntry();
        User user = new User();

        when(userService.findByUserName("testuser")).thenReturn(user);
        doNothing().when(journalEntryService).saveEntry(any(JournalEntry.class), eq("testuser"));

        ResponseEntity<?> response = journalEntryController.createEntry(entry);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(entry, response.getBody());
    }

    @Test
    void testCreateEntry_badRequest() {
        JournalEntry entry = new JournalEntry();
        User user = new User();

        when(userService.findByUserName("testuser")).thenReturn(user);
        doThrow(new RuntimeException()).when(journalEntryService).saveEntry(any(JournalEntry.class), eq("testuser"));

        ResponseEntity<?> response = journalEntryController.createEntry(entry);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetJournalEntryById_whenEntryFound() {
        ObjectId entryId = new ObjectId();
        JournalEntry entry = new JournalEntry();
        entry.setId(entryId);
        User user = new User();
        user.getJournalEntries().add(entry);

        when(userService.findByUserName("testuser")).thenReturn(user);
        when(journalEntryService.findEntryById(entryId)).thenReturn(Optional.of(entry));

        ResponseEntity<?> response = journalEntryController.getJournalEntryById(entryId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(entry, response.getBody());
    }

    @Test
    void testGetJournalEntryById_whenEntryNotFoundInUserEntries() {
        ObjectId entryId = new ObjectId();
        User user = new User(); // User has no entries

        when(userService.findByUserName("testuser")).thenReturn(user);

        ResponseEntity<?> response = journalEntryController.getJournalEntryById(entryId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetJournalEntryById_whenUserNotFound() {
        ObjectId entryId = new ObjectId();

        when(userService.findByUserName("testuser")).thenReturn(null);

        ResponseEntity<?> response = journalEntryController.getJournalEntryById(entryId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetJournalEntryById_whenEntryNotFoundInService() {
        ObjectId entryId = new ObjectId();
        JournalEntry entry = new JournalEntry();
        entry.setId(entryId);
        User user = new User();
        user.getJournalEntries().add(entry);

        when(userService.findByUserName("testuser")).thenReturn(user);
        when(journalEntryService.findEntryById(entryId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = journalEntryController.getJournalEntryById(entryId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @SuppressWarnings("null")
    @Test
    void testUpdateEntry_success() {
        ObjectId entryId = new ObjectId();
        JournalEntry oldEntry = new JournalEntry();
        oldEntry.setId(entryId);
        oldEntry.setTitle("Old Title");
        oldEntry.setContent("Old Content");

        JournalEntry newEntry = new JournalEntry();
        newEntry.setTitle("New Title");
        newEntry.setContent("New Content");

        User user = new User();
        user.getJournalEntries().add(oldEntry);

        when(userService.findByUserName("testuser")).thenReturn(user);
        when(journalEntryService.findEntryById(entryId)).thenReturn(Optional.of(oldEntry));
        doNothing().when(journalEntryService).saveEntry(any(JournalEntry.class));

        ResponseEntity<?> response = journalEntryController.updateEntry(entryId, newEntry);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JournalEntry updatedEntry = (JournalEntry) response.getBody();
        assertEquals("New Title", updatedEntry.getTitle());
        assertEquals("New Content", updatedEntry.getContent());
    }

    @Test
    void testUpdateEntry_notFound() {
        // Set up the security context
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("testuser", "password", "ROLE_USER"));

        ObjectId entryId = new ObjectId();
        JournalEntry newEntry = new JournalEntry();

        User user = new User(); // User has no entries

        when(userService.findByUserName("testuser")).thenReturn(user);

        ResponseEntity<?> response = journalEntryController.updateEntry(entryId, newEntry);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteEntry_success() {
        ObjectId entryId = new ObjectId();
        when(journalEntryService.deleteEntryById(entryId, "testuser")).thenReturn(true);

        ResponseEntity<?> response = journalEntryController.deleteEntry(entryId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    

    @Test
    void testUpdateEntry_whenJournalEntryNotFoundInService() {
        ObjectId entryId = new ObjectId();
        JournalEntry oldEntry = new JournalEntry();
        oldEntry.setId(entryId);

        JournalEntry newEntry = new JournalEntry();
        newEntry.setTitle("New Title");
        newEntry.setContent("New Content");

        User user = new User();
        user.getJournalEntries().add(oldEntry);

        when(userService.findByUserName("testuser")).thenReturn(user);
        when(journalEntryService.findEntryById(entryId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = journalEntryController.updateEntry(entryId, newEntry);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

     @SuppressWarnings("null")
    @Test
    void testUpdateEntry_withNullTitleAndContent() {
        ObjectId entryId = new ObjectId();
        JournalEntry oldEntry = new JournalEntry();
        oldEntry.setId(entryId);
        oldEntry.setTitle("Old Title");
        oldEntry.setContent("Old Content");

        JournalEntry newEntry = new JournalEntry(); // Title and content are null by default

        User user = new User();
        user.getJournalEntries().add(oldEntry);

        when(userService.findByUserName("testuser")).thenReturn(user);
        when(journalEntryService.findEntryById(entryId)).thenReturn(Optional.of(oldEntry));
        doNothing().when(journalEntryService).saveEntry(any(JournalEntry.class));

        ResponseEntity<?> response = journalEntryController.updateEntry(entryId, newEntry);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        JournalEntry updatedEntry = (JournalEntry) response.getBody();
        assertEquals("Old Title", updatedEntry.getTitle()); // Should retain old title
        assertEquals("Old Content", updatedEntry.getContent()); // Should retain old content
    }
    @Test
    void testDeleteEntry_notFound() {
        ObjectId entryId = new ObjectId();
        when(journalEntryService.deleteEntryById(entryId, "testuser")).thenReturn(false);

        ResponseEntity<?> response = journalEntryController.deleteEntry(entryId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteEntry_whenUserNotFound() {
        ObjectId entryId = new ObjectId();
        when(journalEntryService.deleteEntryById(entryId, "testuser")).thenReturn(false); // Assuming deleteEntryById handles user not found internally and returns false

        ResponseEntity<?> response = journalEntryController.deleteEntry(entryId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}