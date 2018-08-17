package component;

public class DoublePoint {

    private double x;
    private double y;

    public DoublePoint(double x, double y) {

        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.valueOf(x) + " " + String.valueOf(y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
