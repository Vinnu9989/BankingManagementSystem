import java.io.*;
import java.util.ArrayList;

public class Bank {

    private final ArrayList<BankAccount> accounts;
    private final String FILE_NAME = "bank_data.dat";
    private int accountCounter = 1000;

    public Bank() {
        accounts = loadData();

        if (!accounts.isEmpty()) {
            accountCounter = accounts.get(accounts.size() - 1).getAccountNumber() + 1;
        }
    }

    public int createAccount(String name, int pin, double balance) {
        BankAccount acc = new BankAccount(accountCounter, name, pin, balance);
        accounts.add(acc);
        saveData();
        return accountCounter++;
    }

    public BankAccount login(int accNo, int pin) {
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber() == accNo && acc.validatePin(pin)) {
                return acc;
            }
        }
        return null;
    }

    public void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found");
            return;
        }

        for (BankAccount acc : accounts) {
            System.out.println("Account No: " + acc.getAccountNumber()
                    + " | Name: " + acc.getName()
                    + " | Balance: ₹" + acc.getBalance());
        }
    }

    public void saveData() {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(accounts);

        } catch (Exception e) {
            System.out.println("Error saving data");
        }
    }

    public final ArrayList<BankAccount> loadData() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            Object obj = ois.readObject();

            if (obj instanceof ArrayList<?> temp) {

                ArrayList<BankAccount> list = new ArrayList<>();

                for (Object o : temp) {
                    if (o instanceof BankAccount bankAccount) {
                        list.add(bankAccount);
                    }
                }

                return list;
            }

        } catch (Exception e) {
            // file not found or first run
        }

        return new ArrayList<>();
    }
}