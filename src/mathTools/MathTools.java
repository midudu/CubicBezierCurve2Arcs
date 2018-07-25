package mathTools;

import component.DoublePoint;

public class MathTools {

    public static double EPSILON = 1e-9;

    public static double CrossProductOfThreePoints(
            DoublePoint p0, DoublePoint p1, DoublePoint p2) {

        double x1 = p1.x - p0.x;
        double y1 = p1.y - p0.y;
        double x2 = p2.x - p0.x;
        double y2 = p2.y - p0.y;

        return x1 * y2 - x2 * y1;
    }

    public static DoublePoint CalculateIntervalPoint(
            double lambda, DoublePoint A, DoublePoint B) {

        return new DoublePoint(
                A.x + lambda * (B.x - A.x), A.y + lambda * (B.y - A.y));
    }

    public static DoublePoint CalculateIntersectionOfTwoLine(
            DoublePoint start_point_A, DoublePoint end_point_A,
            DoublePoint start_point_B, DoublePoint end_point_B)
            throws Exception {

        double A1 = CrossProductOfThreePoints(
                start_point_A, end_point_A, start_point_B);
        double A2 = CrossProductOfThreePoints(
                start_point_A, end_point_A, end_point_B);
        double A3 = CrossProductOfThreePoints(
                start_point_A, start_point_B, end_point_B);

        if (Math.abs(A2 - A1) <= MathTools.EPSILON) {
            throw new Exception("Wrong");
        }

        double t = A3 / (A2 - A1);

        return CalculateIntervalPoint(t, start_point_A, end_point_A);
    }

    public static double EuclideanDistance(DoublePoint p, DoublePoint q) {

        return Math.sqrt(
                (p.x - q.x) * (p.x - q.x) + (p.y - q.y) * (p.y - q.y));
    }

    public static DoublePoint FindInCenterPoint(
            DoublePoint A, DoublePoint B, DoublePoint C) {

        double a = EuclideanDistance(B, C);
        double b = EuclideanDistance(A, C);
        double c = EuclideanDistance(A, B);

        double x1 = A.x;
        double y1 = A.y;

        double x2 = B.x;
        double y2 = B.y;

        double x3 = C.x;
        double y3 = C.y;

        return new DoublePoint((a * x1 + b * x2 + c * x3) / (a + b + c),
                (a * y1 + b * y2 + c * y3) / (a + b + c));
    }
}
