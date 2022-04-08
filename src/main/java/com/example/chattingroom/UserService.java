package com.example.chattingroom;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    // private List<User> users;

    public UserService() {
        System.out.println("UserService() called...");
    }

    public List<User> list() {
        List<User> users = repo.findAll();
        return users;
    }

    public User get(int uid) {
        User user = repo.findById(uid).orElse(null);
        return user;
    }

    public int create(User user) {
        User u = repo.save(user);
        return u.getUid();
    }

    public int update(User user) {
        User u = repo.save(user);
        return u.getUid();
    }

    public void delete(int uid) {
        repo.deleteById(uid);
    }

    public boolean checkNameAvailable(String name) {
        List<User> users = this.list();
        for (User i : users) {
            if (i.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public int login(String name, String password) {
        List<User> users = this.list();
        for (User i : users) {
            if (i.getName().equals(name)) {
                if (i.getPassword().equals(password)) {
                    return i.getUid();
                } else {
                    return -2; // Incorrect password
                }
            }
        }
        return -1; // Not found, account does not exist.
    }

    public int signup(String name, String password) {
        Boolean isNameAvailable = this.checkNameAvailable(name);
        if (isNameAvailable) {
            User newUser = new User(name, password);
            int newUid = this.create(newUser);
            return newUid;
        } else {
            return 0;
        }
        
    }
}
