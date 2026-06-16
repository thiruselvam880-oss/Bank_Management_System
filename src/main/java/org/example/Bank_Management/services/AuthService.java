package org.example.Bank_Management.services;

import org.example.Bank_Management.dto.*;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    String registerCustomer(RegisterCustomerRequest request);
    String createAdmin(String username, String password);
}