package org.example.account;

import org.example.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SavingsAccount extends Account {
    public double interestRate;
    public int withdrawals;
    public LocalDateTime currentDate = LocalDateTime.now();
    public DateTimeFormatter formattedDate = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    public DateTimeFormatter formattedDateDMY = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public SavingsAccount() {
    }

    public SavingsAccount( String accountNumber, double balance, double interestRate, String cardNumber, String cardExpirationDate, int cvv) {
        super( accountNumber, balance, cardNumber,cardExpirationDate,cvv);
        this.interestRate = interestRate;
        this.withdrawals = 0;
    }
}
