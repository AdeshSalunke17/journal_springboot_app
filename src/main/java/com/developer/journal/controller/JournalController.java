package com.developer.journal.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.developer.journal.entity.Journal;
import com.developer.journal.entity.User;
import com.developer.journal.service.JournalService;
import com.developer.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping
    public ResponseEntity<List<Journal>> getJournals() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByName(userName);
        if(user != null) {
            return new ResponseEntity<List<Journal>>(user.getJournalList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Journal> addJournal(@RequestBody Journal journal) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByName(userName);
        if(user != null) {
            try {
                Journal savedJournal = journalService.saveJournal(journal, user);
                return new ResponseEntity<Journal>(savedJournal, HttpStatus.CREATED);
            } catch (Exception exception) {
                System.out.println(exception);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{journalId}")
    public ResponseEntity<Journal> getJournal(@PathVariable ObjectId journalId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByName(userName);
        List<Journal> collect = user.getJournalList().stream().filter(journal -> journal.getId().equals(journalId)).collect(Collectors.toList());
        if(collect.size() == 1) {
            Journal journal = journalService.getParticularJournal(collect.get(0).getId());
            if(journal != null) {
                return new ResponseEntity<Journal>(journal,HttpStatus.FOUND);
            } else {
                return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{journalId}")
    public ResponseEntity<Boolean> deleteJournal(@PathVariable ObjectId journalId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByName(userName);
        if(user != null) {
            try {
                boolean result =  journalService.deleteJournal(journalId, user);
                return new ResponseEntity<Boolean>(result, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
//
    @PutMapping("/{journalId}")
    public ResponseEntity<Journal> updateJournal(@RequestBody Journal newJournal, @PathVariable ObjectId journalId) {
        Journal updatedJournal = null;
        try {
            Journal journalFromDb = journalService.getParticularJournal(journalId);
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