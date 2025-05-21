package com.arkyper.journalApp.repository;

import com.arkyper.journalApp.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataMongoTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserName("testuser");
        testUser.setPassword("password");
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void findByUserName_shouldReturnUser_whenUserExists() {
        User foundUser = userRepository.findByUserName("testuser");
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUserName());
    }

    @Test
    void findByUserName_shouldReturnNull_whenUserDoesNotExist() {
        User foundUser = userRepository.findByUserName("nonexistentuser");
        assertNull(foundUser);
    }

    @Test
    void deleteByUserName_shouldDeleteUser_whenUserExists() {
        userRepository.deleteByUserName("testuser");
        User deletedUser = userRepository.findByUserName("testuser");
        assertNull(deletedUser);
    }

    @Test
    void deleteByUserName_shouldDoNothing_whenUserDoesNotExist() {
        userRepository.deleteByUserName("nonexistentuser");
        User user = userRepository.findByUserName("testuser"); // Verify other users are not affected
        assertNotNull(user);
        assertEquals("testuser", user.getUserName());
    }
}