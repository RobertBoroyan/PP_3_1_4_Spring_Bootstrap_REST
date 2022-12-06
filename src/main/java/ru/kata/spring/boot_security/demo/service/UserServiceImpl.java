package ru.kata.spring.boot_security.demo.service;

import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Hibernate.initialize(user.getRolesSet());
            return user;
        } else {
            throw new UsernameNotFoundException("No user with such id");
        }
    }

    @Override
    @Transactional
    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> userRoles = user.getRolesSet()
                .stream().map(role -> {
                    try {
                        return roleService.findRoleById(role.getId());
                    } catch (RoleNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
        user.setRolesSet(userRoles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changeUser(int id, User replaceUser) {
        replaceUser.setId(id);
        Optional<User> previousUser = userRepository.findById(id);
        if (replaceUser.getRolesSet().isEmpty()) {
            previousUser.ifPresent(user -> replaceUser.setRolesSet(user.getRolesSet()));
        }
        if (replaceUser.getPassword().isEmpty()) {
            Set<Role> roles = replaceUser.getRolesSet().stream().map(role -> {
                        try {
                            return roleService.findRoleById(role.getId());
                        } catch (RoleNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
            replaceUser.setRolesSet(roles);
            previousUser.ifPresent(user -> replaceUser.setPassword(user.getPassword()));
            userRepository.save(replaceUser);
        } else {
            addUser(replaceUser);
        }
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Hibernate.initialize(user.getRolesSet());
            return user;
        } else
            throw new UsernameNotFoundException("No user with such username");
    }


}
