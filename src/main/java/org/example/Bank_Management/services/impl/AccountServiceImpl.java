package org.example.Bank_Management.services.impl;

import jakarta.transaction.Transactional;
import org.example.Bank_Management.dto.AccountRequest;
import org.example.Bank_Management.dto.DepositRequest;
import org.example.Bank_Management.dto.TransferRequest;
import org.example.Bank_Management.dto.WithdrawRequest;
import org.example.Bank_Management.enums.AccountStatus;
import org.example.Bank_Management.enums.TransactionStatus;
import org.example.Bank_Management.enums.TransactionType;
import org.example.Bank_Management.exception.InsufficientBalanceException;
import org.example.Bank_Management.exception.InvalidOperationException;
import org.example.Bank_Management.exception.ResourceNotFoundException;
import org.example.Bank_Management.model.Account;
import org.example.Bank_Management.model.Customer;
import org.example.Bank_Management.model.Transaction;
import org.example.Bank_Management.repository.AccountRepository;
import org.example.Bank_Management.repository.CustomerRepository;
import org.example.Bank_Management.repository.TransactionRepository;
import org.example.Bank_Management.services.AccountService;
import org.example.Bank_Management.util.AccountNumberGenerator;
import org.example.Bank_Management.util.TransactionReferenceGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository,
                              CustomerRepository customerRepository,
                              TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Account createAccount(AccountRequest request) {
        Customer customer = customerRepository.findById(Long.valueOf(Math.toIntExact(request.getCustomerId())))
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        String accountNumber = AccountNumberGenerator.generate();
        while (accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            accountNumber = AccountNumberGenerator.generate();
        }

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountType(request.getAccountType());
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(request.getInitialDeposit());
        account.setBranchName(request.getBranchName());
        account.setOpenedDate(LocalDateTime.now());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        account.setCustomer(customer);

        Account savedAccount = accountRepository.save(account);

        Transaction openingTransaction = Transaction.builder()
                .transactionReference(TransactionReferenceGenerator.generate())
                .transactionType(TransactionType.ACCOUNT_OPENING)
                .transactionStatus(TransactionStatus.SUCCESS)
                .amount(request.getInitialDeposit())
                .availableBalanceAfterTransaction(savedAccount.getBalance())
                .description("Account opening initial deposit")
                .fromAccountNumber(savedAccount.getAccountNumber())
                .toAccountNumber(savedAccount.getAccountNumber())
                .transactionDateTime(LocalDateTime.now())
                .account(savedAccount)
                .build();

        transactionRepository.save(openingTransaction);

        return savedAccount;
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    @Override
    public List<Account> getAccountsByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(Long.valueOf(Math.toIntExact(customerId)))
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return accountRepository.findByCustomer(customer);
    }

    @Override
    public Account deposit(DepositRequest request) {
        Account account = getAccountByNumber(request.getAccountNumber());

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidOperationException("Account is not active");
        }

        account.setBalance(account.getBalance().add(request.getAmount()));
        account.setUpdatedAt(LocalDateTime.now());

        Account saved = accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .transactionReference(TransactionReferenceGenerator.generate())
                .transactionType(TransactionType.DEPOSIT)
                .transactionStatus(TransactionStatus.SUCCESS)
                .amount(request.getAmount())
                .availableBalanceAfterTransaction(saved.getBalance())
                .description(request.getDescription() != null ? request.getDescription() : "Deposit successful")
                .fromAccountNumber(saved.getAccountNumber())
                .toAccountNumber(saved.getAccountNumber())
                .transactionDateTime(LocalDateTime.now())
                .account(saved)
                .build();

        transactionRepository.save(transaction);

        return saved;
    }

    @Override
    public Account withdraw(WithdrawRequest request) {
        Account account = getAccountByNumber(request.getAccountNumber());

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidOperationException("Account is not active");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        account.setUpdatedAt(LocalDateTime.now());

        Account saved = accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .transactionReference(TransactionReferenceGenerator.generate())
                .transactionType(TransactionType.WITHDRAW)
                .transactionStatus(TransactionStatus.SUCCESS)
                .amount(request.getAmount())
                .availableBalanceAfterTransaction(saved.getBalance())
                .description(request.getDescription() != null ? request.getDescription() : "Withdrawal successful")
                .fromAccountNumber(saved.getAccountNumber())
                .toAccountNumber(saved.getAccountNumber())
                .transactionDateTime(LocalDateTime.now())
                .account(saved)
                .build();

        transactionRepository.save(transaction);

        return saved;
    }

    @Override
    @Transactional
    public String transfer(TransferRequest request) {
        if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
            throw new InvalidOperationException("Sender and receiver account cannot be same");
        }

        Account fromAccount = getAccountByNumber(request.getFromAccountNumber());
        Account toAccount = getAccountByNumber(request.getToAccountNumber());

        if (fromAccount.getStatus() != AccountStatus.ACTIVE || toAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidOperationException("Both accounts must be active");
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in sender account");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        fromAccount.setUpdatedAt(LocalDateTime.now());

        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
        toAccount.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction debitTransaction = Transaction.builder()
                .transactionReference(TransactionReferenceGenerator.generate())
                .transactionType(TransactionType.TRANSFER_DEBIT)
                .transactionStatus(TransactionStatus.SUCCESS)
                .amount(request.getAmount())
                .availableBalanceAfterTransaction(fromAccount.getBalance())
                .description(request.getDescription() != null ? request.getDescription() : "Transfer debit")
                .fromAccountNumber(fromAccount.getAccountNumber())
                .toAccountNumber(toAccount.getAccountNumber())
                .transactionDateTime(LocalDateTime.now())
                .account(fromAccount)
                .build();

        Transaction creditTransaction = Transaction.builder()
                .transactionReference(TransactionReferenceGenerator.generate())
                .transactionType(TransactionType.TRANSFER_CREDIT)
                .transactionStatus(TransactionStatus.SUCCESS)
                .amount(request.getAmount())
                .availableBalanceAfterTransaction(toAccount.getBalance())
                .description(request.getDescription() != null ? request.getDescription() : "Transfer credit")
                .fromAccountNumber(fromAccount.getAccountNumber())
                .toAccountNumber(toAccount.getAccountNumber())
                .transactionDateTime(LocalDateTime.now())
                .account(toAccount)
                .build();

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        return "Transfer successful";
    }
}
