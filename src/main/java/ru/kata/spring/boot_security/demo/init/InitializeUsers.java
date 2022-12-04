package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class InitializeUsers implements ApplicationRunner {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public InitializeUsers(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");


        final Set<Role> roles1 = new HashSet<>(List.of(adminRole, userRole));
        final Set<Role> roles2 = new HashSet<>(List.of(userRole));

        final User admin = new User("admin", "admin", 26, "admin", roles1);
        final User user = new User("user", "user", 12, "user", roles2);


        roleService.save(adminRole);
        roleService.save(userRole);

        userService.addUser(admin);
        userService.addUser(user);

    }
}
