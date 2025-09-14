package com.example;

public class DigitalWallet {
    private final String owner;
    private double balance;
    private boolean verified;
    private boolean locked;

    public DigitalWallet(String owner, double initialBalance) {
        if (initialBalance < 0) throw new IllegalArgumentException("Saldo inicial negativo");
        this.owner = owner;
        this.balance = initialBalance;
        this.verified = false;
        this.locked = false;
    }

    public String getOwner() { return owner; }
    public double getBalance() { return balance; }

    public boolean isVerified() { return verified; }
    public boolean isLocked() { return locked; }

    public void verify() { this.verified = true; }
    public void lock() { this.locked = true; }
    public void unlock() { this.locked = false; }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Depósito deve ser > 0");
        balance += amount;
    }

    /** Requer verificada e não bloqueada; amount > 0; debita se houver saldo e retorna true; senão false. */
    public boolean pay(double amount) {
        ensureActiveAndVerified();
        if (amount <= 0) throw new IllegalArgumentException("Pagamento deve ser > 0");
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    /** Requer verificada e não bloqueada; amount > 0; sempre credita. */
    public void refund(double amount) {
        ensureActiveAndVerified();
        if (amount <= 0) throw new IllegalArgumentException("Estorno deve ser > 0");
        balance += amount;
    }

    private void ensureActiveAndVerified() {
        if (!verified) throw new IllegalStateException("Carteira não verificada");
        if (locked) throw new IllegalStateException("Carteira bloqueada");
    }
}
