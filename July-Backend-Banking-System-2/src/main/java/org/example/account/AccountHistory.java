package org.example.account;


public class AccountHistory {
    public String transactionType;
    public double amount;
    public double balancePostTransaction;

    public Account account;


    public AccountHistory(String transactionType, double amount, double balancePostTransaction) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.balancePostTransaction = balancePostTransaction;
    }

    public String toString() {
        return "Transaction Type: " + transactionType + "Amount: $" + amount + ", Balance After Transaction: $" + balancePostTransaction;
    }
}
