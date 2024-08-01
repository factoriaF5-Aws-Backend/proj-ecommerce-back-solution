package com.factoriaf5.ecommerceManager.users;

import jakarta.persistence.*;


@Entity
@Table(name = "app_user")  //Change the table name to avoid conflict with reserved words
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String email;
    private String password;


    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public User(long id, String userName, String email, String password) {
        this(userName, email, password);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
