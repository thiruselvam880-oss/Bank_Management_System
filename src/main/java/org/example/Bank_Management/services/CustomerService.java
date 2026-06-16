package org.example.Bank_Management.services;

import org.example.Bank_Management.dto.CustomerRequest;
import org.example.Bank_Management.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(CustomerRequest request);
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long customerId);
}
