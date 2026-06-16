package org.example.Bank_Management.util;
import java.util.Random;

public class AccountNumberGenerator {

    public static String generate() {
        Random random = new Random();
        return "AC" + (100000000 + random.nextInt(900000000));
    }
}