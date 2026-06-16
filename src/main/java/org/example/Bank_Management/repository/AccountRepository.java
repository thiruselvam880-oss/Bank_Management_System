package org.example.Bank_Management.repository;

import org.example.Bank_Management.model.Account;
import org.example.Bank_Management.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account,Integer> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByCustomer(Customer customer);
}
