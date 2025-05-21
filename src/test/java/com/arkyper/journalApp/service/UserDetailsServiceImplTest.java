package com.arkyper.journalApp.service;

import com.arkyper.journalApp.entity.User;
import com.arkyper.journalApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists() {
        // Arrange
        String username = "testuser";
        User mockUser = new User();
        mockUser.setUserName(username);
        mockUser.setPassword("password123");
        mockUser.setRoles(Arrays.asList("USER", "ADMIN"));

        when(userRepository.findByUserName(username)).thenReturn(mockUser);

        // Act
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_UserDoesNotExist() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUserName(username)).thenReturn(null);

        // Act and Assert
        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername(username);
        });
        assertTrue(thrown.getMessage().contains("User not found" + username));
    }
}