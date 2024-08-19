package org.example.user;

import org.example.account.Account;
import org.example.account.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserService {
    public int userID = 1;
    private List<User> users;
    public Random random = new Random();
    public List<Account> accounts = new ArrayList<>();

    public UserService() {
        this.users = new ArrayList<>();
    }

    public void createNewUser(String name, String idNumber, String email) {
        int newAccountNumber = userID;
        User newUser = new User(name, idNumber, email, newAccountNumber);
        inputValidationOnAccountCreation(newUser);
        userID++;
        users.add(newUser);
    }

    public List<User> getUsers() {
        return users;
    }

    public void inputValidationOnAccountCreation(User user) {
        if (user.name == null) {
            System.out.println("Please provide your name for further account creation process");
            return;
        }
        if (user.idNumber == null) {
            System.out.println("Please enter your ID number for further account creation process");
            return;
        }

        System.out.println(user.name + ", your account has been succesfully created.");
    }

    public double getLoanInformation(AccountService accountService) {
        System.out.printf("\nTotal Loan Amount: $%.2f, Monthly Payment: $%.2f, Months left: %d", accountService.totalLoanAmountDueInterest, accountService.monthlyPayment, accountService.monthLeft);
        return accountService.totalLoanAmountDueInterest + accountService.monthlyPayment + accountService.monthLeft;
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        boolean unique;

        do {
            accountNumber = String.format("%06d", 100000 + random.nextInt(900000));
            unique = true;
            for (Account account : accounts) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    unique = false;
                    break;
                }
            }
        } while (!unique);

        return accountNumber;
    }
}
