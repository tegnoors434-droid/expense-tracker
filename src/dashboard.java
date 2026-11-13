import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class dashboard extends JFrame {
    JLabel balance = new JLabel("₹0.00");
    JLabel income = new JLabel("₹0.00");
    JLabel expenses = new JLabel("₹0.00");
    DefaultTableModel model = new DefaultTableModel(
        new String[]{"ID", "Title", "Category", "Type", "Amount", "Date"}, 0
    );

    public dashboard() {
        setTitle("Smart Expense Tracker - " + Login.currentUserName);
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel sidebar = new JPanel(new GridLayout(8, 1, 5, 5));
        sidebar.setPreferredSize(new Dimension(190, 0));
        sidebar.setBackground(new Color(30, 41, 59));

        String[] options = {
            "Dashboard", "Transactions", "Categories",
            "Budgets", "Analytics", "Reports", "Refresh", "Logout"
        };

        for (String option : options) {
            JButton button = new JButton(option);
            sidebar.add(button);
            button.addActionListener(e -> openModule(option));
        }

        JPanel cards = new JPanel(new GridLayout(1, 3, 12, 12));
        cards.add(createCard("Total Balance", balance));
        cards.add(createCard("Total Income", income));
        cards.add(createCard("Total Expenses", expenses));

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        center.add(cards, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);

        refresh();
    }

    private JPanel createCard(String heading, JLabel value) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(heading));
        value.setFont(new Font("Arial", Font.BOLD, 22));
        value.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(value);
        return panel;
    }

    private void refresh() {
        model.setRowCount(0);
        double totalIncome = 0, totalExpenses = 0;

        ArrayList<DataStorage.Transaction> transactions =
            DataStorage.transactions(Login.currentUserId);

        for (DataStorage.Transaction t : transactions) {
            if (t.type.equals("INCOME")) totalIncome += t.amount;
            else totalExpenses += t.amount;

            model.addRow(new Object[]{
                t.id, t.title, t.category, t.type, t.amount, t.date
            });
        }

        income.setText(String.format("₹%.2f", totalIncome));
        expenses.setText(String.format("₹%.2f", totalExpenses));
        balance.setText(String.format("₹%.2f",
            totalIncome - totalExpenses));
    }

    private void openModule(String option) {
        switch (option) {
            case "Dashboard":
            case "Refresh":
                refresh();
                break;
            case "Transactions":
                new EM().setVisible(true);
                break;
            case "Categories":
                new CategoryManager().setVisible(true);
                break;
            case "Budgets":
                new BudgetManager().setVisible(true);
                break;
            case "Analytics":
                new Analytics().setVisible(true);
                break;
            case "Reports":
                new ReportGenerator().setVisible(true);
                break;
            case "Logout":
                Login.currentUserId = 0;
                Login.currentUserName = null;
                Login.currentUserEmail = null;
                dispose();
                new Login().setVisible(true);
                break;
        }
    }
}
