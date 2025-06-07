package com.developer.journal.service;

import com.developer.journal.entity.Journal;
import com.developer.journal.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    public List<Journal> getJournalService() {
        return journalRepository.findAll();
    }

    public Journal saveJournal(Journal journal) {
        if(journal.getTitle().trim().equals("") || journal.getDescription().trim().equals("") || journal.getTitle() == null || journal.getDescription() == null) {
            return null;
        }
        journal.setCreateDate(LocalDateTime.now());
        journal.setUpdateDate(LocalDateTime.now());
        return journalRepository.save(journal);
    }

    public Journal getParticularJournal(ObjectId id) {
        Optional<Journal> result = journalRepository.findById(id);
        return result.orElse(null);
    }

    public boolean deleteJournal(ObjectId id) {
        journalRepository.deleteById(id);
        return true;
    }
}
