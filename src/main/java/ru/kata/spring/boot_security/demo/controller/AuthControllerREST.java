package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.service.ConverterService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthControllerREST {
    private final UserService userService;
    private final RoleService roleService;
    private final ConverterService converter;

    public AuthControllerREST(UserService userService, RoleService roleService, ConverterService converter) {
        this.userService = userService;
        this.roleService = roleService;
        this.converter = converter;
    }


    @GetMapping("/auth")
    public UserDTO getAuth(Authentication auth) {
        return converter.convertToDto(userService.findByUsername(auth.getName()));
    }

    @GetMapping("/roles")
    public Set<RoleDTO> getAllRoles() {
        return roleService.findAll().stream().map(converter::convertToDto).collect(Collectors.toSet());
    }
}
