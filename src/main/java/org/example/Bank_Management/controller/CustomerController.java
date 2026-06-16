package org.example.Bank_Management.controller;

import jakarta.validation.Valid;
import org.example.Bank_Management.dto.ApiResponse;
import org.example.Bank_Management.dto.CustomerRequest;
import org.example.Bank_Management.model.Customer;
import org.example.Bank_Management.services.CustomerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<Customer> createCustomer(@Valid @RequestBody CustomerRequest request) {
        Customer customer = customerService.createCustomer(request);
        return ApiResponse.<Customer>builder()
                .success(true)
                .message("Customer created successfully")
                .data(customer)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<Customer>> getAllCustomers() {
        return ApiResponse.<List<Customer>>builder()
                .success(true)
                .message("Customers fetched successfully")
                .data(customerService.getAllCustomers())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{customerId}")
    public ApiResponse<Customer> getCustomerById(@PathVariable Long customerId) {
        return ApiResponse.<Customer>builder()
                .success(true)
                .message("Customer fetched successfully")
                .data(customerService.getCustomerById(customerId))
                .timestamp(LocalDateTime.now())
                .build();
    }
}