package org.example.Bank_Management.repository;

import org.example.Bank_Management.model.Account;
import org.example.Bank_Management.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction,Integer> {
    List<Transaction> findByAccountOrderByTransactionDateTimeDesc(Account account);

    Optional<Transaction> findByTransactionReference(String transactionReference);

}
