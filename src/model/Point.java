package model;

/**
 * @author Ipro
 * Date  21/04/2020
 */

/**
 *  Represent a simple 2D point
 */
public class Point {

    private int x;
    private int y;

    /**
     * Default constructor
     */
    public Point() {
        x=0; y=0;
    }

    /**
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the X coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return the Y coordinate
     */
    public int getY() {
        return y;
    }
}
