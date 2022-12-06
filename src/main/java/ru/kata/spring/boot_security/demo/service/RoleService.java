package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface RoleService {

    void save(Role role);

    List<Role> findAll();

    Role findRoleByRole(String role);

    Role findRoleById(int id) throws RoleNotFoundException;
}
