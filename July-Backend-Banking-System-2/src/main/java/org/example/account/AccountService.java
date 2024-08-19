package org.example.account;

import org.example.user.User;
import org.example.user.UserService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Scanner;

import java.time.YearMonth;


public class AccountService {
    public LocalDateTime currentDate = LocalDateTime.now();

    public DateTimeFormatter formattedDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public double interestRate = 10.9;

    public Scanner scanner;

    public double totalLoanAmountDueInterest;
    public double monthlyPayment;
    public int monthLeft;

    public List<Account> accounts = new ArrayList<>();

    public AccountService() {
    }

    public void depositMoney(String accountNumber, double amount) {
        if (amount > 0) {
            Account account = findAccountByNumber(accountNumber);
            if (account != null) {
                account.balance += amount;
                System.out.println("$" + amount + " is deposited to your account.");
            } else {
                System.out.println("Account with number " + accountNumber + " not found.");
            }
        } else {
            System.out.println("Invalid amount of deposit!");
        }
    }

    public void withDraw(String accountNumber, double amount) {
        if (amount > 0) {
            Account account = findAccountByNumber(accountNumber);

            if (account != null) {
                if (account.balance >= amount) {
                    account.balance -= amount;
                    System.out.println("$" + amount + " is withdrawn from your account.");
                } else {
                    System.out.println("Insufficient funds!");
                }
            } else {
                System.out.println("Account with number " + accountNumber + " not found.");
            }
        } else {
            System.out.println("Invalid amount to withdraw!");
        }
    }

    public boolean validateAmount(double amount) {
        return amount > 0;
    }

    public void getTransactionHistory(String accountNumber) {
        Account account = findAccountByNumber(accountNumber);

        if (account != null) {
            System.out.println("\nTransaction History for " + account.user.name);
            for (AccountHistory history : account.accountHistories) {
                System.out.println(history);
            }
        } else {
            System.out.println("Account with number " + accountNumber + " not found.");
        }
    }

    public void transferMoney(String senderAccountNumber, String takerAccountNumber, double amount, UserService userService) {

        Account accountSender = (Account) findAccountByNumber(senderAccountNumber);
        Account accountTaker = (Account) findAccountByNumber(takerAccountNumber);

        boolean senderRegistered = false;
        boolean takerRegistered = false;

        for (User user : userService.getUsers()) {
            if (user.idNumber.equals(accountSender.user.idNumber)) {
                senderRegistered = true;
            }
            if (user.idNumber.equals(accountTaker.user.idNumber)) {
                takerRegistered = true;
            }
        }

        if (accountSender != null && accountTaker != null) {
            if (senderRegistered && takerRegistered) {
                if (accountSender.balance >= amount) {
                    accountSender.balance -= amount;
                    accountTaker.balance += amount;

                    accountSender.accountHistories.add(new AccountHistory("Transfer", amount, accountSender.balance));
                    accountTaker.accountHistories.add(new AccountHistory("Deposit", amount, accountTaker.balance));

                    System.out.println(accountTaker.user.name + ", you have been transferred $" + amount + " from " + accountSender.user.name);
                    System.out.println("Your balance after transaction: $" + accountTaker.balance);
                } else {
                    System.out.println("Insufficient balance to transfer!");
                }
            } else if (!senderRegistered) {
                System.out.println(accountSender.user.name + " is not registered on the system");
            } else if (!takerRegistered) {
                System.out.println(accountTaker.user.name + " is not registered on the system");
            }
        } else {
            if (accountSender == null) {
                System.out.println("Sender account with number " + senderAccountNumber + " not found.");
            }
            if (accountTaker == null) {
                System.out.println("Receiver account with number " + takerAccountNumber + " not found.");
            }
        }
    }

    public void writeCheckForCheckings(String accountNumber, double amount) {
        if (amount > 0) {
            CheckingsAccount checkingsAccount = (CheckingsAccount) findAccountByNumber(accountNumber);

            if (checkingsAccount != null) {
                withDraw(accountNumber, amount);
                System.out.println("Check written for $" + amount + " is withdrawn from account.");
                System.out.println("Remaining balance: $" + checkingsAccount.balance);
            } else {
                System.out.println("Checkings account with number " + accountNumber + " not found.");
            }
        } else {
            System.out.println("Invalid amount to withdraw!");
        }
    }

    public void payBillfromCheckings(String accountNumber, double amount, String biller) {
        if (amount > 0) {
            CheckingsAccount checkingsAccount = (CheckingsAccount) findAccountByNumber(accountNumber);

            if (checkingsAccount != null) {
                withDraw(accountNumber, amount);
                System.out.println("$" + amount + " paid to the " + biller);
            } else {
                System.out.println("Checkings account with number " + accountNumber + " not found.");
            }
        } else {
            System.out.println("Invalid amount to pay!");
        }
    }

    public void withDrawFromSavings(String accountNumber, double amount) {
        if (amount > 0) {
            SavingsAccount savingsAccount = (SavingsAccount) findAccountByNumber(accountNumber);

            if (savingsAccount != null) {
                if (savingsAccount.withdrawals < 6) {
                    withDraw(accountNumber, amount);
                    System.out.println("Withdrawal of $" + amount + " is made on " + LocalDateTime.now().format(savingsAccount.formattedDate));
                    savingsAccount.withdrawals++;
                } else {
                    YearMonth monthOfYear = YearMonth.from(savingsAccount.currentDate);
                    int lengthOfMonth = monthOfYear.lengthOfMonth();
                    int daysTillEndOfMonth = lengthOfMonth - savingsAccount.currentDate.getDayOfMonth();
                    daysTillEndOfMonth++;
                    LocalDateTime nextWithdrawalDate = savingsAccount.currentDate.plusDays(daysTillEndOfMonth);

                    System.out.printf("Withdrawal limits reached for this month. \nYou can withdraw starting from this date: %s\n", nextWithdrawalDate.format(savingsAccount.formattedDateDMY));
                }
            } else {
                System.out.println("Savings account with number " + accountNumber + " not found.");
            }
        } else {
            System.out.println("Invalid amount to withdraw!");
        }
    }

    public void addInterestToSavings() {
        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.balance += savingsAccount.balance * savingsAccount.interestRate;
        System.out.println("Balance after addition of interest: $" + savingsAccount.balance);
    }

    public void calculateInterest(String accountNumber) {
        Account account = findAccountByNumber(accountNumber);

        if (account == null) {
            System.out.println("This account is not actively registered on the system.");
            return;
        }
        if (interestRate < 0) {
            System.out.println("Interest rate should be more than 0");
            return;
        }
        if (account.balance < 0) {
            System.out.println("Balance should be more than 0 for interest gaining operation.");
            return;
        }

        LocalDateTime dayRequested = LocalDateTime.now();
        LocalDateTime oneMonthLater = dayRequested.plusMonths(1);
        long daysTillConfirm = ChronoUnit.DAYS.between(dayRequested, oneMonthLater);

        double profitAmount = account.balance * (interestRate / 100);
        account.balance += profitAmount;

        if (dayRequested.isAfter(oneMonthLater.minusDays(1))) {
            System.out.println("Payment is successfully done.");
            System.out.println("Balance after Profit: $" + account.balance);
        } else {
            System.out.printf("Your payment is requested on %s and will be actively posted on the system after %d days.%n",
                    dayRequested.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), daysTillConfirm);
            System.out.printf("$%.2f will be added to your account after completion of the interest operation on this date - %s%n",
                    profitAmount, oneMonthLater.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
    }

    public void manageLoan(String accountNumber, double loanAmount, int termInMonth) {

        Account account = findAccountByNumber(accountNumber);

        if (account == null) {
            System.out.println("Account with number " + accountNumber + " is not registered on the system.");
            return;
        }
        if (loanAmount < 1000) {
            System.out.println("Loan Amount should be more than $1000!");
            return;
        }
        if (interestRate < 0) {
            System.out.println("Interest rate can't be negative!");
            return;
        }
        if (termInMonth <= 3) {
            System.out.println("Term should be at least 3 months length!");
            return;
        }

        double totalAmountDueInterest = loanAmount + (loanAmount * interestRate / 100);
        double monthlyPayment = totalAmountDueInterest / termInMonth;
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime oneMonthLater = currentDate.plusMonths(1);
        long daysTillNextPayment = ChronoUnit.DAYS.between(currentDate, oneMonthLater);

        account.balance += loanAmount;
        account.accountHistories.add(new AccountHistory("Loan", loanAmount, account.balance));

        System.out.printf("You have loaned $%.2f and total amount based on interest that you will pay back is $%.2f%n", loanAmount, totalAmountDueInterest);
        System.out.printf("Your balance after loan amount added: $%.2f%n", account.balance);
        System.out.printf("You are calculated to pay the amount back in %d months and your monthly payment will be equal to $%.2f%n", termInMonth, monthlyPayment);
        System.out.printf("Next payment is awaiting to be paid after %d days, on this date - %s%n", daysTillNextPayment, oneMonthLater.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public List<Account> listAllAccounts() {
        return accounts;
    }

    public Account findAccountByNumber(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    public void displayAccountDetails(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                System.out.println("Account Details:");
                System.out.println("Account Number: " + account.getAccountNumber());
                System.out.println("Balance: $" + account.getBalance());
                return;
            }
        }
        System.out.println("Account with number " + accountNumber + " not found.");
    }

    public void saveAccountsToFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            System.out.println("Invalid filename.");
            return;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(accounts);
            System.out.println("Accounts have been successfully saved.");
        } catch (IOException e) {
            System.err.println("An error occurred while saving accounts: " + e.getMessage());

        }
    }

    public String checkBalance(String accountNumber) throws AccountNotFoundException {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return "Account balance: " + account.getBalance();
            }
        }

        throw new AccountNotFoundException("Account with number " + accountNumber + " not found.");
    }


    public boolean deleteAccount(String accountNumber) {

        List<Account> updatedAccounts = new ArrayList<>();
        boolean accountFound = false;

        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                accountFound = true;
            } else {
                updatedAccounts.add(account);
            }
        }

        if (accountFound) {
            accounts = updatedAccounts;
            return true;
        } else {
            return false;
        }
    }

    public boolean confirmDeleting(String accountNumber) {

        while (true) {
            System.out.print("Are you sure you want to delete account " + accountNumber + "? (true/false): ");

            boolean confirmation = scanner.nextBoolean();

            scanner.nextLine();

            if (confirmation) {
                return true;
            } else {
                System.out.println("Account deletion cancelled.");
                return false;
            }
        }
    }


}
