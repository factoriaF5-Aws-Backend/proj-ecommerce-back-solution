package com.factoriaf5.ecommerceManager.users;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepo userRepo;

    @InjectMocks
    UserService userService;

    @Test
    void testRegisterUser(){
        UserRequest userRequest = new UserRequest("test","test","test");
        User registeredUser = new User(1L, "test", "test", "test");

        Mockito.when(userRepo.save(any(User.class))).thenReturn(registeredUser);
        User userResponse = userService.saveUser(userRequest);


        assertEquals(1L, userResponse.getId());
        assertEquals(userRequest.userName(),userResponse.getUserName());
        assertEquals(userRequest.email(),userResponse.getEmail());
        assertEquals(userRequest.password(), userResponse.getPassword());
    }

    @Test
    void testUserCanNotRegisterTwice(){
        UserRequest userRequest = new UserRequest("test", "test", "test");
        User registeredUser = new User(1L, "test", "test", "test");
        Mockito.when(userRepo.findByUserName("test")).thenReturn(registeredUser);

        Exception exception = assertThrows(UserAlreadyRegisterException.class, () -> userService.saveUser(userRequest));

        String expectedMessage = "User already registered";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testUserCanBeRetrievedUsingUserName(){
        User registeredUser = new User(1L, "test", "test","test");
        Mockito.when(userRepo.findByUserName("test")).thenReturn(registeredUser);
        User userResponse = userService.findUser("test");

        assertEquals(registeredUser, userResponse);
    }
}
