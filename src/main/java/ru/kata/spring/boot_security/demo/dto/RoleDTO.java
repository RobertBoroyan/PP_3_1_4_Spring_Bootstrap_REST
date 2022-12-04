package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public class RoleDTO {

    private int id;

    private String roleName;

    private Set<User> usersSet;

    public RoleDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<User> getUsersSet() {
        return usersSet;
    }

    public void setUsersSet(Set<User> usersSet) {
        this.usersSet = usersSet;
    }
}
