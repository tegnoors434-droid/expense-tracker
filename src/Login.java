import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame {
    public static int currentUserId;
    public static String currentUserName, currentUserEmail;

    JTextField email = new JTextField();
    JPasswordField password = new JPasswordField();

    public Login() {
        setTitle("Expense Tracker - Login");
        setSize(380, 330);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(7, 1, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        p.add(new JLabel("Email"));
        p.add(email);
        p.add(new JLabel("Password"));
        p.add(password);

        JButton login = new JButton("Login");
        JButton register = new JButton("Create Account");
        p.add(login);
        p.add(register);
        add(p);

        login.addActionListener(e -> loginUser());
        register.addActionListener(e -> new Register().setVisible(true));
    }

    private void loginUser() {
        DataStorage.User user = DataStorage.login(email.getText().trim());
        String pw = new String(password.getPassword());

        if (user == null || !PasswordUtil.checkPassword(pw, user.passwordHash)) {
            JOptionPane.showMessageDialog(this, "Invalid email or password.");
            return;
        }

        currentUserId = user.id;
        currentUserName = user.name;
        currentUserEmail = user.email;

        dispose();
        new dashboard().setVisible(true);
    }
}
