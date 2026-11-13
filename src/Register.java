import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Register extends JFrame {
    JTextField name = new JTextField();
    JTextField email = new JTextField();
    JPasswordField password = new JPasswordField();
    JPasswordField confirm = new JPasswordField();

    public Register() {
        setTitle("Create Account");
        setSize(380, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(9, 1, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        p.add(new JLabel("Full Name"));
        p.add(name);
        p.add(new JLabel("Email"));
        p.add(email);
        p.add(new JLabel("Password"));
        p.add(password);
        p.add(new JLabel("Confirm Password"));
        p.add(confirm);

        JButton register = new JButton("Register");
        p.add(register);
        add(p);

        register.addActionListener(e -> registerUser());
    }

    private void registerUser() {
        String n = name.getText().trim();
        String em = email.getText().trim();
        String pw = new String(password.getPassword());
        String cp = new String(confirm.getPassword());

        if (n.isEmpty() || em.isEmpty() || pw.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields.");
            return;
        }

        if (!em.matches("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Enter a valid email.");
            return;
        }

        if (!PasswordUtil.isStrongPassword(pw)) {
            JOptionPane.showMessageDialog(this,
                "Password needs 8 characters, uppercase, lowercase and a number.");
            return;
        }

        if (!pw.equals(cp)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        try {
            DataStorage.User user = DataStorage.register(
                n, em, PasswordUtil.hashPassword(pw)
            );

            if (user == null) {
                JOptionPane.showMessageDialog(this, "Email already registered.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Registration successful!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
