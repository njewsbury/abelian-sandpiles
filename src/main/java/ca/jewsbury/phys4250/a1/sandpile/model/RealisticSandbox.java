package ca.jewsbury.phys4250.a1.sandpile.model;

import ca.jewsbury.phys4250.a1.sandpile.Sandbox;
import ca.jewsbury.phys4250.a1.sandpile.Sandpile;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * RealisticModel.class
 *
 *
 *
 * 17-Jan-2015
 *
 * @author Nathan
 */
public class RealisticSandbox extends Sandbox {

    public static final String SANDBOX_MODEL_NAME = "REALISTIC";

    private final int MAX_DIFFERENCE = 5;
    private final int MAXIMUM_ADJACENT = 4;

    private final int BASE_RGB = 0xADFFFF;
    private final int SHADE_MOD = 0x01FEFE;
    private List<Color> colourScheme;

    public RealisticSandbox() {
        int currentShade = BASE_RGB;
        colourScheme = new ArrayList<Color>();

        while (currentShade > 0) {
            colourScheme.add(new Color(currentShade));
            currentShade -= SHADE_MOD;
        }
        
        BufferedImage img = new BufferedImage(20, (20*colourScheme.size()), BufferedImage.TYPE_INT_RGB);
        Graphics2D draw = img.createGraphics();
        
        for( int i = 0; i < colourScheme.size(); i++ ) {
            draw.setColor(colourScheme.get(i));
            draw.fillRect(0, i*20, 20, 20);
        }
        draw.dispose();
        try {
            ImageIO.write(img, "png", new File("ColourScheme.png"));
        } catch(Exception e ) {
            //
        }
    }

    @Override
    public boolean canPlaceGrain(Sandpile pile) {
        boolean placeGrain = true;
        Sandpile[] smallPiles;
        int pileHeight = pile.getHeight();

        if (pileHeight >= (MAX_DIFFERENCE-1)) {
            smallPiles = getAdjacentPiles(pile);

            if (smallPiles != null) {
                for (Sandpile adjacent : smallPiles) {
                    if (adjacent != null && (pileHeight-adjacent.getHeight()) >= (MAX_DIFFERENCE-1)) {
                        placeGrain = false;
                    }
                }
            }
        }

        return placeGrain;
    }

    @Override
    public Sandpile[] getAdjacentPiles(Sandpile pile) {
        Sandpile[] adjacent = new Sandpile[MAXIMUM_ADJACENT];
        int px, py, pHeight;

        if (pile != null) {
            px = pile.getPileX();
            py = pile.getPileY();
            pHeight = pile.getHeight();

            adjacent[0] = getSandPile(px + 1, py);
            if (adjacent[0] != null && (adjacent[0].getHeight() - pHeight) > 0) {
                adjacent[0] = null;
            }
            adjacent[1] = getSandPile(px - 1, py);
            if (adjacent[1] != null && (adjacent[1].getHeight() - pHeight) > 0) {
                adjacent[1] = null;
            }
            adjacent[2] = getSandPile(px, py + 1);
            if (adjacent[2] != null && (adjacent[2].getHeight() - pHeight) > 0) {
                adjacent[2] = null;
            }
            adjacent[3] = getSandPile(px, py - 1);
            if (adjacent[3] != null && (adjacent[3].getHeight() - pHeight) > 0) {
                adjacent[3] = null;
            }

        }
        return adjacent;
    }

    @Override
    public int getHeightDifference(Sandpile pile) {
        return 1; // return 1 to only place a single grain every turn.
    }

    @Override
    public String getSandboxModelName() {
        return SANDBOX_MODEL_NAME;
    }

    @Override
    public void resetSandpileHeight(Sandpile pile) {
        Sandpile[] adjacent;
        int numAdjacent = 0;

        if (pile != null) {
            adjacent = getAdjacentPiles(pile);
            for (Sandpile adj : adjacent) {
                if (adj != null) {
                    numAdjacent++;
                }
            }
            pile.setHeight(pile.getHeight() - (numAdjacent - 1));
        }
    }

    @Override
    public Color getColourByHeight(int height) {
        int colourIndex = (height % colourScheme.size());
        return colourScheme.get(colourIndex);
    }

}
