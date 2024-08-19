package org.example.account;

import org.example.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    public User user;
    public String accountNumber;
    public double balance;
    public String cardNumber;
    public String cardExpirationDate;
    public int cvv;

    public List<AccountHistory> accountHistories;

    public Account() {
    }

    public Account(String accountNumber, double balance, String cardNumber, String cardExpirationDate, int cvv) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.cardNumber = cardNumber;
        this.cardExpirationDate = cardExpirationDate;
        this.cvv = cvv;
        this.accountHistories = new ArrayList<>();
    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;

    }
}
