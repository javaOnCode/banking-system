package org.example;

import org.example.account.Account;
import org.example.account.AccountService;
import org.example.user.UserService;

public class Main {
    public static void main(String[] args) {

        UserService userService = new UserService();
        userService.createNewUser("Javanshir", "AA5199546", "agayevcavansir@gmail.com");

        Account account = new Account("AA5199546", 2000, "469873", "09/26", 198);
        AccountService accountService = new AccountService();

        accountService.accounts.add(account);

        accountService.depositMoney("AA5199546", 500);
        System.out.println(account.balance);

        accountService.manageLoan("AA5199546", 3000, 12);
    }
}