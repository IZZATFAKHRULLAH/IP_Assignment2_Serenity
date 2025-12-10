

package com.example.bmiapp.dao;

public interface UserDao {
    void register(String username, String password, String role);
    boolean validateUser(String username, String password);
    String getUserRole(String username);
}