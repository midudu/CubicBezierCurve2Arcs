package mathTools;

import component.DoublePoint;

public class MathTools {

    public static double EPSILON = 1e-9;

    public static double crossProductOfThreePoints(
            final DoublePoint p0, final DoublePoint p1, final DoublePoint p2) {

        double x1 = p1.x - p0.x;
        double y1 = p1.y - p0.y;
        double x2 = p2.x - p0.x;
        double y2 = p2.y - p0.y;

        return x1 * y2 - x2 * y1;
    }

    public static DoublePoint calculateIntervalPoint(
            final double lambda, final DoublePoint A, final DoublePoint B) {

        return new DoublePoint(
                A.x + lambda * (B.x - A.x), A.y + lambda * (B.y - A.y));
    }

    public static DoublePoint calculateIntersectionOfTwoLine(
            final DoublePoint start_point_A, final DoublePoint end_point_A,
            final DoublePoint start_point_B, final DoublePoint end_point_B)
            throws Exception {

        double A1 = crossProductOfThreePoints(
                start_point_A, end_point_A, start_point_B);
        double A2 = crossProductOfThreePoints(
                start_point_A, end_point_A, end_point_B);
        double A3 = crossProductOfThreePoints(
                start_point_A, start_point_B, end_point_B);

        if (Math.abs(A2 - A1) <= MathTools.EPSILON) {
            throw new Exception("Wrong");
        }

        double t = A3 / (A2 - A1);

        return calculateIntervalPoint(t, start_point_A, end_point_A);
    }

    public static double euclideanDistance(
            final DoublePoint p, final DoublePoint q) {

        return Math.sqrt(
                (p.x - q.x) * (p.x - q.x) + (p.y - q.y) * (p.y - q.y));
    }

    public static DoublePoint findInCenterPoint(
            final DoublePoint A, final DoublePoint B, final DoublePoint C) {

        double a = euclideanDistance(B, C);
        double b = euclideanDistance(A, C);
        double c = euclideanDistance(A, B);

        double x1 = A.x;
        double y1 = A.y;

        double x2 = B.x;
        double y2 = B.y;

        double x3 = C.x;
        double y3 = C.y;

        return new DoublePoint((a * x1 + b * x2 + c * x3) / (a + b + c),
                (a * y1 + b * y2 + c * y3) / (a + b + c));
    }

    public static double getRadiusAndCenterByThreePointsOnCircle(
            final DoublePoint p1, final DoublePoint p2, final DoublePoint p3,
            DoublePoint center) {

        double a = 2.0 * (p2.x - p1.x);
        double b = 2.0 * (p2.y - p1.y);
        double c = p2.x * p2.x + p2.y * p2.y - p1.x * p1.x - p1.y * p1.y;
        double d = 2.0 * (p3.x - p2.x);
        double e = 2.0 * (p3.y - p2.y);
        double f = p3.x * p3.x + p3.y * p3.y - p2.x * p2.x - p2.y * p2.y;

        double x = (b * f - e * c) / (b * d - e * a);
        double y = (d * c - a * f) / (b * d - e * a);

        if (center == null) {
            throw new NullPointerException();
        } else {
            center.x = x;
            center.y = y;
        }

        return Math.sqrt((x - p1.x) * (x - p1.x) + (y - p1.y) * (y - p1.y));
    }

    public static DoublePoint calculateUnitTangentVector(
            final DoublePoint center, final DoublePoint pointOnCircle) {

        double x1 = center.x;
        double y1 = center.y;
        double x2 = pointOnCircle.x;
        double y2 = pointOnCircle.y;

        if (Math.abs(x1 - x2) <= MathTools.EPSILON) {
            if (y1 < y2) {
                return new DoublePoint(1.0, 0.0);
            } else {
                return new DoublePoint(-1.0, 0.0);
            }
        }

        double radius = MathTools.euclideanDistance(center, pointOnCircle);

        double delta_x = x2 - x1;
        double delta_y = y2 - y1;

        return new DoublePoint(delta_y / radius, -delta_x / radius);
    }
}
