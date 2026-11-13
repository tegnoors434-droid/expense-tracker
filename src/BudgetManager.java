import java.awt.GridLayout;
import java.time.YearMonth;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BudgetManager extends JFrame {
    JComboBox<String> category = new JComboBox<>();
    JTextField amount = new JTextField();

    public BudgetManager() {
        setTitle("Budget Manager");
        setSize(380, 250);
        setLocationRelativeTo(null);

        for (String c : DataStorage.categories(Login.currentUserId))
            category.addItem(c);

        JPanel p = new JPanel(new GridLayout(4, 2, 10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        p.add(new JLabel("Category"));
        p.add(category);
        p.add(new JLabel("Monthly Budget"));
        p.add(amount);

        JButton save = new JButton("Save Budget");
        p.add(new JLabel());
        p.add(save);
        add(p);

        save.addActionListener(e -> saveBudget());
    }

    private void saveBudget() {
        try {
            double value = Double.parseDouble(amount.getText().trim());
            if (value <= 0 || category.getSelectedItem() == null)
                throw new IllegalArgumentException();

            DataStorage.setBudget(new DataStorage.Budget(
                Login.currentUserId,
                category.getSelectedItem().toString(),
                YearMonth.now().toString(),
                value
            ));

            JOptionPane.showMessageDialog(this, "Budget saved.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter a valid budget.");
        }
    }
}
