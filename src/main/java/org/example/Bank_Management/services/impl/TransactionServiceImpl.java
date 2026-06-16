package org.example.Bank_Management.services.impl;

import org.example.Bank_Management.exception.ResourceNotFoundException;
import org.example.Bank_Management.model.Account;
import org.example.Bank_Management.model.Transaction;
import org.example.Bank_Management.repository.AccountRepository;
import org.example.Bank_Management.repository.TransactionRepository;
import org.example.Bank_Management.services.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return transactionRepository.findByAccountOrderByTransactionDateTimeDesc(account);
    }

    @Override
    public Transaction getTransactionByReference(String reference) {
        return transactionRepository.findByTransactionReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }
}