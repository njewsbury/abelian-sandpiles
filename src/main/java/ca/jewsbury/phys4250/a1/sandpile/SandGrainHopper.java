package ca.jewsbury.phys4250.a1.sandpile;

import ca.jewsbury.phys4250.a1.results.SandpileResultsContainer;
import java.util.concurrent.locks.Lock;

/**
 * SandGrainHopper.class
 *
 *
 *
 * 13-Jan-2015
 *
 * @author Nathan
 */
public class SandGrainHopper implements Runnable {

    private final Sandpile focus;
    private final Sandbox sandbox;
    private final SandpileResultsContainer container;

    public SandGrainHopper(Sandbox sandbox, Sandpile focus, SandpileResultsContainer container) {
        this.focus = focus;
        this.sandbox = sandbox;
        this.container = container;
    }

    @Override
    public void run() {
        placeSandGrain(focus);
    }

    private void placeSandGrain(Sandpile pile) {
        Sandpile[] adjacent;
        Lock pileLock;
        
        if (sandbox != null && pile != null) {
            if (sandbox.canPlaceGrain(pile)) {
                pile.increaseHeight();
            } else {
                //avalance
                adjacent = sandbox.getAdjacentPiles(pile);
                container.increaseAvalancheSize();
                sandbox.resetSandpileHeight(pile);
            
                if( adjacent != null ) {
                    for( Sandpile single : adjacent ) {
                        if(single != null ) {
                            placeSandGrain(single);
                        }
                    }
                }                
            }
        }
    }
}
