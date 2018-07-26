package component;

public class DoublePoint {

    public double x;
    public double y;

    public DoublePoint(double x, double y) {

        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.valueOf(x) + " " + String.valueOf(y);
    }
}
