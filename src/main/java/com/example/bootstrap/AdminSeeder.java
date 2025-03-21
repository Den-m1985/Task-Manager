package com.example.bootstrap;

import com.example.model.Role;
import com.example.model.User;
import com.example.model.enums.RoleEnum;
import com.example.repositiry.RoleRepository;
import com.example.repositiry.UserRepository;
import com.example.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createSuperAdministrator();
    }

    public Role findRoleByName(RoleEnum name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role with name: " + name.name() + " not found"));
    }

    private void createSuperAdministrator() {
        String email = "super.admin@email.com";
        Role role = findRoleByName(RoleEnum.SUPER_ADMIN);
        User user = null;
        try {
            user = userService.findUserByEmail(email);
        } catch (EntityNotFoundException e) {
            user = new User();
            user.setFirstName("Super Admin");
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole(role);
            userRepository.save(user);
        }
    }

}
