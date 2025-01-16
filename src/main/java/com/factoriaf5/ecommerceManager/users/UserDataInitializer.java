package com.factoriaf5.ecommerceManager.users;

import org.springframework.boot.CommandLineRunner;

import java.util.concurrent.ThreadLocalRandom;

public class UserDataInitializer implements CommandLineRunner {

    private final UserRepo userRepository;

    public UserDataInitializer(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if there are already users in the database
        if (userRepository.count() == 0) {
            // Create and save users if no users are found
            User user1 = new User(1L, "john_doe", "password123", "john@example.com");
            User user2 = new User(2L, "jane_smith", "password456", "jane@example.com");
            User user3 = new User(2L, "giacomo", "password789", "giacomo@example.com");
            User user4 = new User(2L, "maria", "password101", "maria@example.com");

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);


        }
    }
}

