package ca.jewsbury.phys4250.a1.results;

import ca.jewsbury.phys4250.a1.sandpile.Sandbox;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

/**
 * SandpileResultsContainer.class
 *
 * Container class for a series of results.
 *
 * 13-Jan-2015
 *
 * @author Nathan
 */
public class SandpileResultsContainer {

    //Publicly accessible format objects
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MMMdd-ha");
    public static final String FILE_NAME_TEMPLATE = "%d_%s_%dx%d_%s-%d.png";
    public static final String SEPARATOR = "\t";

    //Simulation parameters and properties to display.
    //constant properties.
    private final int simulationId;
    private final int simulationGridSize;
    //variable results
    private long simulationStartTime, simulationEndTime;
    private long simulationStableSteps;
    private double[] regressionValues;
    private long transientStepCount;
    private int[][] finalHeightMap;
    private long maxAvalancheSize;
    private long transientTimeEnd;
    private long totalStepCount;
    private long avalancheSize;
    private String modelType;
    private String cpuType;
    //list results
    private List<long[]> avalancheResults;
    private List<long[]> stepTiming;

    public SandpileResultsContainer(int simulationId, int gridSize) {
        this.simulationId = simulationId;
        this.simulationGridSize = gridSize;
        //initialization
        avalancheResults = new ArrayList<long[]>();
        stepTiming = new ArrayList<long[]>();
        transientTimeEnd = 0;
        avalancheSize = 0;
    }

    /**
     * Mark that the sandbox model has completed it's transient period, and has
     * started stable state behaviour - reset all recorded data up to this
     * point.
     *
     * @param model - The current in use sandbox model.
     */
    public void markTransientTime(Sandbox model) {
        if (this.transientTimeEnd == 0) {
            model.setStable();
            avalancheResults = new ArrayList<long[]>();
            stepTiming = new ArrayList<long[]>();

            this.transientTimeEnd = System.nanoTime();
        }
    }

    public void saveAvalancheResults(long stepNumber) {
        long[] resultSet;

        if (avalancheSize > 0) {
            resultSet = new long[2];

            resultSet[0] = stepNumber;
            resultSet[1] = getAvalancheSize();
            avalancheResults.add(resultSet);
        }
        avalancheSize = 0;
    }

    public void saveStepTimingResults(long stepNumber, long stepStart, long stepEnd) {
        long[] resultSet = new long[2];
        resultSet[0] = stepNumber;
        resultSet[1] = (stepEnd - stepStart);
        stepTiming.add(resultSet);
    }

    public void printCsvData() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.cpuType).append(SEPARATOR);
        builder.append(this.modelType).append(SEPARATOR);
        builder.append(this.simulationGridSize).append(SEPARATOR);
        builder.append(this.simulationStableSteps).append(SEPARATOR);
        builder.append(this.simulationId).append(SEPARATOR);
        builder.append((this.transientTimeEnd - this.simulationStartTime) / 1000000.0).append(SEPARATOR);
        builder.append(this.transientStepCount).append(SEPARATOR);
        builder.append(this.totalStepCount).append(SEPARATOR);
        builder.append(getTotalNanoTime() / 1000000.0).append(SEPARATOR);
        builder.append(this.maxAvalancheSize).append(SEPARATOR);

        System.out.println(builder);
    }

    /**
     * ACCESSORS & MUTATORS
     */
    public int getSimulationId() {
        return this.simulationId;
    }

    public int getSimulationGridSize() {
        return this.simulationGridSize;
    }

    public void resetIterativeResults() {
        this.avalancheSize = 0;
    }

    public long getSimulationStartTime() {
        return simulationStartTime;
    }

    public void setSimulationStartTime(long simulationStartTime) {
        this.simulationStartTime = simulationStartTime;
    }

    public long getSimulationEndTime() {
        return simulationEndTime;
    }

    public void increaseAvalancheSize() {
        avalancheSize++;
    }

    public long getAvalancheSize() {
        return avalancheSize;
    }

    public long getTotalStepCount() {
        return totalStepCount;
    }

    public void setTotalStepCount(long totalStepCount) {
        this.totalStepCount = totalStepCount;
    }

    public void setSimulationEndTime(long simulationEndTime) {
        this.simulationEndTime = simulationEndTime;
    }

    public long getTransientTimeEnd() {
        return transientTimeEnd;
    }

    public void setTransientTimeEnd(long transientTimeEnd) {
        this.transientTimeEnd = transientTimeEnd;
    }

    public long getTransientStepCount() {
        return transientStepCount;
    }

    public void setTransientStepCount(long transientStepCount) {
        this.transientStepCount = transientStepCount;
    }

    public int[][] getFinalHeightMap() {
        return finalHeightMap;
    }

    public void setFinalHeightMap(int[][] finalHeightMap) {
        this.finalHeightMap = finalHeightMap;
    }

    public long getTotalNanoTime() {
        return (this.simulationEndTime - this.simulationStartTime);
    }

    public List<long[]> getAvalancheResults() {
        return avalancheResults;
    }

    public List<long[]> getStepTiming() {
        return stepTiming;
    }

    public long getMaxAvalancheSize() {
        return maxAvalancheSize;
    }

    public void setMaxAvalancheSize(long maxAvalancheSize) {
        this.maxAvalancheSize = maxAvalancheSize;
    }

    public double[] getRegressionValues() {
        return regressionValues;
    }

    public long getSimulationStableSteps() {
        return simulationStableSteps;
    }

    public void setSimulationStableSteps(long simulationStableSteps) {
        this.simulationStableSteps = simulationStableSteps;
    }

    public void setRegressionValues(double[] regressionValues) {
        this.regressionValues = regressionValues;
    }

    public String getCpuType() {
        return cpuType;
    }

    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

}
