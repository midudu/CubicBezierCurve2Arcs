package mathTools;

import component.DoublePoint;

/**
 * This class contains different mathematical tools.
 */
public class MathTools {

    /* a constant which represents a quite small number */
    public static final double EPSILON = 1e-9;

    /**
     * To calculate the intersection point of two line.
     *
     * @param startPointA the start point of the first line
     * @param endPointA   the end point of the first line
     * @param startPointB the start point of the second line
     * @param endPointB   the end point of the second line
     * @return the coordinate of the intersection point
     */
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

    /**
     * To calculate the cross-product of vector p0p1 and vector p0p2.
     *
     * @param p0 the first point
     * @param p1 the second point
     * @param p2 the third point
     * @return p0p1 · p0p2
     */
    private static double crossProductOfThreePoints(
            DoublePoint p0, DoublePoint p1, DoublePoint p2) {

        double x1 = p1.getX() - p0.getX();
        double y1 = p1.getY() - p0.getY();
        double x2 = p2.getX() - p0.getX();
        double y2 = p2.getY() - p0.getY();

        return x1 * y2 - x2 * y1;
    }

    /**
     * To calculate a point on the given segment according to the λ parameter
     *
     * @param lambda the interval proportion
     * @param A      start point of the line
     * @param B      end point of the line
     * @return a point on the segment AB according to {@code lambda}
     */
    private static DoublePoint calculateIntervalPoint(
            double lambda, DoublePoint A, DoublePoint B) {

        return new DoublePoint(
                A.getX() + lambda * (B.getX() - A.getX()),
                A.getY() + lambda * (B.getY() - A.getY())
        );
    }

    /**
     * To calculate the Euclidean distance between two points
     *
     * @param A the first point
     * @param B the second point
     * @return the Euclidean distance between two points
     */
    public static double euclideanDistance(DoublePoint A, DoublePoint B) {

        return Math.sqrt(
                (A.getX() - B.getX()) * (A.getX() - B.getX())
                        + (A.getY() - B.getY()) * (A.getY() - B.getY()));
    }

    /**
     * To find the incenter of a triangle.
     *
     * @param p1 the first vertex of the triangle
     * @param p2 the second vertex of the triangle
     * @param p3 the third vertex of the triangle
     * @return the incenter of a triangle
     */
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

    /**
     * To solve the equation:
     * <p>
     * a11*x + a12*y = b1
     * a21*x + a22*y = b2.
     *
     * @param a11 coefficient 1
     * @param a12 coefficient 2
     * @param b1  parameter1
     * @param a21 coefficient 3
     * @param a22 coefficient 4
     * @param b2  parameter2
     * @return (x, y) in {@code DoublePoint} type
     */
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

    /**
     * To calculate the result of
     * <p>
     * |a11, a12|
     * |a21, a22|.
     *
     * @param a11 the left-top element
     * @param a12 the right-top element
     * @param a21 the left-bottom element
     * @param a22 the right-bottom element
     * @return the result of
     * |a11, a12|
     * |a21, a22|.
     */
    private static double calculateSecondOrderDeterminant(
            double a11, double a12, double a21, double a22) {

        return a11 * a22 - a12 * a21;
    }

    /**
     * To calculate the unit tangent vector of a point on circle.
     *
     * @param center        the center of the circle
     * @param pointOnCircle a point on the circle
     * @return the unit tangent vector of the point on the circle
     */
    public static DoublePoint calculateUnitTangentVectorOfCircle(
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

    /**
     * To calculate the dot-product: p1·p2.
     *
     * @param p1 the first vector
     * @param p2 the second vector
     * @return the result of the dot-product: p1·p2
     */
    public static double dotProductOfTwoPoints(DoublePoint p1, DoublePoint p2) {

        return p1.getX() * p2.getX() + p1.getY() * p2.getY();
    }

    /**
     * To calculate the angle from a point to the center of the circle and the
     * x axis. The return value lies in the range [-pi, pi].
     *
     * @param center the center of the circle
     * @param point  a point on the circle
     * @return Let {@code center} be point O, and let {@code point} be point A
     * and let a point on the positive x-axis be point X, then the calculated
     * angle is <AOX. The return value lies in the range [-pi, pi].
     */
    public static double calculateAngleRelativeToCircleCenter(
            DoublePoint center, DoublePoint point) {

        double deltaX = point.getX() - center.getX();
        double deltaY = point.getY() - center.getY();

        return Math.atan2(deltaY, deltaX);
    }
}
