package com.developer.journal.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.developer.journal.entity.Journal;
import com.developer.journal.service.JournalService;
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

    @GetMapping
    public List<Journal> getJournals() {
        return journalService.getJournalService();
    }

    @PostMapping
    public ResponseEntity<Journal> addJournal(@RequestBody Journal journal) {
        journal.setCreateDate(LocalDateTime.now());
        journal.setUpdateDate(LocalDateTime.now());
       return journalService.saveJournal(journal);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Journal> getJournal(@PathVariable ObjectId id) {
        Journal journal = journalService.getParticularJournal(id);
        if(journal != null) {
            return new ResponseEntity<Journal>(journal,HttpStatus.FOUND);
        } else {
            return  new ResponseEntity<Journal>(journal,HttpStatus.NOT_FOUND);
        }
    }
//
    @DeleteMapping("/{id}")
    public boolean deleteJournal(@PathVariable ObjectId id) {
        return journalService.deleteJournal(id);
    }
//
    @PutMapping("/{id}")
    public ResponseEntity<Journal> updateJournal(@PathVariable ObjectId id, @RequestBody Journal newJournal) {
        Journal oldJournal = journalService.getParticularJournal(id);
        if(oldJournal != null && !newJournal.getTitle().equals("") && !newJournal.getDescription().equals(("")) ) {
            oldJournal.setTitle(newJournal.getTitle());
            oldJournal.setDescription(newJournal.getDescription());
            oldJournal.setUpdateDate(LocalDateTime.now());
            journalService.saveJournal(oldJournal);
            return new ResponseEntity<Journal>(oldJournal , HttpStatus.OK);
        } else {
            return new ResponseEntity<Journal>(HttpStatus.BAD_REQUEST);
        }
    }
}