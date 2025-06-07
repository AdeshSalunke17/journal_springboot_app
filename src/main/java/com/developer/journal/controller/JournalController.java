package com.developer.journal.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.developer.journal.entity.Journal;
import com.developer.journal.entity.User;
import com.developer.journal.service.JournalService;
import com.developer.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/journal")
public class JournalController {
    @Autowired
    private JournalService journalService;
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Journal>> getJournals(@PathVariable ObjectId userId) {
        User user = userService.findUser(userId);
        if(user != null) {
            return new ResponseEntity<List<Journal>>(user.getJournalList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Journal> addJournal(@RequestBody Journal journal, @PathVariable ObjectId userId) {
        User user = null;
        Journal savedJournal = null;
        // first adding journal to db
        try {
            savedJournal = journalService.saveJournal(journal);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // second adding in user
        try {
            if(savedJournal != null) {
                user = userService.findUser(userId);
                if(user != null) {
                    user.getJournalList().add(journal);
                    userService.saveUser(user);
                    return new ResponseEntity<Journal>(savedJournal, HttpStatus.CREATED);
                } else {
                    new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
                }
            } else {
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Journal> getJournal(@PathVariable ObjectId id) {
//        Journal journal = journalService.getParticularJournal(id);
//        if(journal != null) {
//            return new ResponseEntity<Journal>(journal,HttpStatus.FOUND);
//        } else {
//            return  new ResponseEntity<Journal>(journal,HttpStatus.NOT_FOUND);
//        }
//    }
//
    @DeleteMapping
    public boolean deleteJournal(@RequestParam ObjectId userId , @RequestParam ObjectId journalId) {
        boolean journalResult = false;
        try {
            // delete from journal db
            journalResult = journalService.deleteJournal(journalId);
            if(journalResult) {
                // delete from users db
                User user = userService.findUser(userId);
                if(user != null) {
                    user.getJournalList().removeIf(journal -> journal.getId() == journalId);
                    userService.saveUser(user);
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }
//
    @PutMapping("/{userId}")
    public ResponseEntity<Journal> updateJournal(@PathVariable ObjectId userId, @RequestBody Journal newJournal) {
        try {
            Journal journalFromDb = journalService.getParticularJournal(newJournal.getId());
            if(journalFromDb != null) {
                journalFromDb.setTitle(newJournal.getTitle());
                journalFromDb.setDescription(newJournal.getDescription());
                journalFromDb.setUpdateDate(LocalDateTime.now());
                journalService.saveJournal(journalFromDb);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception ) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}