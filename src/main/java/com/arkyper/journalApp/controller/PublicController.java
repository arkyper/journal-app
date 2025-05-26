package com.arkyper.journalApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arkyper.journalApp.cache.AppCache;
import com.arkyper.journalApp.entity.User;
import com.arkyper.journalApp.service.QuotesService;
import com.arkyper.journalApp.service.UserService;

@RestController
@RequestMapping("/public")
public class PublicController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private QuotesService quotesService;

    @Autowired
    private AppCache appCache;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Server is UP and running...";
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.saveNewUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
    }

    
    @GetMapping
    public ResponseEntity<?> greeting() {
        String quote = quotesService.getQuote().get(0).getQuote();
        return new ResponseEntity<>("Hi " + "Saurabh " + quote, HttpStatus.OK);
    }

    @GetMapping("/clear-cache")
    public void clearAppCache() {
        appCache.init();
    }
}
