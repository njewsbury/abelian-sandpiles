package ca.jewsbury.phys4250.a1.simulation;

import ca.jewsbury.phys4250.a1.results.SandpileResultsContainer;
import ca.jewsbury.phys4250.a1.results.SandpileResultsDisplayer;
import ca.jewsbury.phys4250.a1.sandpile.SandGrainHopper;
import ca.jewsbury.phys4250.a1.sandpile.Sandbox;
import ca.jewsbury.phys4250.a1.sandpile.Sandpile;

/**
 * SandpileSimulation.class
 *
 *
 *
 * 13-Jan-2015
 *
 * @author Nathan
 */
public class SandpileSimulation {

    /**
     * Steps to take after reaching max width
     */
    private final int simulationSteps;
    /**
     * N x N grid size for the sandbox.
     */
    private final int simulationGridSize;

    private final int printEveryNSteps;
    
    private SandpileResultsContainer simulationResults;
    
    

    public SandpileSimulation(int steps, int size, int printEvery) {
        this.simulationSteps = steps;
        this.simulationGridSize = size;
        this.printEveryNSteps = printEvery;
    }

    private boolean validateSimulationParameters() {
        boolean validSimulation = false;
        if (simulationGridSize > 0 && simulationGridSize % 2 == 1) {
            if (simulationSteps > 0) {
                validSimulation = true;
            } else {
                System.err.println("Invalid simulation step count.");
            }
        } else {
            System.err.println("Invalid simulation grid size.");
        }
        return validSimulation;
    }

    private void initializeSimulationResultsContainer(int id, Sandbox model) {
        simulationResults = new SandpileResultsContainer(id, simulationGridSize);
        simulationResults.setModelType(model.getSandboxModelName());
        simulationResults.setSimulationStableSteps(this.simulationSteps);
    }

    public SandpileResultsContainer runSimulation(Sandbox sandboxModel, int id) {
        SandpileResultsDisplayer display;
        boolean continueSimulation = true;
        SandGrainHopper[] adjacent;
        Sandpile centralPile;
        long stableSteps = 0;
        long stepStartTime;
        long totalSteps = 0;

        if (validateSimulationParameters()) {
            if (sandboxModel != null) {
                initializeSimulationResultsContainer(id, sandboxModel);
                display = new SandpileResultsDisplayer(sandboxModel, simulationResults);
                sandboxModel.initialize(simulationGridSize);
                centralPile = sandboxModel.getCentralPile();
                adjacent = getAdjacentHoppers(sandboxModel, centralPile); //cache for faster access

                simulationResults.setSimulationStartTime(System.nanoTime());
                while (continueSimulation) {

                    simulationResults.resetIterativeResults();
                    stepStartTime = System.nanoTime();
                    placeCentralSandGrain(sandboxModel, centralPile, adjacent);

                    if (!sandboxModel.isStable()) {
                        //TEST STABLE
                        if (sandboxModel.getSandPile(0, 0).getTouchCount() > 0) {
                            //sandbox has reached corners t.f stable state reached
                            simulationResults.markTransientTime(sandboxModel);
                            simulationResults.setTransientStepCount(totalSteps);
                        }
                    } else {
                        stableSteps++;
                        simulationResults.saveStepTimingResults(stableSteps, stepStartTime, System.nanoTime());

                    }
                    totalSteps++;

                    // STORE ITERATIVE RESULT.
                    simulationResults.saveAvalancheResults(totalSteps);
                    if (totalSteps > 0 && (totalSteps) % printEveryNSteps == 0) {
                        display.generateAndSaveImage(totalSteps, sandboxModel.getHeightMap());
                    }
                    // TEST LOOP CONDITION
                    if (stableSteps >= simulationSteps) {
                        continueSimulation = false;
                        simulationResults.setFinalHeightMap(sandboxModel.getHeightMap());
                        simulationResults.setTotalStepCount(totalSteps);
                        simulationResults.setSimulationEndTime(System.nanoTime());
                        display.generateAndSaveImage(totalSteps, simulationResults.getFinalHeightMap());
                    }
                }
            }
        }

        return simulationResults;
    }

    private void placeCentralSandGrain(Sandbox model, Sandpile central, SandGrainHopper[] adj) {
        int difference;

        if (central != null && adj != null) {
            if (model.canPlaceGrain(central)) {
                difference = model.getHeightDifference(central);
                central.increaseHeight(difference);
            } else {
                // avalanche
                simulationResults.increaseAvalancheSize();
                model.resetSandpileHeight(central);

                for (SandGrainHopper hopper : adj) {
                    hopper.run();
                }
            }
        }
    }

    private SandGrainHopper[] getAdjacentHoppers(Sandbox sandboxModel, Sandpile central) {
        SandGrainHopper[] hopperList = null;
        Sandpile[] adjacent;

        if (central != null && sandboxModel != null) {
            adjacent = sandboxModel.getAdjacentPiles(central);
            if (adjacent != null) {
                hopperList = new SandGrainHopper[adjacent.length];

                for (int i = 0; i < adjacent.length; i++) {
                    hopperList[i] = new SandGrainHopper(sandboxModel, adjacent[i], simulationResults);
                }
            }
        }

        return hopperList;
    }
}
