package com.developer.journal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import com.developer.journal.entity.Journal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/journal")
public class JournalController {
    private HashMap<Long , Journal> fakeData = new HashMap<>();
    
    @GetMapping
    public ArrayList<Journal> getJournals() {
        return new ArrayList<>(fakeData.values());
    }

    @PostMapping
    public boolean addJournal(@RequestBody Journal journal) {
        fakeData.put(journal.getId(), journal);
        return true;
    }

    @GetMapping("/{id}")
    public Journal getJournal(@PathVariable Long id) {
        return fakeData.get(id);
    }

    @DeleteMapping("/{id}") 
    public String deleteJournal(@PathVariable Long id) {
        fakeData.remove(id);
        return "removed";
    }

    @PutMapping("/{id}")
    public Journal updateJournal(@PathVariable String id, @RequestBody Journal journal) {
       fakeData.put(journal.getId(), journal);
        return journal;
    }
}