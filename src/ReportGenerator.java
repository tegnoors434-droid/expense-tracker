import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ReportGenerator extends JFrame {
    public ReportGenerator() {
        setTitle("Expense Reports");
        setSize(350, 180);
        setLocationRelativeTo(null);

        JButton generate = new JButton("Generate CSV Report");
        add(generate);
        generate.addActionListener(e -> generateReport());
    }

    private void generateReport() {
        try (FileWriter writer = new FileWriter("expense-report.csv")) {
            writer.write("ID,Title,Category,Type,Amount,Date\n");

            for (DataStorage.Transaction t :
                    DataStorage.transactions(Login.currentUserId)) {
                writer.write(t.id + "," + csv(t.title) + ","
                    + csv(t.category) + "," + t.type + ","
                    + t.amount + "," + t.date + "\n");
            }

            JOptionPane.showMessageDialog(this,
                "Report generated: expense-report.csv");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
