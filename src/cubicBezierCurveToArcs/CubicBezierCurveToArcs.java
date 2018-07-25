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

        DoublePoint G = MathTools.findInCenterPoint(A0, V, A1);

        System.out.print(G.toString());
    }

    public static void main(String[] args) throws Exception {

        /*DoublePoint A = new DoublePoint(0,0);
        DoublePoint controlPointA = new DoublePoint(0.25, Math.sqrt(3)/4.0);
        DoublePoint controlPointB = new DoublePoint(0.75, Math.sqrt(3)/4.0);
        DoublePoint B = new DoublePoint(1,0);*/

        DoublePoint A = new DoublePoint(0, 0);
        DoublePoint controlPointA = new DoublePoint(0.25, Math.sqrt(3) / 4.0);
        DoublePoint controlPointB = new DoublePoint(0.75, Math.sqrt(3) / 4.0);

        DoublePoint center = new DoublePoint(0.0, 0.0);
        double radius = MathTools.getRadiusAndCenterByThreePointsOnCircle(
                A,controlPointA, controlPointB, center);
        System.out.println(radius);
        System.out.println(MathTools.euclideanDistance(A, center));
        System.out.println(MathTools.euclideanDistance(controlPointA, center));
        System.out.println(MathTools.euclideanDistance(controlPointB, center));
    }
}
