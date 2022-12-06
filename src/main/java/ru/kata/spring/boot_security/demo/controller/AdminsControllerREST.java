package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.ConverterService;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserErrorResponse;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/api")
public class AdminsControllerREST {

    private final UserService userService;
    private final RoleService roleService;
    private final ConverterService converter;

    @Autowired
    public AdminsControllerREST(UserService userService, RoleService roleService, ConverterService converter) {
        this.userService = userService;
        this.roleService = roleService;
        this.converter = converter;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.findAll().stream().map(converter::convertToDto).collect(Collectors.toList());
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody UserDTO userDTO, @PathVariable("id") int id) {
        System.out.println("updating");
        userService.changeUser(id, converter.convertToUser(userDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
        System.out.println("delete");
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<?> addNewUser(@RequestBody UserDTO userDTO) {
        System.out.println("creating");
        User user = converter.convertToUser(userDTO);

        userService.addUser(user);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        UserErrorResponse userErrorResponse = new UserErrorResponse("User with this id wasn't found!");
        return new ResponseEntity<>(userErrorResponse, HttpStatus.NOT_FOUND);

    }
}
