package org.example.Bank_Management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int  customerId;

    @Column(nullable = false, unique = true)
    private String email;

    private String fullName;
    private double balance;
    private String status;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    private String address;
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accounts;

}
