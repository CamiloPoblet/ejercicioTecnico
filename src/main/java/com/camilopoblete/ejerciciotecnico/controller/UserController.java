package com.camilopoblete.ejerciciotecnico.controller;

import com.camilopoblete.ejerciciotecnico.exceptions.ErrorCode;
import com.camilopoblete.ejerciciotecnico.exceptions.UserAuthenticationException;
import com.camilopoblete.ejerciciotecnico.exceptions.UserCreationException;
import com.camilopoblete.ejerciciotecnico.model.User;
import com.camilopoblete.ejerciciotecnico.security.JWTProvider;
import com.camilopoblete.ejerciciotecnico.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.UUID;


@Slf4j
@Validated
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JWTProvider tokenProvider,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@Validated @RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);


            User authenticatedUser = userService.findByEmail(user.getEmail());
            // Genera JWT token
            String jwt = tokenProvider.generateToken(String.valueOf(authentication));
            authenticatedUser.setToken(jwt);

            // Actualiza lastLogin
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime localDateTime=LocalDateTime.now();
            String timestamp = localDateTime.format(formatter);
            authenticatedUser.setLastLogin(timestamp);

            userService.save(authenticatedUser);

            return ResponseEntity.ok(authenticatedUser);
        }catch (AuthenticationException e)
        {
            throw new UserAuthenticationException(ErrorCode.AUTHENTICATION_FAILED.getCode(),ErrorCode.AUTHENTICATION_FAILED.getMessage() );
        }

    }


    @PostMapping("/sign-up")
    public ResponseEntity<User> createUser(@Validated @RequestBody User user) {
        System.out.println("createUser101");
        User usuarioExistente = userService.findByEmail(user.getEmail());
        if(null != usuarioExistente) {
            throw new UserCreationException(ErrorCode.USER_ALREADY_EXISTS.getCode(), ErrorCode.USER_ALREADY_EXISTS.getMessage());
        }
        if(!user.getPassword().matches("^(?=.*[A-Z])(?=.*\\d.*\\d)[A-Za-z\\d]*{8,12}$")){
            throw new UserCreationException(ErrorCode.INVALID_PASSWORD_FORMAT.getCode(), ErrorCode.INVALID_PASSWORD_FORMAT.getMessage());
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setActive(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime localDateTime=LocalDateTime.now();
        String timestamp = localDateTime.format(formatter);
        user.setCreated(timestamp);

        User createdUser = userService.createUser(user);
        System.out.println("createUser103");
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUsers() {
        Iterable<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}