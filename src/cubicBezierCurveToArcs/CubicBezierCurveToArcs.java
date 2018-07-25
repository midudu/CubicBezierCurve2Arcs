package cubicBezierCurveToArcs;

import component.Circle;
import component.CubicBezierCurve;
import component.DoublePoint;
import mathTools.MathTools;

class CubicBezierCurveToArcsTools {

    public static void cubicBezierCurveToArcs(
            CubicBezierCurve bezierCurve, Circle[] circles) throws Exception {

        DoublePoint A0 = bezierCurve.A;
        DoublePoint A1 = bezierCurve.B;
        DoublePoint V = MathTools.calculateIntersectionOfTwoLine(
                bezierCurve.A, bezierCurve.controlPointA,
                bezierCurve.controlPointB, bezierCurve.B);

        // Step 1: Find Incenter of the triangle A0A1V
        DoublePoint G = MathTools.findInCenterPoint(A0, V, A1);

        // Step 2: Find center of the circle which makes A0, A1, G on the circle
        DoublePoint center = new DoublePoint(0.0, 0.0);
        MathTools.getRadiusAndCenterByThreePointsOnCircle(
                A0, G, A1, center);

        // Step 3: Calculate the unit tangent vector of the circle on point G
        DoublePoint H = MathTools.calculateUnitTangentVector(center, G);

        System.out.println(H.toString());

        // Step 4:
    }

    public static void main(String[] args) throws Exception {

        DoublePoint A = new DoublePoint(0, 0);
        DoublePoint controlPointA = new DoublePoint(0.25, Math.sqrt(3) / 4.0);
        DoublePoint controlPointB = new DoublePoint(0.75, Math.sqrt(3) / 4.0);
        DoublePoint B = new DoublePoint(2.0, 0);

        Circle[] circles = null;
        CubicBezierCurve bezierCurve
                = new CubicBezierCurve(A, controlPointA, controlPointB, B);

        cubicBezierCurveToArcs(bezierCurve, circles);
    }
}
