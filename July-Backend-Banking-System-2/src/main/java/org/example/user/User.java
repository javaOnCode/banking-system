package org.example.user;

import org.example.account.AccountService;

public class User {
    public String name;
    public String idNumber;
    public int accountNumber;
    public String email;

    public AccountService accountService = new AccountService();
    public double totalLoanAmountDueInterest = accountService.totalLoanAmountDueInterest;
    public double monthlyPayment = accountService.monthlyPayment;
    public int monthLeft = accountService.monthLeft;


    public User() {
    }

    public User(String name, String idNumber, String email, int accountNumber) {
        this.name = name;
        this.idNumber = idNumber;
        this.accountNumber = accountNumber;
        this.email = email;
    }

}

