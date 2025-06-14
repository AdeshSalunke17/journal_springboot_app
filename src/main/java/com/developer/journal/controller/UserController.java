package com.developer.journal.controller;

import com.developer.journal.entity.User;
import com.developer.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<User> getUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByName(userName);
        if(user != null) {
            return new ResponseEntity<User>(user, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            User updateUser = userService.updateUser(user, userName);
            if(updateUser != null) {
                return new ResponseEntity<User>(updateUser, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping
    public boolean deleterUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.deleteUser(userName);
    }
}
