package com.developer.journal.controller;

import com.developer.journal.entity.User;
import com.developer.journal.responsehandler.Pet;
import com.developer.journal.service.PetService;
import com.developer.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user, @RequestParam String role) {
        if(user.getUserName() != null && !user.getUserName().trim().equals("") && user.getPassword() != null && !user.getPassword().trim().equals("")) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            if(role.equals("USER")) {
                user.setRoles(Arrays.asList("USER"));
            } else if(role.equals("ADMIN")) {
                user.setRoles(Arrays.asList("USER", "ADMIN"));
            }
            User storeUser = userService.saveUser(user);
            return  new ResponseEntity<User>(storeUser, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/getpets/{status}")
    public Pet[] getPetsByStatus(@PathVariable String status) {
        return petService.getPetsByStatus(status);
    }
}
