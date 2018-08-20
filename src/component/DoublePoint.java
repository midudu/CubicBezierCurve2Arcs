package component;

/**
 * This class is to store a two-dimension point. There are two dimensions: x
 * and y.
 */
public class DoublePoint {

    /* the x coordinate of the point */
    private double x;

    /* the y coordinate of the point */
    private double y;

    /**
     * Constructor
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     */
    public DoublePoint(double x, double y) {

        this.x = x;
        this.y = y;
    }

    /**
     * To get this.x.
     *
     * @return this.x
     */
    public double getX() {
        return x;
    }

    /**
     * To get this.y.
     *
     * @return this.y
     */
    public double getY() {
        return y;
    }
}
