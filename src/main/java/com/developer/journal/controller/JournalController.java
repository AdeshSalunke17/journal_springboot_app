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
        try {
            Journal savedJournal = journalService.saveJournal(journal, userId);
            return new ResponseEntity<Journal>(savedJournal, HttpStatus.CREATED);
        } catch (Exception exception) {
            System.out.println(exception);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<Boolean> deleteJournal(@RequestParam ObjectId userId , @RequestParam ObjectId journalId) {
        try {
            boolean result =  journalService.deleteJournal(journalId, userId);
            return new ResponseEntity<Boolean>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//
    @PutMapping("/{userId}")
    public ResponseEntity<Journal> updateJournal(@RequestBody Journal newJournal) {
        Journal updatedJournal = null;
        try {
            Journal journalFromDb = journalService.getParticularJournal(newJournal.getId());
            if(journalFromDb != null) {
                journalFromDb.setTitle(newJournal.getTitle());
                journalFromDb.setDescription(newJournal.getDescription());
                journalFromDb.setUpdateDate(LocalDateTime.now());
                updatedJournal = journalService.saveUpdatedJournal(journalFromDb);
            }
            return new ResponseEntity<>(updatedJournal,HttpStatus.OK);
        } catch (Exception exception ) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}