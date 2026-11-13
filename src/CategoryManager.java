import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class CategoryManager extends JFrame {
    public CategoryManager() {
        setTitle("Category Manager");
        setSize(350, 300);
        setLocationRelativeTo(null);

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String c : DataStorage.categories(Login.currentUserId))
            model.addElement(c);

        JList<String> list = new JList<>(model);
        JTextField category = new JTextField();
        JButton add = new JButton("Add Category");

        add.addActionListener(e -> {
            String c = category.getText().trim();
            if (!c.isEmpty()) {
                DataStorage.addCategory(Login.currentUserId, c);
                model.clear();
                for (String item : DataStorage.categories(Login.currentUserId))
                    model.addElement(item);
                category.setText("");
            }
        });

        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        bottom.add(category, BorderLayout.CENTER);
        bottom.add(add, BorderLayout.SOUTH);

        add(new JScrollPane(list), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
}
