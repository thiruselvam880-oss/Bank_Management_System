package org.example.Bank_Management.services.impl;


import org.example.Bank_Management.dto.CustomerRequest;
import org.example.Bank_Management.exception.InvalidOperationException;
import org.example.Bank_Management.exception.ResourceNotFoundException;
import org.example.Bank_Management.model.Customer;
import org.example.Bank_Management.repository.CustomerRepository;
import org.example.Bank_Management.services.CustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(CustomerRequest request) {
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new InvalidOperationException("Email already exists");
        }

        if (customerRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new InvalidOperationException("Phone number already exists");
        }

        Customer customer = Customer.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }
}
