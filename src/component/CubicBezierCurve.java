package component;

public class CubicBezierCurve {

    public DoublePoint A;
    public DoublePoint controlPointA;
    public DoublePoint controlPointB;
    public DoublePoint B;

    public CubicBezierCurve(DoublePoint A, DoublePoint controlPointA,
                            DoublePoint controlPointB, DoublePoint B) {

        this.A = A;
        this.controlPointA = controlPointA;
        this.controlPointB = controlPointB;
        this.B = B;
    }
}
