package org.example.account;

import org.example.user.User;

public class CheckingsAccount extends Account {
    public double overDraftLimit;

    public CheckingsAccount() {
    }

    public CheckingsAccount(String accountNumber, double balance, double overDraftLimit, String cardNumber, String cardExpirationDate, int cvv) {
        super(accountNumber, balance, cardNumber, cardExpirationDate, cvv);
        this.overDraftLimit = overDraftLimit;
    }
}
