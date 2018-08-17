package mathTools;

import component.DoublePoint;

public class MathTools {

    public static final double EPSILON = 1e-9;


    public static DoublePoint calculateIntersectionOfTwoLine(
            DoublePoint startPointA, DoublePoint endPointA,
            DoublePoint startPointB, DoublePoint endPointB) {

        double A1 = MathTools.crossProductOfThreePoints(
                startPointA, endPointA, startPointB);
        double A2 = MathTools.crossProductOfThreePoints(
                startPointA, endPointA, endPointB);
        double A3 = MathTools.crossProductOfThreePoints(
                startPointA, startPointB, endPointB);

        assert (Math.abs(A2 - A1) > MathTools.EPSILON);

        double t = A3 / (A2 - A1);

        return MathTools.calculateIntervalPoint(t, startPointA, endPointA);
    }


    private static double crossProductOfThreePoints(
            DoublePoint p0, DoublePoint p1, DoublePoint p2) {

        double x1 = p1.getX() - p0.getX();
        double y1 = p1.getY() - p0.getY();
        double x2 = p2.getX() - p0.getX();
        double y2 = p2.getY() - p0.getY();

        return x1 * y2 - x2 * y1;
    }


    private static DoublePoint calculateIntervalPoint(
            double lambda, DoublePoint A, DoublePoint B) {

        return new DoublePoint(
                A.getX() + lambda * (B.getX() - A.getX()),
                A.getY() + lambda * (B.getY() - A.getY())
        );
    }


    public static double euclideanDistance(DoublePoint p, DoublePoint q) {

        return Math.sqrt(
                (p.getX() - q.getX()) * (p.getX() - q.getX())
                        + (p.getY() - q.getY()) * (p.getY() - q.getY()));
    }


    public static DoublePoint findInCenterPointOfTriangle(
            DoublePoint p1, DoublePoint p2, DoublePoint p3) {

        double a = MathTools.euclideanDistance(p2, p3);
        double b = MathTools.euclideanDistance(p1, p3);
        double c = MathTools.euclideanDistance(p1, p2);

        double x1 = p1.getX();
        double y1 = p1.getY();

        double x2 = p2.getX();
        double y2 = p2.getY();

        double x3 = p3.getX();
        double y3 = p3.getY();

        return new DoublePoint((a * x1 + b * x2 + c * x3) / (a + b + c),
                (a * y1 + b * y2 + c * y3) / (a + b + c));
    }


    public static DoublePoint solveLinearEquationsOfTwoUnknownVariables(
            double a11, double a12, double b1,
            double a21, double a22, double b2) {

        double x =
                calculateSecondOrderDeterminant(b1, a12, b2, a22) /
                        calculateSecondOrderDeterminant(a11, a12, a21, a22);

        double y =
                calculateSecondOrderDeterminant(a11, b1, a21, b2) /
                        calculateSecondOrderDeterminant(a11, a12, a21, a22);

        return new DoublePoint(x, y);
    }


    private static double calculateSecondOrderDeterminant(
            double a11, double a12, double a21, double a22) {

        return a11 * a22 - a12 * a21;
    }


    public static DoublePoint CalculateUnitTangentVectorOfCircle(
            DoublePoint center, DoublePoint pointOnCircle) {

        double x1 = center.getX();
        double y1 = center.getY();
        double x2 = pointOnCircle.getX();
        double y2 = pointOnCircle.getY();

        if (Math.abs(x1 - x2) <= MathTools.EPSILON) {
            if (y1 < y2) {
                return new DoublePoint(1.0, 0.0);
            } else {
                return new DoublePoint(-1.0, 0.0);
            }
        }

        double radius = MathTools.euclideanDistance(center, pointOnCircle);

        double deltaX = x2 - x1;
        double deltaY = y2 - y1;

        return new DoublePoint(deltaY / radius, 0.0 - deltaX / radius);
    }


    public static double dotProductOfTwoPoints(DoublePoint p1, DoublePoint p2) {

        return p1.getX() * p2.getX() + p1.getY() * p2.getY();
    }


    public static double calculateAngleRelativeToCircleCenter(
            DoublePoint center, DoublePoint point) {

        double deltaX = point.getX() - center.getX();
        double deltaY = point.getY() - center.getY();

        return Math.atan2(deltaY, deltaX);
    }
}
