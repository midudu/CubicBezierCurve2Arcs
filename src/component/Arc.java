package component;


public class Arc {

    private DoublePoint center;

    private double radius;

    private double startAngle;

    private double endAngle;

    private boolean clockwiseFlag;

    public Arc(DoublePoint center, double radius,
               double startAngle, double endAngle, boolean clockwiseFlag) {

        this.center = center;
        this.radius = radius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.clockwiseFlag = clockwiseFlag;
    }

    public double getEndAngle() {
        return endAngle;
    }

    public double getRadius() {
        return radius;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public DoublePoint getCenter() {
        return center;
    }

    public boolean getClockwiseFlag() {
        return clockwiseFlag;
    }
}
