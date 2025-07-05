package com.developer.journal.controller;

import com.developer.journal.config.JwtService;
import com.developer.journal.config.UserConfig;
import com.developer.journal.constant.Role;
import com.developer.journal.entity.User;
import com.developer.journal.responsehandler.Pet;
import com.developer.journal.service.PetService;
import com.developer.journal.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private UserConfig userConfig;
    @Autowired
    private PetService petService;
    @Autowired
    private JwtService jwtService;
    private Logger LOGGER = LoggerFactory.getLogger(PublicController.class);

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user, @RequestParam String role) {
        if(user.getUsername() != null && !user.getUsername().trim().equals("") && user.getPassword() != null && !user.getPassword().trim().equals("")) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            if(role.equals("USER")) {
                user.setRoles(Arrays.asList(Role.ROLE_USER));
            } else if(role.equals("ADMIN")) {
                user.setRoles(Arrays.asList(Role.ROLE_USER, Role.ROLE_ADMIN));
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
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            String jwt = jwtService.generateToken(user);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch (Exception e){
            LOGGER.error("Exception occurred while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}
