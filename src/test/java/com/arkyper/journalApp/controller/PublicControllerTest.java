package com.arkyper.journalApp.controller;

import com.arkyper.journalApp.entity.User;
import com.arkyper.journalApp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PublicControllerTest {

    @InjectMocks
    private PublicController publicController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void healthCheck() {
        // Fix: Assign the result of publicController.healthCheck() to a ResponseEntity<String>
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Ok", HttpStatus.OK); 
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Ok", responseEntity.getBody());
    }

    @Test
    void createUser_Success() {
        User user = new User();
        user.setUserName("testUser");
        user.setPassword("password");

        // Fix: Use doNothing() to mock a void method call that should succeed
        doNothing().when(userService).saveNewUser(any(User.class));

        ResponseEntity<?> responseEntity = publicController.createUser(user);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("User created successfully", responseEntity.getBody());
        verify(userService, times(1)).saveNewUser(user);
    }

    @Test
    void createUser_Failure() {
        User user = new User();
        user.setUserName("testUser");
        user.setPassword("password");

        // Fix: Use doThrow() to mock a void method call that should throw an exception
        doThrow(new RuntimeException("Error creating user")).when(userService).saveNewUser(any(User.class));

        ResponseEntity<?> responseEntity = publicController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("User creation failed", responseEntity.getBody());
        verify(userService, times(1)).saveNewUser(user);
    }
}