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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers_UsersFound() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userService.getAll()).thenReturn(users);

        ResponseEntity<?> response = adminController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void testGetAllUsers_NoUsersFound() {
        when(userService.getAll()).thenReturn(new ArrayList<>());

        ResponseEntity<?> response = adminController.getAllUsers();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

     @Test
    void testGetAllUsers_NullUsers() {
        when(userService.getAll()).thenReturn(null);

        ResponseEntity<?> response = adminController.getAllUsers();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateAdminUser_Success() {
        User user = new User();
        user.setUserName("testadmin");
        user.setPassword("password");

        doNothing().when(userService).saveNewAdmin(any(User.class));

        ResponseEntity<?> response = adminController.createAdminUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).saveNewAdmin(any(User.class));
    }

    @Test
    void testCreateAdminUser_Exception() {
        User user = new User();
        user.setUserName("testadmin");
        user.setPassword("password");

        doThrow(new RuntimeException("Error creating admin user")).when(userService).saveNewAdmin(any(User.class));

        ResponseEntity<?> response = adminController.createAdminUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, times(1)).saveNewAdmin(any(User.class));
    }
}