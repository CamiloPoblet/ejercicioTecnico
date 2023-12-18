package com.camilopoblete.ejerciciotecnico;

import com.camilopoblete.ejerciciotecnico.controller.UserController;
import com.camilopoblete.ejerciciotecnico.exceptions.UserAuthenticationException;
import com.camilopoblete.ejerciciotecnico.exceptions.UserCreationException;
import com.camilopoblete.ejerciciotecnico.model.Phone;
import com.camilopoblete.ejerciciotecnico.model.User;
import com.camilopoblete.ejerciciotecnico.security.JWTProvider;
import com.camilopoblete.ejerciciotecnico.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTProvider tokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserController userController;
    @Test
    void testLogin_SuccessfulAuthentication() {
        // Setup mock
        Authentication mockAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(mockAuthentication);

        // usuario token no nulo
        User mockUser = new User();
        mockUser.setToken("mockToken"); // token no nulo
        mockUser.setPassword("Holailos12");
        mockUser.setEmail("mail@dominio.com");
        when(userService.findByEmail(anyString())).thenReturn(mockUser);

        // tokenProvider
        when(tokenProvider.generateToken(anyString())).thenReturn("mockToken");

        ResponseEntity<User> response = userController.login(mockUser);

        // Verificacion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mockToken", response.getBody().getToken());
    }

    @Test
    void testLogin_AuthenticationFailure() {
        // Setup mock
        when(authenticationManager.authenticate(any())).thenThrow(new UsernameNotFoundException("User not found"));

        // Verification
        assertThrows(UserAuthenticationException.class, () -> userController.login(new User()));
    }

    @Test
    void testCreateUser_SuccessfulCreation() {
        // Setup mock
        when(userService.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userService.createUser(any())).thenReturn(new User());


        ResponseEntity<User> response = userController.createUser(createTestUser());

        // Verificacion
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        // Setup mock
        when(userService.findByEmail(anyString())).thenReturn(createTestUser());

        // Verificacion
        assertThrows(UserCreationException.class, () -> userController.createUser(createTestUser()));
    }

    @Test
    void testCreateUser_InvalidPasswordFormat() {
        // Setup mock
        when(userService.findByEmail(anyString())).thenReturn(null);

        //Verificacion
        assertThrows(UserCreationException.class, () -> userController.createUser(createTestUserWithInvalidPassword()));
    }

    @Test
    void testGetUserById_UserFound() {
        // Setup mock
        when(userService.getUserById(any(UUID.class))).thenReturn(createTestUser());


        ResponseEntity<User> response = userController.getUserById(UUID.randomUUID());

        // Verificacion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Setup mock
        when(userService.getUserById(any(UUID.class))).thenReturn(null);


        ResponseEntity<User> response = userController.getUserById(UUID.randomUUID());

        // Verificacion
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetAllUsers() {
        // Setup mock
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(createTestUser()));


        ResponseEntity<Iterable<User>> response = userController.getAllUsers();

        // Verificacion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().iterator().hasNext());
    }

    private User createTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setPassword("Test12345");
        user.setPhones(Collections.singletonList(new Phone(71786222L,9,"+56")));
        return user;
    }

    private User createTestUserWithInvalidPassword() {
        User user = createTestUser();
        user.setPassword("invalid"); // Invalid password format
        return user;
    }
}