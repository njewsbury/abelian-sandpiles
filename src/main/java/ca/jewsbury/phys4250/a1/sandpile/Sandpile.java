package ca.jewsbury.phys4250.a1.sandpile;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Sandpile.class
 *
 *
 *
 * 13-Jan-2015
 *
 * @author Nathan
 */
public class Sandpile {

    private final int pileX, pileY;

    private int height;
    private long touched;
    
    public Sandpile(int x, int y) {
        this.pileX = x;
        this.pileY = y;

        this.height = 0;
        this.touched = 0;
    }

    public void increaseHeight() {
        this.touched++;
        height++;
    }
    public void increaseHeight(int amount) {
        this.touched++;
        height += amount;
    }
    
    public void setHeight(int height) {
        this.touched++;
        this.height = height;
    }
    
    public int getHeight() {
        return height;
    }

    public int getPileX() {
        return pileX;
    }

    public int getPileY() {
        return pileY;
    }
    
    public long getTouchCount() {
        return touched;
    }
}
