import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class BoxPlotter extends ApplicationFrame { // the MyBoxPlot library will extend the ApplicationFrame class form JFreeChart library

    public BoxPlotter() {

        super("Box Plot");

        final BoxAndWhiskerCategoryDataset dataset = createSampleDataset();

        final CategoryAxis xAxis = new CategoryAxis("Group");
        final NumberAxis yAxis = new NumberAxis("Time (Seconds)");
        yAxis.setAutoRangeIncludesZero(false);
        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(false);
        // renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        final JFreeChart chart = new JFreeChart(
                "Box Plot",
                new Font("SansSerif", Font.BOLD, 14),
                plot,
                true
        );
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(450, 270));
        setContentPane(chartPanel);

    }

    private BoxAndWhiskerCategoryDataset createSampleDataset() {

        final int seriesCount = 1;
        final int categoryCount = 2;
        final int entityCount = 10;

        final DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        List<Double> ListA = new ArrayList<>(Arrays.asList(12.0, 9.0, 11.0, 17.0, 21.0, 16.0, 23.0, 14.0, 15.0, 24.0));
        List<Double> ListB = new ArrayList<>(Arrays.asList(5.0, 8.0, 11.0, 9.0, 12.0, 6.0, 8.0, 7.0, 13.0, 11.0));

        dataset.add(ListA, "Series " + 1, " Group A");
        dataset.add(ListB, "Series " + 1, " Group B");

        return dataset;
    }

    public static void main(final String[] args) {
        final BoxPlotter demo = new BoxPlotter();
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}