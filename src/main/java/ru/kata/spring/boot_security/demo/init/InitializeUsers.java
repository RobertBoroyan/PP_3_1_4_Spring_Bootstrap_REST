package ru.kata.spring.boot_security.demo.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
public class InitializeUsers {
    private final UserService userService;
    private final RoleService roleService;


    Role adminRole = new Role("ROLE_ADMIN");
    Role userRole = new Role("ROLE_USER");

    private final Set<Role> roles1 = new HashSet<>(Set.of(adminRole, userRole));
    private final Set<Role> roles2 = new HashSet<>(Set.of(userRole));


    private final User admin = new User("admin", "admin", roles1);
    private final User user = new User("user", "user", roles2);

    @Autowired
    public InitializeUsers(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Transactional
    public void init() {
        roleService.save(adminRole);
        roleService.save(userRole);
        userService.addUser(admin);
        userService.addUser(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InitializeUsers that = (InitializeUsers) o;
        return Objects.equals(userService, that.userService) && Objects.equals(roleService, that.roleService) && Objects.equals(adminRole, that.adminRole) && Objects.equals(userRole, that.userRole) && Objects.equals(roles1, that.roles1) && Objects.equals(roles2, that.roles2) && Objects.equals(admin, that.admin) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userService, roleService, adminRole, userRole, roles1, roles2, admin, user);
    }

    @Override
    public String toString() {
        return "InitializeUsers{" +
                "userService=" + userService +
                ", roleService=" + roleService +
                ", adminRole=" + adminRole +
                ", userRole=" + userRole +
                ", roles1=" + roles1 +
                ", roles2=" + roles2 +
                ", admin=" + admin +
                ", user=" + user +
                '}';
    }
}
