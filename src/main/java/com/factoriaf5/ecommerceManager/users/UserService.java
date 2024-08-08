package com.factoriaf5.ecommerceManager.users;

import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public User saveUser(UserRequest userRequest) {
        if (userRepo.findByUserName(userRequest.userName()) != null) {
            throw new UserAlreadyRegisterException("User already registered");
        }
        User user = new User(
                userRequest.userName(),
                userRequest.email(),
                userRequest.password()

        );
        return userRepo.save(user);
    }
    public User findUser(String userName) {
        if (userRepo.findByUserName(userName) == null) {
            throw new RuntimeException("The user " + userName + " is not found");
        }
        //TODO test if user not found
        return userRepo.findByUserName(userName);
    }

}
