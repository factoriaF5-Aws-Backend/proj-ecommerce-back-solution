package com.factoriaf5.ecommerceManager;

import com.factoriaf5.ecommerceManager.users.User;
import com.factoriaf5.ecommerceManager.users.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceManagerApplication implements CommandLineRunner {
	@Autowired
	UserRepo userRepo;

	public static void main(String[] args) {
		SpringApplication.run(EcommerceManagerApplication.class, args);
	}

	@Override
	public void run(String... args) {
		User authUser = new User("admin", "email","password");
		userRepo.save(authUser);

		System.out.println("User created: " + authUser.getUserName() + " with ID: " + authUser.getId());
	}

}
