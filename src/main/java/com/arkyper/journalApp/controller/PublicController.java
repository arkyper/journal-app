package com.arkyper.journalApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.arkyper.journalApp.cache.AppCache;
import com.arkyper.journalApp.entity.User;
import com.arkyper.journalApp.service.QuotesService;
import com.arkyper.journalApp.service.UserDetailsServiceImpl;
import com.arkyper.journalApp.service.UserService;
import com.arkyper.journalApp.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/public")
@Tag(name = "Public APIs", description = "Health-Check, Signup, Login, Greeting, Logout, Clear-Cache")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private QuotesService quotesService;

    @Autowired
    private AppCache appCache;

    @GetMapping("/health-check")
    @Operation(summary = "Health-Check", description = "Health-Check API")
    public String healthCheck() {
        return "Server is UP and running...";
    }

    @PostMapping("/signup")
    @Operation(summary = "Signup User", description = "Signup API")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.saveNewUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login User", description = "Login API")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getUserName());
            String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
            log.info("Token: " + jwtToken);
            return new ResponseEntity<>(jwtToken, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occured while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect Username or Password", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/greeting")
    @Operation(summary = "Greeting User", description = "Greeting API")
    public ResponseEntity<?> greeting() {
        String quote = quotesService.getQuote().get(0).getQuote();
        return new ResponseEntity<>("Hi " + "Saurabh " + quote, HttpStatus.OK);
    }

  

    @GetMapping("/clear-cache")             
    @Operation(summary = "Clear Cache", description = "Clear Cache API")
    public void clearAppCache() {
        appCache.init();
    }

    @DeleteMapping("/logout")
    @Operation(summary = "Logout User", description = "Logout API")
    public ResponseEntity<?> logout() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
