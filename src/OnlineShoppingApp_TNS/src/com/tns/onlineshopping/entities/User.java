package com.tns.onlineshopping.entities;

//===== com/tns/onlineshopping/entities/User.java =====


public class User {
 protected int userId;
 protected String username;
 protected String email;

 public User() {}

 public User(int userId, String username, String email) {
     this.userId = userId;
     this.username = username;
     this.email = email;
 }

 public int getUserId() { return userId; }
 public String getUsername() { return username; }
 public String getEmail() { return email; }

 @Override
 public String toString() {
     return "User ID: " + userId + ", Username: " + username + ", Email: " + email;
 }
}