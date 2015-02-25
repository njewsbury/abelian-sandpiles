package ca.jewsbury.phys4250.a1.sandpile.model;

import ca.jewsbury.phys4250.a1.results.Colours;
import ca.jewsbury.phys4250.a1.sandpile.Sandbox;
import ca.jewsbury.phys4250.a1.sandpile.Sandpile;
import java.awt.Color;

/**
 * AbelianSandbox.class
 *
 *
 *
 * 13-Jan-2015
 *
 * @author Nathan
 */
public class AbelianSandbox extends Sandbox {

    public static final String SANDBOX_MODEL_NAME = "ABELIAN";
    public final int MAXIMUM_HEIGHT = 4;
    public final int MAXIMUM_ADJACENT = 4;

    @Override
    public boolean canPlaceGrain(Sandpile pile) {
        //if sandpile height is less than max-1, it can accept a new grain
        return (pile.getHeight() < (MAXIMUM_HEIGHT - 1));
    }

    @Override
    public Sandpile[] getAdjacentPiles(Sandpile pile) {
        Sandpile[] adjacent = new Sandpile[MAXIMUM_ADJACENT];
        Sandpile temp;
        int currentX, currentY;

        currentX = pile.getPileX();
        currentY = pile.getPileY();
        // NORTH
        if ((temp = super.getSandPile(currentX, currentY - 1)) != null) {
            adjacent[0] = temp;
        }
        // EAST 
        if ((temp = super.getSandPile(currentX + 1, currentY)) != null) {
            adjacent[1] = temp;
        }
        // SOUTH
        if ((temp = super.getSandPile(currentX, currentY + 1)) != null) {
            adjacent[2] = temp;
        }
        // WEST
        if ((temp = super.getSandPile(currentX - 1, currentY)) != null) {
            adjacent[3] = temp;
        }

        return adjacent;
    }

    @Override
    public int getHeightDifference(Sandpile pile) {
        int difference = -1;
        int height;

        if (pile != null) {
            height = pile.getHeight();
            difference = (this.MAXIMUM_HEIGHT - 1) - height;
        }
        return difference;
    }

    @Override
    public String getSandboxModelName() {
        return SANDBOX_MODEL_NAME;
    }

    @Override
    public void resetSandpileHeight(Sandpile pile) {
        if (pile != null) {
            pile.setHeight(0);
        }
    }

    @Override
    public Color getColourByHeight(int height) {
        return Colours.getColor(height);
    }

}
