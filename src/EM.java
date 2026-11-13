import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EM extends JFrame {
    JTextField title = new JTextField();
    JTextField amount = new JTextField();
    JComboBox<String> type = new JComboBox<>(new String[]{"EXPENSE", "INCOME"});
    JComboBox<String> category = new JComboBox<>();

    public EM() {
        setTitle("Transactions");
        setSize(400, 360);
        setLocationRelativeTo(null);

        for (String c : DataStorage.categories(Login.currentUserId))
            category.addItem(c);

        JPanel p = new JPanel(new GridLayout(6, 2, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        p.add(new JLabel("Title")); p.add(title);
        p.add(new JLabel("Amount")); p.add(amount);
        p.add(new JLabel("Type")); p.add(type);
        p.add(new JLabel("Category")); p.add(category);

        JButton add = new JButton("Add Transaction");
        JButton delete = new JButton("Delete Transaction");
        p.add(add); p.add(delete);
        add(p);

        add.addActionListener(e -> addTransaction());
        delete.addActionListener(e -> deleteTransaction());
    }

    private void addTransaction() {
        try {
            String t = title.getText().trim();
            double a = Double.parseDouble(amount.getText().trim());

            if (t.isEmpty() || a <= 0 || category.getSelectedItem() == null)
                throw new IllegalArgumentException();

            DataStorage.addTransaction(new DataStorage.Transaction(
                0, Login.currentUserId, t,
                category.getSelectedItem().toString(),
                type.getSelectedItem().toString(),
                a, LocalDate.now().toString()
            ));

            JOptionPane.showMessageDialog(this, "Transaction saved.");
            title.setText("");
            amount.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter valid transaction details.");
        }
    }

    private void deleteTransaction() {
        String value = JOptionPane.showInputDialog(this, "Enter transaction ID:");
        if (value == null) return;

        try {
            DataStorage.deleteTransaction(
                Integer.parseInt(value), Login.currentUserId
            );
            JOptionPane.showMessageDialog(this, "Transaction deleted if it existed.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID.");
        }
    }
}
