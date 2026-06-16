package org.example.Bank_Management.services.impl;

import org.example.Bank_Management.dto.*;
import org.example.Bank_Management.enums.Role;
import org.example.Bank_Management.exception.InvalidOperationException;
import org.example.Bank_Management.model.Customer;
import org.example.Bank_Management.model.User;
import org.example.Bank_Management.repository.CustomerRepository;
import org.example.Bank_Management.repository.UserRepository;
import org.example.Bank_Management.security.JwtUtil;
import org.example.Bank_Management.services.AuthService;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidOperationException("User not found"));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return new AuthResponse(token, user.getRole().name(), user.getUsername());
    }

    @Override
    public String registerCustomer(RegisterCustomerRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new InvalidOperationException("Username already exists");
        }

        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new InvalidOperationException("Email already exists");
        }

        if (customerRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new InvalidOperationException("Phone number already exists");
        }

        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setAddress(request.getAddress());
        customer.setStatus("ACTIVE");
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());

        Customer savedCustomer = customerRepository.save(customer);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        user.setCustomer(savedCustomer);

        userRepository.save(user);

        return "Customer registered successfully";
    }

    @Override
    public String createAdmin(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new InvalidOperationException("Admin username already exists");
        }

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole(Role.ADMIN);
        admin.setCustomer(null);

        userRepository.save(admin);

        return "Admin created successfully";
    }
}