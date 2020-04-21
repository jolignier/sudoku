package model;

/**
 * @author Ipro
 * Date  21/04/2020
 */

public class Point {

    private int x;
    private int y;

    public Point() {
        x=0; y=0;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
