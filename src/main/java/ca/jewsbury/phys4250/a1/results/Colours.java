package ca.jewsbury.phys4250.a1.results;

import java.awt.Color;

/**
 * Colours.class
 *
 * ENUM class that defines the colours for the Abelian sandbox model. Returns
 * colours for a specified height. They're kind of purple.
 *
 * 13-Jan-2015
 *
 * @author Nathan
 */
public enum Colours {

    FIRST(76, 0, 153),
    SECOND(127, 0, 255),
    THIRD(178, 102, 255),
    FOURTH(204, 153, 255);

    private Color clr;

    private Colours(int r, int g, int b) {
        clr = new Color(r, g, b);
    }

    public Color getColor() {
        return clr;
    }

    public static Color getColor(int height) {
        return Colours.values()[height].getColor();
    }

}
