[BankingManagementSystem-README.md](https://github.com/user-attachments/files/29159770/BankingManagementSystem-README.md)
# Banking Management System

A console-based Java application for managing bank accounts — account creation, login, deposits, withdrawals, and transaction history — with data persisted to disk using Java Serialization.

## Features

- **Account Creation** — opens a new account with an auto-incrementing account number, starting balance, and a PIN
- **Secure Login** — PINs are never stored in plain text; they're hashed with SHA-256 before being saved, and login compares hashes, not raw PIN values
- **Deposits & Withdrawals** — balance updates with input validation (rejects negative amounts, blocks withdrawals that exceed the available balance)
- **Transaction History** — every deposit/withdrawal is logged per account and viewable on demand
- **Persistent Storage** — all accounts and their transaction histories are saved to a local file and reloaded automatically the next time the app starts, so nothing is lost when it closes

## Tech Stack

- Java (Core Java, OOP — Encapsulation, Classes/Objects)
- `java.io.Serializable` + `ObjectOutputStream` / `ObjectInputStream` for persistence
- `java.security.MessageDigest` (SHA-256) for PIN hashing
- No database — this project intentionally uses file-based serialization instead of JDBC

## Project Structure

```
Main.java          # console menu (entry point)
Bank.java          # manages the account list: create, login, list all, save/load
BankAccount.java    # individual account: balance, PIN hash, transaction log
bank_data.dat       # serialized account data (generated at runtime)
```

## How It Works

- **`Bank`** holds the in-memory list of accounts and is responsible for persistence — `saveData()` serializes the full account list to `bank_data.dat` after every change, and `loadData()` deserializes it back on startup.
- **`BankAccount`** implements `Serializable` so it can be written directly to the file. The constructor hashes the PIN immediately on account creation — the raw PIN is never held in memory longer than necessary and never written to disk.
- **PIN validation** re-hashes whatever PIN is entered at login and compares it against the stored hash (`validatePin()`), rather than decrypting anything — this is a one-way hash, by design.

## Setup & Run

1. **Compile:**

   ```bash
   javac *.java
   ```

2. **Run:**

   ```bash
   java Main
   ```

   On first run, `bank_data.dat` won't exist yet — the app starts with an empty account list and creates the file automatically on the first save.

## Design Notes

- **Why SHA-256 over plain text:** Storing raw PINs in a serialized file would mean anyone with file access could read every customer's PIN directly. Hashing means even direct access to `bank_data.dat` doesn't expose the original PINs.
- **Why serialization instead of a database:** This project was built to focus specifically on core Java fundamentals — OOP design, file I/O, and exception handling — without the added complexity of a DB layer. For a version of this same domain backed by a real relational database with transactions, see [InventoryBillingSystem](https://github.com/Vinnu9989/InventoryBillingSystem), which uses JDBC + MySQL instead.
- **Limitation:** Serialized `.dat` files aren't human-readable or queryable, and they're tightly coupled to the Java class structure — renaming a field in `BankAccount` would break loading of old data. This tradeoff is acceptable for a learning project but wouldn't hold up in production.
