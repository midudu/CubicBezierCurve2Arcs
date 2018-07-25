package component;

public class DoublePoint {

    //x-coordinate
    public double x;

    //y-coordinate
    public double y;

    /**
     * Constructor
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public DoublePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {

        return String.valueOf(this.x) + " " + String.valueOf(this.y);
    }
}
