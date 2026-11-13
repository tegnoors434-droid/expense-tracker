import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class Analytics extends JFrame {
    public Analytics() {
        setTitle("Expense Analytics");
        setSize(650, 500);
        setLocationRelativeTo(null);

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        for (DataStorage.Transaction t :
                DataStorage.transactions(Login.currentUserId)) {
            if (t.type.equals("EXPENSE")) {
                double old = dataset.getValue(t.category) == null
                    ? 0 : dataset.getValue(t.category).doubleValue();
                dataset.setValue(t.category, old + t.amount);
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
            "Expenses by Category", dataset, true, true, false
        );

        add(new ChartPanel(chart), BorderLayout.CENTER);
    }
}
