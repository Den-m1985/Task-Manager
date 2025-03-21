package com.example.service;

import com.example.bootstrap.RoleSeeder;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.dto.AuthResponse;
import com.example.model.dto.LoginRequest;
import com.example.model.dto.RegisterRequest;
import com.example.model.enums.RoleEnum;
import com.example.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleSeeder roleSeeder;


    public AuthResponse signup(RegisterRequest request) {
        Role role = roleSeeder.findRoleByName(RoleEnum.USER);
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.password()));
        user = userService.saveUser(user);
        return new AuthResponse(
                null,
                null,
                user.getId()
        );
    }

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = userService.findUserByEmail(request.email());
        String jwtToken = jwtService.generateToken(user);
        String expTime = String.valueOf(jwtService.getExpirationTime());
        return new AuthResponse(jwtToken, expTime, user.getId());
    }
}
