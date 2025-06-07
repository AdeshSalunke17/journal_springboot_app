package com.developer.journal.service;

import com.developer.journal.entity.User;
import com.developer.journal.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
        if(userInDb != null) {
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            return userRepository.save(userInDb);
        }
        return null;
    }

    public boolean deleteUser(ObjectId id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
