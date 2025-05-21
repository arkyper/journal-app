package com.arkyper.journalApp.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

public class JournalEntryTest {

    @Test
    void testJournalEntryCreationWithNoArgsConstructor() {
        JournalEntry journalEntry = new JournalEntry();
        assertNotNull(journalEntry);
    }

    @Test
    void testSetAndGetTitle() {
        JournalEntry journalEntry = new JournalEntry();
        String title = "My Test Title";
        journalEntry.setTitle(title);
        assertEquals(title, journalEntry.getTitle());
    }

    @Test
    void testSetAndGetContent() {
        JournalEntry journalEntry = new JournalEntry();
        String content = "This is the test content.";
        journalEntry.setContent(content);
        assertEquals(content, journalEntry.getContent());
    }

    @Test
    void testSetAndGetDate() {
        JournalEntry journalEntry = new JournalEntry();
        LocalDateTime date = LocalDateTime.now();
        journalEntry.setDate(date);
        assertEquals(date, journalEntry.getDate());
    }

    @Test
    void testDocumentAnnotationPresence() {
        Class<JournalEntry> clazz = JournalEntry.class;
        Annotation documentAnnotation = clazz.getAnnotation(Document.class);
        assertNotNull(documentAnnotation, "JournalEntry should be annotated with @Document");
    }

    @Test
    void testDataAnnotationPresence() {
        Class<JournalEntry> clazz = JournalEntry.class;
        Annotation dataAnnotation = clazz.getAnnotation(Data.class);
        assertNotNull(dataAnnotation, "JournalEntry should be annotated with @Data");
    }

    @Test
    void testNoArgsConstructorAnnotationPresence() {
        Class<JournalEntry> clazz = JournalEntry.class;
        Annotation noArgsConstructorAnnotation = clazz.getAnnotation(NoArgsConstructor.class);
        assertNotNull(noArgsConstructorAnnotation, "JournalEntry should be annotated with @NoArgsConstructor");
    }
}