package org.example.Bank_Management.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String address;

    @Data
    public static class AccountRequest {

        @NotNull(message = "Customer ID is required")
        private Long customerId;

        @NotNull(message = "Account type is required")
        private org.example.Bank_Management.enums.AccountType accountType;

        @NotNull(message = "Initial deposit is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Initial deposit must be >= 0")
        private BigDecimal initialDeposit;

        private String branchName;
        private String ifscCode;
    }
}