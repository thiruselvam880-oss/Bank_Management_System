package org.example.Bank_Management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
public class BankManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankManagementApplication.class, args);
        System.out.println("Bank Management Application has been started");
        System.out.println(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        System.out.println(LocalDateTime.now());
    }

}
