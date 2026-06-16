package org.example.Bank_Management.controller;

import org.example.Bank_Management.dto.*;
import org.example.Bank_Management.services.AuthService;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register-customer")
    public String registerCustomer(@RequestBody RegisterCustomerRequest request) {
        return authService.registerCustomer(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/create-admin")
    public String createAdmin(@RequestParam String username, @RequestParam String password) {
        return authService.createAdmin(username, password);
    }
}