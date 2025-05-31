package com.arkyper.journalApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arkyper.journalApp.entity.User;
import com.arkyper.journalApp.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs", description = "Get all Users, Create Admin User")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    @Operation(summary = "Get all Users", description = "Get all Users")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userService.getAll();
        if (!allUsers.isEmpty() && allUsers != null)
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    @Operation(summary = "Create Admin User", description = "Create Admin User")
    public ResponseEntity<?> createAdminUser(@RequestBody User user) {
        try {
            userService.saveNewAdmin(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
