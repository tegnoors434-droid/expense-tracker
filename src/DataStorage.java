import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage {
    private static final String FILE = "data/expense-data.dat";
    private static Store store = load();

    public static class User implements Serializable {
        public int id;
        public String name, email, passwordHash;

        public User(int id, String name, String email, String passwordHash) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.passwordHash = passwordHash;
        }
    }

    public static class Transaction implements Serializable {
        public int id, userId;
        public String title, category, type, date;
        public double amount;

        public Transaction(int id, int userId, String title, String category,
                           String type, double amount, String date) {
            this.id = id;
            this.userId = userId;
            this.title = title;
            this.category = category;
            this.type = type;
            this.amount = amount;
            this.date = date;
        }
    }

    public static class Budget implements Serializable {
        public int userId;
        public String category, month;
        public double amount;

        public Budget(int userId, String category, String month, double amount) {
            this.userId = userId;
            this.category = category;
            this.month = month;
            this.amount = amount;
        }
    }

    private static class Store implements Serializable {
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Transaction> transactions = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<Budget> budgets = new ArrayList<>();
        int nextUserId = 1, nextTransactionId = 1;
    }

    private static Store load() {
        File file = new File(FILE);
        if (!file.exists()) return new Store();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Store) in.readObject();
        } catch (Exception e) {
            System.err.println("Could not load saved data: " + e.getMessage());
            return new Store();
        }
    }

    public static synchronized void save() {
        try {
            File file = new File(FILE);
            File parent = file.getParentFile();
            if (parent != null) parent.mkdirs();

            try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(store);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not save data: " + e.getMessage(), e);
        }
    }

    public static synchronized User register(String name, String email, String hash) {
        for (User u : store.users)
            if (u.email.equalsIgnoreCase(email)) return null;

        User user = new User(store.nextUserId++, name, email, hash);
        store.users.add(user);

        store.categories.add(user.id + "|Food");
        store.categories.add(user.id + "|Travel");
        store.categories.add(user.id + "|Shopping");
        store.categories.add(user.id + "|Bills");
        store.categories.add(user.id + "|Salary");
        save();
        return user;
    }

    public static synchronized User login(String email) {
        for (User u : store.users)
            if (u.email.equalsIgnoreCase(email)) return u;
        return null;
    }

    public static synchronized ArrayList<Transaction> transactions(int userId) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction t : store.transactions)
            if (t.userId == userId) result.add(t);
        return result;
    }

    public static synchronized void addTransaction(Transaction t) {
        t.id = store.nextTransactionId++;
        store.transactions.add(t);
        save();
    }

    public static synchronized void deleteTransaction(int id, int userId) {
        store.transactions.removeIf(t -> t.id == id && t.userId == userId);
        save();
    }

    public static synchronized ArrayList<String> categories(int userId) {
        ArrayList<String> result = new ArrayList<>();
        for (String c : store.categories) {
            String[] parts = c.split("\\|", 2);
            if (parts.length == 2 && Integer.parseInt(parts[0]) == userId)
                result.add(parts[1]);
        }
        return result;
    }

    public static synchronized void addCategory(int userId, String name) {
        String entry = userId + "|" + name;
        if (!store.categories.contains(entry)) {
            store.categories.add(entry);
            save();
        }
    }

    public static synchronized ArrayList<Budget> budgets(int userId) {
        ArrayList<Budget> result = new ArrayList<>();
        for (Budget b : store.budgets)
            if (b.userId == userId) result.add(b);
        return result;
    }

    public static synchronized void setBudget(Budget budget) {
        store.budgets.removeIf(b -> b.userId == budget.userId
                && b.category.equals(budget.category)
                && b.month.equals(budget.month));
        store.budgets.add(budget);
        save();
    }
}
