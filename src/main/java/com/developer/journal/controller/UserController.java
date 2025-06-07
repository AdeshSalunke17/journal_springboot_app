package com.developer.journal.controller;

import com.developer.journal.entity.User;
import com.developer.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if(user.getUserName() != null && !user.getUserName().trim().equals("") && user.getPassword() != null && !user.getPassword().trim().equals("")) {
            User storeUser = userService.saveUser(user);
            return  new ResponseEntity<User>(user, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        List<User> list = userService.findAllUser();
        return new ResponseEntity<List<User>>(list, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable ObjectId id) {
        User user = userService.findUser(id);
        if(user != null) {
            return new ResponseEntity<User>(user, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{userName}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable String userName) {
            User updateUser = userService.updateUser(user, userName);
            if(updateUser != null) {
                return new ResponseEntity<User>(updateUser, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{id}")
    public boolean deleterUser(@PathVariable ObjectId id) {
        return userService.deleteUser(id);
    }
}
