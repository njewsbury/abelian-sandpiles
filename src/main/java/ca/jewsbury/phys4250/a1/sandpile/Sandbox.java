package ca.jewsbury.phys4250.a1.sandpile;

import java.awt.Color;

/**
 * Sandbox.class
 *
 *
 *
 * 13-Jan-2015
 *
 * @author Nathan
 */
public abstract class Sandbox {

    protected int sandboxSize;
    protected int centralCoord;
    protected boolean stableState;
    protected Sandpile[][] sandbox;

    public void initialize(int cubeSize) {
        this.sandboxSize = cubeSize;
        this.centralCoord = (this.sandboxSize - 1) / 2;
        stableState = false;
        generateSandbox();
    }

    private void generateSandbox() {
        Sandpile generate;
        this.sandbox = new Sandpile[this.sandboxSize][this.sandboxSize];

        for (int y = 0; y < this.sandboxSize; y++) {
            for (int x = 0; x < this.sandboxSize; x++) {
                generate = new Sandpile(x, y);
                this.sandbox[y][x] = generate;
            }
        }
    }

    public Sandpile getCentralPile() {
        return sandbox[centralCoord][centralCoord];
    }

    public Sandpile getSandPile(int x, int y) {
        Sandpile pile = null;
        if (x >= 0 && x < sandboxSize) {
            if (y >= 0 && y < sandboxSize) {
                pile = this.sandbox[y][x];
            }
        }
        return pile;
    }

    public boolean isStable() {
        return stableState;
    }

    public void setStable() {
        stableState = true;
    }

    public abstract boolean canPlaceGrain(Sandpile pile);

    public abstract Sandpile[] getAdjacentPiles(Sandpile pile);

    /**
     * Returns the difference between the piles current height, and the
     * maximum allowable height.  Used for skipping 'useless' steps of just
     * placing a single grain.
     * @param pile
     * @return 
     */
    public abstract int getHeightDifference(Sandpile pile);
    
    public abstract String getSandboxModelName();
    
    public abstract void resetSandpileHeight(Sandpile pile);
    
    public abstract Color getColourByHeight(int height);

    public int[][] getHeightMap() {
        int[][] heightMap = new int[sandboxSize][sandboxSize];

        for (int y = 0; y < sandboxSize; y++) {
            for (int x = 0; x < sandboxSize; x++) {
                heightMap[y][x] = sandbox[y][x].getHeight();
            }
        }
        return heightMap;
    }


}
