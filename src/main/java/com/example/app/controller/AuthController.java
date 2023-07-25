package com.example.app.controller;

import com.example.app.dto.LoginRequest;
import com.example.app.dto.SignupRequest;
import com.example.app.model.AppUser;
import com.example.app.repository.UserRepository;
import com.example.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static java.util.regex.Pattern.matches;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> signup(@RequestBody SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()) != null) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        //Create a new user
        AppUser newUser = new AppUser();
        newUser.setEmail(signupRequest.getEmail());
        newUser.setPassword(signupRequest.getPassword());
        newUser.setName(signupRequest.getName());

        userRepository.save(newUser);
        // Generate and include the JWT token in the response

        Map<String, Object> response = new HashMap<>();
        response.put("token", authService.generateJwtToken(newUser));
        response.put("user", newUser);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        AppUser user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null || !matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect email or password");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("token", authService.generateJwtToken(user));
        response.put("user", user);

        return ResponseEntity.ok(response);
    }
}
