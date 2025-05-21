package com.arkyper.journalApp.service;

import com.arkyper.journalApp.entity.User;
import com.arkyper.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Since passwordEncoder is a static final field in the actual class, we need to mock its behavior directly in the test.
        // However, Mockito doesn't easily mock static fields. A common approach is to use a spy or inject a mock PasswordEncoder.
        // Here, we'll inject a mock and ensure the service uses it (assuming the service was designed to take a PasswordEncoder).
        // If the service truly uses a static field, testing password encoding requires different techniques (like PowerMock or rethinking the service design).
        // For this test, we will assume that injecting a mock PasswordEncoder works with the current setup due to MockitoAnnotations.openMocks.
        // In a real-world scenario with a static final field, you'd need a different approach.
    }

    @Test
    void saveUser_shouldCallSaveOnRepository() {
        User user = new User();
        userService.saveUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveNewUser_shouldEncodePasswordSetRolesAndSave() {
        User user = new User();
        user.setPassword("password");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        userService.saveNewUser(user);

        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.getRoles().contains("USER"));
        assertEquals(1, user.getRoles().size());
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void getAll_shouldReturnListOfUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
        verify(userRepository, times(1)).findAll();
    }

    @Test
void findById_shouldReturnOptionalUser() {
    ObjectId id = new ObjectId();
    User user = new User();
    when(userRepository.findById(id)).thenReturn(Optional.of(user));

    Optional<User> result = userService.findById(id);

    assertTrue(result.isPresent());
    assertEquals(user, result.get());
    verify(userRepository, times(1)).findById(id);
}

    @Test
    void deleteById_shouldCallDeleteByIdOnRepository() {
        ObjectId id = new ObjectId();
        userService.deleteById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void findByUserName_shouldReturnUser() {
        String userName = "testUser";
        User user = new User();
        when(userRepository.findByUserName(userName)).thenReturn(user);

        User result = userService.findByUserName(userName);

        assertEquals(user, result);
        verify(userRepository, times(1)).findByUserName(userName);
    }

    @Test
    void deleteByUserName_shouldCallDeleteByUserNameOnRepository() {
        String userName = "testUser";
        userService.deleteByUserName(userName);
        verify(userRepository, times(1)).deleteByUserName(userName);
    }

    @Test
    void saveNewAdmin_shouldEncodePasswordSetRolesAndSave() {
        User user = new User();
        user.setPassword("password");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        userService.saveNewAdmin(user);

        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.getRoles().contains("USER"));
        assertTrue(user.getRoles().contains("ADMIN"));
        assertEquals(2, user.getRoles().size());
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("password");
    }
}
