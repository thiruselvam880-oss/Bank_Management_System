package org.example.Bank_Management.services;

import org.example.Bank_Management.dto.AccountRequest;
import org.example.Bank_Management.dto.DepositRequest;
import org.example.Bank_Management.dto.TransferRequest;
import org.example.Bank_Management.dto.WithdrawRequest;
import org.example.Bank_Management.model.Account;

import java.util.List;

public interface AccountService {
    Account createAccount(AccountRequest request);
    Account getAccountByNumber(String accountNumber);
    List<Account> getAccountsByCustomer(Long customerId);
    Account deposit(DepositRequest request);
    Account withdraw(WithdrawRequest request);
    String transfer(TransferRequest request);
}
