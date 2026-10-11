package com.example.pharmacy.controller;

import com.example.pharmacy.entity.User;
import com.example.pharmacy.security.JwtService;
import com.example.pharmacy.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Locale;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "https://pharmacy-frontend-sb2d.onrender.com")
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    public UserController(UserService userService,
                          JwtService jwtService) {

        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {

        return userService.register(user);
    }

    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestBody User user) {

        User loggedInUser =
                userService.login(
                        user.getEmail(),
                        user.getPassword()
                );

        String role = loggedInUser.getRole();

        if (role != null) {
            role = role.trim()
                    .replaceFirst("(?i)^ROLE_", "")
                    .toUpperCase(Locale.ROOT);
        }

        String token =
                jwtService.generateToken(
                        loggedInUser.getEmail(),
                        role
                );

        Map<String, Object> response =
                new HashMap<>();

        response.put("id", loggedInUser.getId());
        response.put("name", loggedInUser.getName());
        response.put("email", loggedInUser.getEmail());
        response.put("role", role);
        response.put("token", token);

        return response;
    }
}
