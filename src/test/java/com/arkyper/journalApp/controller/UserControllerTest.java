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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetUserByUserName_ExistingUser() {
        User user = new User();
        user.setUserName("testUser");
        when(userService.findByUserName("testUser")).thenReturn(user);

        ResponseEntity<?> response = userController.getUserByUserName("testUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testGetUserByUserName_NonExistingUser() {
        when(userService.findByUserName("nonExistingUser")).thenReturn(null);

        ResponseEntity<?> response = userController.getUserByUserName("nonExistingUser");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUser() {
        String userName = "testUser";
        User existingUser = new User();
        existingUser.setUserName(userName);
        existingUser.setPassword("oldPassword");

        User updatedUser = new User();
        updatedUser.setUserName("newUserName");
        updatedUser.setPassword("newPassword");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userName);
        when(userService.findByUserName(userName)).thenReturn(existingUser);

        ResponseEntity<?> response = userController.updateUser(updatedUser);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).saveNewUser(existingUser);
        assertEquals("newUserName", existingUser.getUserName());
        assertEquals("newPassword", existingUser.getPassword());
    }

    @Test
    public void testDeleteUser() {
        String userName = "testUser";

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userName);

        ResponseEntity<?> response = userController.deleteUser();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteByUserName(userName);
    }
}