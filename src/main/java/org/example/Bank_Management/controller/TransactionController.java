package org.example.Bank_Management.controller;

import org.example.Bank_Management.dto.ApiResponse;
import org.example.Bank_Management.model.Transaction;
import org.example.Bank_Management.services.TransactionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping("/account/{accountNumber}")
    public ApiResponse<List<Transaction>> getTransactionsByAccount(@PathVariable String accountNumber) {
        return ApiResponse.<List<Transaction>>builder()
                .success(true)
                .message("Transactions fetched successfully")
                .data(transactionService.getTransactionsByAccount(accountNumber))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @GetMapping("/{reference}")
    public ApiResponse<Transaction> getTransactionByReference(@PathVariable String reference) {
        return ApiResponse.<Transaction>builder()
                .success(true)
                .message("Transaction fetched successfully")
                .data(transactionService.getTransactionByReference(reference))
                .timestamp(LocalDateTime.now())
                .build();
    }
}