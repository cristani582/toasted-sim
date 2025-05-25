package GUI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChartsPane extends JPanel {
    public static XYSeries series0;
    public static XYSeriesCollection dataset = new XYSeriesCollection();
    private static int n = 1;

    public ChartsPane() {
        setBackground(Color.white);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Normalized Temperature [θ*] x Normalized Position [x*]",
                "x* [m]",
                "θ*",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        add(new ChartPanel(chart));
    }
    public static class ButtonHandlerChart implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == InputPane.getCalculateButton()) {

                series0 = new XYSeries("Fo" + n);
                n += 1;
                dataset.addSeries(series0);
            }
        }
    }
}
