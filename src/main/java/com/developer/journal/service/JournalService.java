package com.developer.journal.service;

import com.developer.journal.entity.Journal;
import com.developer.journal.entity.User;
import com.developer.journal.repository.JournalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;
    @Autowired
    private UserService userService;

    public List<Journal> getJournalService() {
        return journalRepository.findAll();
    }
    @Transactional
    public Journal saveJournal(Journal journal, ObjectId userId) {
        // saving in journals db
        if(journal.getTitle().trim().equals("") || journal.getDescription().trim().equals("") || journal.getTitle() == null || journal.getDescription() == null) {
            return null;
        }
        journal.setCreateDate(LocalDateTime.now());
        journal.setUpdateDate(LocalDateTime.now());
        Journal savedJournal = journalRepository.save(journal);
        // saving in user db
            if(savedJournal != null) {
                User user = userService.findUser(userId);
                if(user != null) {
                    user.getJournalList().add(journal);
                    userService.saveUser(user);
                    return savedJournal;
                } else {
                    throw new RuntimeException("user not found");
                }
            } else {
                throw new RuntimeException("Unable to store journal in journal db");
            }
    }

    public Journal getParticularJournal(ObjectId id) {
        Optional<Journal> result = journalRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public boolean deleteJournal(ObjectId id, ObjectId userId) {
        journalRepository.deleteById(id);
        User user = userService.findUser(userId);
        if(user != null) {
            user.getJournalList().removeIf(journal -> journal.getId() == id);
            userService.saveUser(user);
            return true;
        } else {
            throw new RuntimeException("unable to delete");
        }
    }

    public Journal saveUpdatedJournal(Journal journal) {
        return journalRepository.save(journal);
    }
}
