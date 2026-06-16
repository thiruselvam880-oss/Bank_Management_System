package org.example.Bank_Management.util;

import java.util.UUID;

public class TransactionReferenceGenerator {

    public static String generate() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}