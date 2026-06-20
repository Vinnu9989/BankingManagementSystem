import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Bank bank = new Bank();

        // Try-with-resources automatically closes the scanner when the main method exits
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n===== BANK SYSTEM =====");
                System.out.println("1. Create Account");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter choice: ");

                int choice = readInt(sc);

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Name: ");
                        String name = sc.nextLine();
                        System.out.print("Set PIN (Numeric): ");
                        int pin = readInt(sc);
                        System.out.print("Initial Deposit: ");
                        double bal = readDouble(sc);
                        int accNo = bank.createAccount(name, pin, bal);
                        System.out.println("\nAccount Created Successfully!\nAccount Number: " + accNo);
                    }
                    case 2 -> {
                        System.out.println("\n--- ACCOUNTS ---");
                        bank.displayAllAccounts();
                        System.out.print("\nEnter Account Number: ");
                        int accNo = readInt(sc);
                        System.out.print("Enter PIN: ");
                        int pin = readInt(sc);
                        BankAccount acc = bank.login(accNo, pin);
                        if (acc == null) {
                            System.out.println("Invalid credentials");
                            continue;
                        }
                        System.out.println("\nWelcome " + acc.getName());
                        handleATMMenu(acc, bank, sc);
                    }
                    case 3 -> {
                        System.out.println("Thank you!");
                        return; // No manual sc.close() needed here anymore!
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }

    private static void handleATMMenu(BankAccount acc, Bank bank, Scanner sc) {
        while (true) {
            System.out.println("\n--- ATM MENU ---");
            System.out.println("1. Deposit\n2. Withdraw\n3. Balance\n4. Details\n5. Transactions\n6. Logout");
            System.out.print("Enter choice: ");
            int ch = readInt(sc);

            switch (ch) {
                case 1 -> {
                    System.out.print("Enter amount: ");
                    acc.deposit(readDouble(sc));
                    bank.saveData();
                    System.out.println("Deposit successful");
                }
                case 2 -> {
                    System.out.print("Enter amount: ");
                    if (acc.withdraw(readDouble(sc))) {
                        bank.saveData();
                        System.out.println("Withdraw successful");
                    }
                }
                case 3 -> System.out.println("Balance: ₹" + acc.getBalance());
                case 4 -> acc.showDetails();
                case 5 -> acc.showTransactions();
                case 6 -> {
                    bank.saveData();
                    System.out.println("Logged out");
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static int readInt(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid numeric format. Please enter a valid integer: ");
            }
        }
    }

    private static double readDouble(Scanner sc) {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid decimal format. Please enter a valid number: ");
            }
        }
    }
}