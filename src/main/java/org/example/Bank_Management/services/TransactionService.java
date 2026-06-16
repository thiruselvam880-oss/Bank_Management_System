package org.example.Bank_Management.services;

import org.example.Bank_Management.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getTransactionsByAccount(String accountNumber);
    Transaction getTransactionByReference(String reference);
}
