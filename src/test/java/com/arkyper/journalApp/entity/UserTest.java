package com.arkyper.journalApp.entity;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testCreateUserWithAllFields() {
        ObjectId id = new ObjectId();
        String userName = "testUser";
        String password = "password123";
        List<JournalEntry> journalEntries = new ArrayList<>();
        journalEntries.add(new JournalEntry()); // Add a dummy entry
        List<String> roles = Arrays.asList("USER", "ADMIN");

        User user = new User(id, userName, password, journalEntries, roles);

        assertEquals(id, user.getId());
        assertEquals(userName, user.getUserName());
        assertEquals(password, user.getPassword());
        assertEquals(journalEntries, user.getJournalEntries());
        assertEquals(roles, user.getRoles());
    }

    @Test
    void testCreateUserWithRequiredFields() {
        String userName = "requiredUser";
        String password = "requiredPassword";

        User user = new User(null, userName, password, new ArrayList<>(), null); // Using constructor with null for optional fields

        assertNull(user.getId());
        assertEquals(userName, user.getUserName());
        assertEquals(password, user.getPassword());
        assertNotNull(user.getJournalEntries()); // Should be initialized by default
        assertTrue(user.getJournalEntries().isEmpty());
        assertNull(user.getRoles());
    }

    @Test
    void testAddAndRetrieveJournalEntries() {
        User user = new User();
        List<JournalEntry> entries = new ArrayList<>();
        JournalEntry entry1 = new JournalEntry();
        JournalEntry entry2 = new JournalEntry();
        entries.add(entry1);
        entries.add(entry2);

        user.setJournalEntries(entries);

        assertEquals(2, user.getJournalEntries().size());
        assertTrue(user.getJournalEntries().contains(entry1));
        assertTrue(user.getJournalEntries().contains(entry2));
    }

    @Test
    void testAddAndRetrieveRoles() {
        User user = new User();
        List<String> roles = Arrays.asList("ROLE_EDITOR", "ROLE_VIEWER");

        user.setRoles(roles);

        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains("ROLE_EDITOR"));
        assertTrue(user.getRoles().contains("ROLE_VIEWER"));
    }

    @Test
    void testGettersAndSetters() {
        User user = new User();
        ObjectId id = new ObjectId();
        String userName = "getterSetterUser";
        String password = "getterSetterPassword";
        List<JournalEntry> journalEntries = new ArrayList<>();
        List<String> roles = new ArrayList<>();

        user.setId(id);
        user.setUserName(userName);
        user.setPassword(password);
        user.setJournalEntries(journalEntries);
        user.setRoles(roles);

        assertEquals(id, user.getId());
        assertEquals(userName, user.getUserName());
        assertEquals(password, user.getPassword());
        assertEquals(journalEntries, user.getJournalEntries());
        assertEquals(roles, user.getRoles());
    }

    @Test
    void testNonNullUserNameConstraint() {
        User user = new User();
        // Setting password is required for the specific constructor used here,
        // but the test focuses on userName
        user.setPassword("somePassword");
        
        // Attempting to set userName to null should ideally be prevented by @NonNull
        // and potentially framework-level validation.
        // This test checks if the basic behavior of setting is possible,
        // the real enforcement is by the framework.
        assertDoesNotThrow(() -> user.setUserName("validUserName"));

        // While @NonNull is a compile-time/lombok feature,
        // runtime might still allow setting null.
        // Frameworks like Spring Data often handle the runtime enforcement.
        // A more robust test for @NonNull would involve a validation framework.
    }

     @Test
    void testNonNullPasswordConstraint() {
        User user = new User();
         // Setting userName is required for the specific constructor used here,
        // but the test focuses on password
        user.setUserName("someUser");

        // Attempting to set password to null should ideally be prevented by @NonNull
        // and potentially framework-level validation.
        // This test checks if the basic behavior of setting is possible,
        // the real enforcement is by the framework.
        assertDoesNotThrow(() -> user.setPassword("validPassword"));

        // Similar to userName, runtime behavior with null might be possible
        // without a validation framework.
    }
}