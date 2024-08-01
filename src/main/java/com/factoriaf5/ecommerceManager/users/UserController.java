package com.factoriaf5.ecommerceManager.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api")
public class UserController {
    @Autowired
    UserService userService;



    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRequest userRequest){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User registeredUser = userService.saveUser(userRequest);
        return ResponseEntity.status(201).body(registeredUser);
    }

    @GetMapping("/users/{userName}")
    public ResponseEntity<User> findUser(@PathVariable String userName){
        User returnedUser = userService.findUser(userName);
        return ResponseEntity.status(200).body(returnedUser);
    }



}
