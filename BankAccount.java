import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class BankAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int accountNumber;
    private final String name;
    private final String pinHash; 
    private double balance;
    private final ArrayList<String> transactions;

    public BankAccount(int accountNumber, String name, int pin, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.pinHash = hashPin(pin);
        this.balance = balance;
        this.transactions = new ArrayList<>();
        this.transactions.add("Account created with ₹" + balance);
    }

    private String hashPin(int pin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(Integer.toString(pin).getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public boolean validatePin(int pin) {
        return this.pinHash.equals(hashPin(pin));
    }

    public int getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount <= 0) { System.out.println("Invalid amount"); return; }
        balance += amount;
        transactions.add("Deposited ₹" + amount + " | Balance: ₹" + balance);
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            System.out.println(amount <= 0 ? "Invalid amount" : "Insufficient balance");
            return false;
        }
        balance -= amount;
        transactions.add("Withdraw ₹" + amount + " | Balance: ₹" + balance);
        return true;
    }

    public void showDetails() {
        System.out.println("\n--- ACCOUNT DETAILS ---");
        System.out.println("Account No : " + accountNumber);
        System.out.println("Name       : " + name);
        System.out.println("Balance    : ₹" + balance);
    }

    public void showTransactions() {
        System.out.println("\n--- TRANSACTION HISTORY ---");
        if (transactions.isEmpty()) { System.out.println("No transactions found"); return; }
        for (String t : transactions) System.out.println(t);
    }
}