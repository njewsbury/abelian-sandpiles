package ca.jewsbury.phys4250.a1.results;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.function.Function2D;
import org.jfree.data.function.PowerFunction2D;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.statistics.Regression;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * SandpileResultsPlotter.class
 *
 * This class handles the output of the graphs after collecting all the result
 * data from the current simulation.
 *
 * Generates "Avalanche Profile" which is a log-log chart of the number of
 * events of a given "avalanche size" (the cells affected during and avalanche
 * event) as well as a "Timing Chart" which shows how much time each step took
 * throughout the simulation.
 *
 * 16-Jan-2015
 *
 * @author Nathan
 */
public class SandpileResultsPlotter {

    private static final String TIMING_FILE_ID = "TIMECHART";
    private static final String PROFILE_FILE_ID = "PROFILE";

    private static final int PLOT_FILE_WIDTH = 2160;
    private static final int PLOT_FILE_HEIGHT = 1400;

    private static final int BIN_SIZE = 1000;

    private final String FILE_DATE_INFO;

    private final SandpileResultsContainer results;

    public SandpileResultsPlotter(SandpileResultsContainer container) {
        this.FILE_DATE_INFO = SandpileResultsContainer.DATE_FORMATTER.format(new Date());
        this.results = container;
    }

    public void generateAndSaveAllCharts() {
        if (this.results != null) {
            // generateTimingChart(); //not as interesting
            generateAvalancheProfileChart();
        } else {
            System.err.println("Unable to save charts, no results given.");
        }
    }

    private void generateAvalancheProfileChart() {
        JFreeChart avalancheProfileChart;
        XYSeriesCollection profileData;
        Map<Long, Long> resultKeys;
        String plotFileName;

        plotFileName = String.format(SandpileResultsContainer.FILE_NAME_TEMPLATE,
                results.getSimulationId(), //Simulation ID
                PROFILE_FILE_ID, //File identifier
                results.getSimulationGridSize(), results.getSimulationGridSize(), //Grid size
                FILE_DATE_INFO, //Date info
                results.getTotalStepCount());

        resultKeys = generateBinMap(results.getAvalancheResults());
        profileData = generateAvalancheProfileData(resultKeys);

        avalancheProfileChart = generateAvalancheProfileChart(profileData);
        saveChartObjectAsFile(avalancheProfileChart, plotFileName);
    }

    //generates the actual JFreeChart object for the avalanche profile.
    private JFreeChart generateAvalancheProfileChart(XYSeriesCollection data) {
        JFreeChart chart = ChartFactory.createScatterPlot("Avalance Plot", "x", "y", data);
        NumberAxis countData = new LogarithmicAxis("Number of Events");
        NumberAxis avalancheSize = new LogarithmicAxis("Avalanche Size");
        XYPlot plot = chart.getXYPlot();
        
        plot.setDomainAxis(avalancheSize);
        plot.setRangeAxis(countData);

        return chart;
    }

    //generates the data for use in the avalanche profile chart.
    private XYSeriesCollection generateAvalancheProfileData(Map<Long, Long> profileData) {
        XYSeries dataSeries = new XYSeries("avalanche-profile");
        Iterator<Long> iterator;
        long key, val;

        iterator = profileData.keySet().iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            val = profileData.get(key);

            if (val > 0) {
                dataSeries.add((int) ((key) * BIN_SIZE), (val));
            }
        }

        return new XYSeriesCollection(dataSeries);
    }

    private Map<Long, Long> generateBinMap(List<long[]> resultSet) {
        Map<Long, Long> resultKeys = new HashMap<Long, Long>();
        long maximumAvalancheSize = 0;
        long bin;

        for (long[] sizeArr : resultSet) {
            if (sizeArr[1] > maximumAvalancheSize) {
                maximumAvalancheSize = sizeArr[1];
            }

            bin = ((long) (sizeArr[1] / (double) BIN_SIZE)) + 1;
            if (resultKeys.containsKey(bin)) {
                resultKeys.put(bin, resultKeys.get(bin) + 1);
            } else {
                resultKeys.put(bin, (long) 1);
            }

        }
        results.setMaxAvalancheSize(maximumAvalancheSize);
        return resultKeys;
    }

    private void generateTimingChart() {
        List<long[]> timeResultSet = results.getStepTiming();
        XYSeries dataSet = new XYSeries("timingGraph");
        XYSeriesCollection collection;
        String plotFileName;
        JFreeChart chart;

        plotFileName = String.format(SandpileResultsContainer.FILE_NAME_TEMPLATE,
                results.getSimulationId(), //Simulation ID
                TIMING_FILE_ID, //File identifier
                results.getSimulationGridSize(), results.getSimulationGridSize(), //Grid size
                FILE_DATE_INFO, //Date info
                results.getTotalStepCount());

        for (long[] timing : timeResultSet) {
            dataSet.add(timing[0], timing[1] / 1000000.0); // Steps = x, Time = y (nanoseconds)
        }

        collection = new XYSeriesCollection(dataSet);
        chart = ChartFactory.createScatterPlot("Timing Chart",
                "Step Number", "Time (ms)", collection);

        saveChartObjectAsFile(chart, plotFileName);
    }

    private void saveChartObjectAsFile(JFreeChart chart, String filename) {
        try {
            ChartUtilities.saveChartAsPNG(new File(filename), chart, PLOT_FILE_WIDTH, PLOT_FILE_HEIGHT);
        } catch (IOException e) {
            System.err.println("Error saving chart: " + e.getMessage());
        }
    }
}
