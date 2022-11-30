package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(int id);

    void addUser(User user);

    void changeUser(int id, User replaceUser);

    void deleteUser(int id);

    User findByUsername(String username);
}
