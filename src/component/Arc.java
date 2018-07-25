package component;

public class Arc {

    public DoublePoint center;

    public double radius;

    public double startAngle;

    public double endAngle;

    public Arc(DoublePoint center, double radius, double startAngle, double endAngle) {

        this.center = center;
        this.radius = radius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }
}
