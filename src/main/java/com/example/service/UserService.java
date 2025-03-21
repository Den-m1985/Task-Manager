package com.example.service;

import com.example.bootstrap.RoleSeeder;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.dto.ChangeUserRoleDto;
import com.example.model.enums.RoleEnum;
import com.example.repositiry.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleSeeder roleSeeder;

    public User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with mail: " + userEmail + " not found"));
    }

    public User changeUserRole(ChangeUserRoleDto request) {
        Role role = roleSeeder.findRoleByName(RoleEnum.valueOf(request.role()));
        var user = findUserByEmail(request.email());
        user.setRole(role);
        return userRepository.save(user);
    }

}
