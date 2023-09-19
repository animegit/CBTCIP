import java.io.*;
import java.util.*;
import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface LogTransaction {}

class User {
    private String userId;
    private int pin;
    private String name;

    public User(String userId, int pin, String name) {
        this.userId = userId;
        this.pin = pin;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public int getPin() {
        return pin;
    }

    public String getName() {
        return name;
    }
}

class Account {
    private User user;
    private double balance;

    public Account(User user) {
        this.user = user;
        this.balance = 0;
    }

    public User getUser() {
        return user;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public boolean transfer(Account recipient, double amount) {
        if (withdraw(amount)) {
            recipient.deposit(amount);
            return true;
        }
        return false;
    }
}

class Transaction {
    private User user;
    private String type;
    private double amount;
    private Date timestamp;

    public Transaction(User user, String type, double amount) {
        this.user = user;
        this.type = type;
        this.amount = amount;
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return "User: " + user.getName() + "\nType: " + type + "\nAmount: " + amount + "\nTimestamp: " + timestamp;
    }
}

class ATM {
    private Account account;
    private ArrayList<Transaction> transactionHistory;

    public ATM(Account account) {
        this.account = account;
        this.transactionHistory = new ArrayList<>();
    }

    @LogTransaction
    public void deposit(double amount) {
        if (account.deposit(amount)) {
            transactionHistory.add(new Transaction(account.getUser(), "Deposit", amount));
            System.out.println("Deposited " + amount + "Rs successfully.");
        } else {
            System.out.println("Invalid amount for deposit.");
        }
    }

    @LogTransaction
    public void withdraw(double amount) {
        if (account.withdraw(amount)) {
            transactionHistory.add(new Transaction(account.getUser(), "Withdraw", amount));
            System.out.println("Withdrawn " + amount + "Rs successfully.");
        } else {
            System.out.println("Insufficient balance or invalid amount for withdrawal.");
        }
    }

    @LogTransaction
    public void transfer(Account recipient, double amount) {
        if (account.transfer(recipient, amount)) {
            transactionHistory.add(new Transaction(account.getUser(), "Transfer", amount));
            System.out.println("Transferred " + amount + "Rs successfully to " + recipient.getUser().getUserId());
        } else {
            System.out.println("Insufficient balance or invalid amount for transfer.");
        }
    }

    public void showBalance() {
        System.out.println("Current Balance: " + account.getBalance()+"Rs.");
    }

    public void showTransactionHistory() {
        for (Transaction transaction : transactionHistory) {
            System.out.println(transaction);
            System.out.println("---------------------------");
        }
    }
}

class Main {
    public static void main(String[] args) {
        User user1 = new User("123456", 1234, "Animesh");
        Account account1 = new Account(user1);
        ATM atm1 = new ATM(account1);

        Scanner scanner = new Scanner(System.in);
        int attempts = 3;

        while (attempts > 0) {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter PIN: ");
            int pin = Integer.parseInt(scanner.nextLine());

            if (userId.equals(user1.getUserId()) && pin == user1.getPin()) {
                System.out.println("Welcome, " + user1.getName() + "!");
                showMenu(atm1);
                break;
            } else {
                attempts--;
                if (attempts > 0) {
                    System.out.println("Invalid User ID or PIN. Attempts left: " + attempts);
                } else {
                    System.out.println("Max attempts reached. Card blocked.");
                }
            }
        }
    }

    public static void showMenu(ATM atm) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Show Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Transaction History");
            System.out.println("6. Exit");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    atm.showBalance();
                    break;
                case 2:
                    System.out.print("Enter the deposit amount: in Rs.");
                    double depositAmount = Double.parseDouble(scanner.nextLine());
                    atm.deposit(depositAmount);
                    break;
                case 3:
                    System.out.print("Enter the withdrawal amount: in Rs.");
                    double withdrawAmount = Double.parseDouble(scanner.nextLine());
                    atm.withdraw(withdrawAmount);
                    break;
                case 4:
                    System.out.print("Enter the recipient's User ID: ");
                    String recipientUserId = scanner.nextLine();
                    System.out.print("Enter the recipient's User Name: ");
                    String recpname=scanner.nextLine();
                    
                    Account recipientAccount = new Account(new User(recipientUserId, 0, ""));
                    System.out.print("Enter the transfer amount: in Rs.");
                    double transferAmount = Double.parseDouble(scanner.nextLine());
                    atm.transfer(recipientAccount, transferAmount);
                    break;
                case 5:
                    atm.showTransactionHistory();
                    break;
                case 6:
                    System.out.println("Exiting the ATM. Thank you!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }
}
