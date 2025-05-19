package com.arkyper.journalApp.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arkyper.journalApp.entity.User;
import com.arkyper.journalApp.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired 
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName) {
        Optional<User> user = Optional.ofNullable(userService.findByUserName(userName));
        if(user.isPresent())
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        else 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        
        User userInDB = userService.findByUserName(userName);
        userInDB.setUserName(user.getUserName());
        userInDB.setPassword(user.getPassword());
        userService.saveUser(userInDB);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        userService.deleteByUserName(userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
        