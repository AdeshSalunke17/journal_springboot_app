package com.developer.journal.service;

import com.developer.journal.entity.User;
import com.developer.journal.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAllUser() {
        return  userRepository.findAll();
    }

    public User findUser(ObjectId id) {
        Optional<User> result =  userRepository.findById(id);
        return result.orElse(null);
    }

    public User findByName(String userName) {
        Optional<User> result = userRepository.findByUserName(userName);
        return result.orElse(null);
    }

    public User updateUser(User user, String userName) {
        User userInDb = findByName(userName);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(userInDb != null) {
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userInDb.setEmail(user.getEmail());
            userInDb.setSentimentAnalysis(user.getSentimentAnalysis());
            return userRepository.save(userInDb);
        }
        return null;
    }

    public boolean deleteUser(String userName) {
        try {
            userRepository.deleteByUserName(userName);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public List<User> getAllUserWithEmailAndSentimentAnalysis() {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("email").exists(true).ne(null).ne("")
                        .regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"),
                Criteria.where("sentimentAnalysis").exists(true).is(true)
        ));
        List<User> list = mongoTemplate.find(query, User.class);
        return list;
    }
}
