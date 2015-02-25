package ca.jewsbury.phys4250.a1.root;

import ca.jewsbury.phys4250.a1.results.SandpileResultsContainer;
import ca.jewsbury.phys4250.a1.results.SandpileResultsPlotter;
import ca.jewsbury.phys4250.a1.sandpile.Sandbox;
import ca.jewsbury.phys4250.a1.sandpile.model.AbelianSandbox;
import ca.jewsbury.phys4250.a1.sandpile.model.RealisticSandbox;
import ca.jewsbury.phys4250.a1.simulation.SandpileSimulation;
import org.apache.commons.lang3.StringUtils;

/**
 * Root.class
 *
 * 
 * 13-Jan-2015
 *
 * @author Nathan
 */
public class Root {

    private static final String DEFAULT_KEYWORD = "DEFAULT";
    private static final int DEFAULT_GRID_SIZE = 101;
    private static final int DEFAULT_STEP_COUNT = 10000;
    private static final int DEFAULT_IMG_SAVE = 1000;
    private static final String ERR_STRING = "Invalid arguments passed, valid arguments are:\n"
            + "[int trial id] [string CPU ID] [int grid size] [int step total] [string sandpile model] [int img every n steps]\n"
            + "Sandpile model can be chosen by using either 'ABELIAN' or 'REALISTIC'\n"
            + "You may also run with argument 'DEFAULT' to run a Abelian model 10,000 steps on a 101x101 grid.";

    private static SandpileSimulation simulation;

    private static String cpuIdentifier;
    private static int simGridSize;
    private static int simStepCount;
    private static int simTrialId;
    private static int simPrintEvery;
    private static Sandbox sandpileModel;

    public static void main(String[] args) {
        try {
            parseArguments(args);
            runSingleSimulation();
        } catch (NumberFormatException e) {
            System.out.println(ERR_STRING);
            System.err.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        // prints out a CSV line when complete.
        System.exit(0);
    }

    // map arguments to simulation parameters
    private static void parseArguments(String[] args) throws IllegalArgumentException, NumberFormatException {
        if (args != null) {
            if (args.length == 6) {
                simTrialId = Integer.parseInt(args[0]);
                cpuIdentifier = args[1];
                simGridSize = Integer.parseInt(args[2]);
                simStepCount = Integer.parseInt(args[3]);
                
                if( StringUtils.equalsIgnoreCase(AbelianSandbox.SANDBOX_MODEL_NAME, args[4]) ) {
                    sandpileModel = new AbelianSandbox();
                } else if( StringUtils.equalsIgnoreCase(RealisticSandbox.SANDBOX_MODEL_NAME, args[4])) {
                    sandpileModel = new RealisticSandbox();
                } else {
                    throw new IllegalArgumentException(ERR_STRING);
                }
                simPrintEvery = Integer.parseInt(args[5]);
            } else if (args.length == 1) {
                if (StringUtils.equalsIgnoreCase(DEFAULT_KEYWORD, args[0])) {
                    cpuIdentifier = "DEFAULT";
                    simGridSize = DEFAULT_GRID_SIZE;
                    simStepCount = DEFAULT_STEP_COUNT;
                    sandpileModel = new AbelianSandbox();
                    simPrintEvery = DEFAULT_IMG_SAVE;
                } else {
                    throw new IllegalArgumentException(ERR_STRING);
                }
            } else {
                throw new IllegalArgumentException(ERR_STRING);
            }
        }
    }

    // run the simulation with the given params.
    private static void runSingleSimulation() {
        SandpileResultsPlotter plotter;
        SandpileResultsContainer results;
        
        simulation = new SandpileSimulation(simStepCount, simGridSize, simPrintEvery);
        results = simulation.runSimulation(sandpileModel, simTrialId);
        results.setCpuType(cpuIdentifier);
        
        plotter = new SandpileResultsPlotter(results);
        plotter.generateAndSaveAllCharts();
        
        results.printCsvData();
    }    
}
