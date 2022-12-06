package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

public interface Converter {
    RoleDTO convertToDto(Role role);

    UserDTO convertToDto(User user);

    User convertToUser(UserDTO userDTO);
}
