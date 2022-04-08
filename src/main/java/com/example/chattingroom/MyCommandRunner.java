package com.example.chattingroom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(MyCommandRunner.class);

    @Autowired
    private UserRepository repository;

    @Override
    public void run(String...args) throws Exception {
        if (repository.findAll().isEmpty()){ //insert data if no data in the database
            User u1, u2, u3;
            u1 = new User("Alice", "123456");
            u2 = new User("Bob", "123456");
            u3 = new User("Cindy", "123456");
            repository.save(u1);
            repository.save(u2);
            repository.save(u3);
        }

        // fetch all Users
        logger.info("Users found with findAll():");
        logger.info("-------------------------------");
        for (User u : repository.findAll()) {
            logger.info(u.toString());
        }
    }
}