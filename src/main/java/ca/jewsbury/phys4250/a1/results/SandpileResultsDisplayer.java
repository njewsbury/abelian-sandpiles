package ca.jewsbury.phys4250.a1.results;

import ca.jewsbury.phys4250.a1.sandpile.Sandbox;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;

/**
 * SandpileResultsDisplayer.class
 *
 * Given a specific Height map, generates a bitmap/buffered image of the
 * sandbox.
 *
 * 13-Jan-2015
 *
 * @author Nathan
 */
public class SandpileResultsDisplayer {

    private final int TILE_SIZE = 10;
    private final int BASE_COLOUR = 0xADFFFF;
    private final int MOD_COLOUR = 0x010101;

    private final String FILE_ID = "IMG";

    private final int MAX_COLOURS = 170;
    private final String FILE_DATE_INFO;

    private final Sandbox model;
    private final SandpileResultsContainer results;

    public SandpileResultsDisplayer(Sandbox model, SandpileResultsContainer results) {
        this.model = model;
        this.results = results;
        

        this.FILE_DATE_INFO = SandpileResultsContainer.DATE_FORMATTER.format(new Date());

    }

    public void generateAndSaveImage(long step, int[][] heightMap) {
        File file;
        String imgFileName;

        imgFileName = String.format(SandpileResultsContainer.FILE_NAME_TEMPLATE,
                results.getSimulationId(), //Simulation ID
                FILE_ID, //File identifier
                results.getSimulationGridSize(), results.getSimulationGridSize(), //Grid size
                FILE_DATE_INFO, //Date info
                step);

        file = new File(imgFileName);

        BufferedImage img;

        img = generateImage(heightMap);
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException e) {
            System.err.println("Unable to save image: " + file.getName() + " -- " + e.getMessage());
        }
    }

    private BufferedImage generateImage(int[][] heightMap) {
        Graphics2D draw;
        BufferedImage img;
        int imgSize = heightMap.length * TILE_SIZE;

        img = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
        draw = img.createGraphics();

        for (int y = 0; y < heightMap.length; y++) {
            for (int x = 0; x < heightMap[y].length; x++) {
                //draw.setColor(Color.BLACK);
                //draw.draw(new Rectangle2D.Double((x*TILE_SIZE), (y*TILE_SIZE), TILE_SIZE, TILE_SIZE));
                draw.setColor(model.getColourByHeight(heightMap[y][x]));
                //draw.setColor(Colours.getColor(heightMap[y][x]));
                draw.fillRect((x * TILE_SIZE) + 1, (y * TILE_SIZE) + 1, TILE_SIZE - 2, TILE_SIZE - 2);
            }
        }
        draw.dispose();

        return img;
    }

    private Color calculateColour(int height, int maxHeight) {
        Color selectColour;
        int colourSpace = (int) (MAX_COLOURS / (double) maxHeight);
        int remainder = colourSpace % 10;

        colourSpace -= remainder;
        
        selectColour = new Color(BASE_COLOUR - (height * MOD_COLOUR*maxHeight));
        //System.out.print("Height : " + height + " -- " );
        //System.out.format( "Colour :: 0x%X\n", selectColour.getRGB());
        return selectColour;
    }
}
