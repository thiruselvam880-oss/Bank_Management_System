package org.example.Bank_Management.controller;

import jakarta.validation.Valid;
import org.example.Bank_Management.dto.*;
import org.example.Bank_Management.model.Account;
import org.example.Bank_Management.services.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // ADMIN ONLY - create account for customer
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ApiResponse<Account> createAccount(@Valid @RequestBody AccountRequest request) {
        Account account = accountService.createAccount(request);
        return ApiResponse.<Account>builder()
                .success(true)
                .message("Account created successfully")
                .data(account)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ADMIN or CUSTOMER
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping("/{accountNumber}")
    public ApiResponse<Account> getAccount(@PathVariable String accountNumber) {
        return ApiResponse.<Account>builder()
                .success(true)
                .message("Account fetched successfully")
                .data(accountService.getAccountByNumber(accountNumber))
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ADMIN ONLY
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/customer/{customerId}")
    public ApiResponse<List<Account>> getAccountsByCustomer(@PathVariable Long customerId) {
        return ApiResponse.<List<Account>>builder()
                .success(true)
                .message("Customer accounts fetched successfully")
                .data(accountService.getAccountsByCustomer(customerId))
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ADMIN or CUSTOMER
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping("/deposit")
    public ApiResponse<Account> deposit(@Valid @RequestBody DepositRequest request) {
        Account account = accountService.deposit(request);
        return ApiResponse.<Account>builder()
                .success(true)
                .message("Deposit successful")
                .data(account)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ADMIN or CUSTOMER
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping("/withdraw")
    public ApiResponse<Account> withdraw(@Valid @RequestBody WithdrawRequest request) {
        Account account = accountService.withdraw(request);
        return ApiResponse.<Account>builder()
                .success(true)
                .message("Withdrawal successful")
                .data(account)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ADMIN or CUSTOMER
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping("/transfer")
    public ApiResponse<Map<String, String>> transfer(@Valid @RequestBody TransferRequest request) {
        String result = accountService.transfer(request);
        return ApiResponse.<Map<String, String>>builder()
                .success(true)
                .message(result)
                .data(Map.of("status", result))
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ADMIN or CUSTOMER
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping("/{accountNumber}/balance")
    public ApiResponse<Map<String, Object>> getBalance(@PathVariable String accountNumber) {
        Account account = accountService.getAccountByNumber(accountNumber);
        return ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Balance fetched successfully")
                .data(Map.of(
                        "accountNumber", account.getAccountNumber(),
                        "balance", account.getBalance(),
                        "status", account.getStatus()
                ))
                .timestamp(LocalDateTime.now())
                .build();
    }
}