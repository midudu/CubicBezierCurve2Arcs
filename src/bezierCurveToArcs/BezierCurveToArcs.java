package bezierCurveToArcs;

import component.Arc;
import component.DoublePoint;
import mathTools.MathTools;
import mathTools.cubicBezierTools.CubicBezierTools;

import java.util.ArrayList;


/**
 * This class contains methods to convert a cubic Bezier curve to a series of
 * arcs.
 * <p>
 * The algorithm is from the paper "Approximation of a planar cubic Bezier
 * spiral by circular arcs. D.J. Walton, D.S. Meek, Department of Computer
 * Science, University of Manitoba, Winnipeg, Man., Canada R3T 2N2".
 * <p>
 * Note that the input cubic Bezier curve should have an angle of less than 180
 * degrees from the start point of to the end point. If not so, the result
 * might be wrong.
 */
public class BezierCurveToArcs {

    /**
     * To convert a cubic Bezier curve to a series of arcs.
     *
     * @param A              the start point of the Bezier curve
     * @param controlPointA  the first control point of the Bezier curve which
     *                       is close to {@code A}
     * @param controlPointB  the second control point of the Bezier curve which
     *                       is close to {@code B}
     * @param B              the end point of the Bezier curve
     * @param allowableError the allowable error
     * @param arcs           the fitted arcs
     */
    public static void convertACubicBezierCurveToArcs(
            DoublePoint A, DoublePoint controlPointA, DoublePoint controlPointB,
            DoublePoint B, double allowableError, ArrayList<Arc> arcs) {

        if (!arcs.isEmpty()) {
            arcs.clear();
        }

        convertACubicBezierCurveToArcsHelper(
                A, controlPointA, controlPointB, B,
                0.0, 1.0, allowableError, arcs);
    }

    /**
     * This function is an auxiliary function for the method
     * {@code convertACubicBezierCurveToArcs}. The aim is to generate fitted
     * arcs from the original Bezier curve according to the range of t.
     *
     * @param A              the start point of the Bezier curve
     * @param controlPointA  the control point which is close to the start
     *                       point
     * @param controlPointB  the control point which is close to the end
     *                       point
     * @param B              the end point of the Bezier curve
     * @param startT         the start t value for Bezier curve. It is a
     *                       parameter to determine the start range. The range
     *                       of it should lies in [0.0, endT]
     * @param endT           the end t value for Bezier curve. It is a
     *                       parameter to determine the end range. The range
     *                       of it should lies in [startT, 1.0]
     * @param allowableError the allowable error between the original
     *                       Bezier curve and the fitted arcs
     * @param arcs           the fitted arcs
     */
    private static void convertACubicBezierCurveToArcsHelper(
            DoublePoint A, DoublePoint controlPointA, DoublePoint controlPointB,
            DoublePoint B, double startT, double endT, double allowableError,
            ArrayList<Arc> arcs) {

        /* Step 1: Calculate the new start point and the new end point of the
        current circumstance */
        DoublePoint newA
                = CubicBezierTools.pointOnBezierCurve(startT,
                A, controlPointA, controlPointB, B);

        DoublePoint newB = CubicBezierTools.pointOnBezierCurve(endT,
                A, controlPointA, controlPointB, B);

        /* Step 2: Calculate the unit tangent vector of the Bezier curve on the
        start point and the end point */
        DoublePoint unitTangentVector0
                = CubicBezierTools.CalculateUnitTangentVectorOfBezierCurve(
                A, controlPointA, controlPointB, B, startT);

        DoublePoint unitTangentVector1
                = CubicBezierTools.CalculateUnitTangentVectorOfBezierCurve(
                A, controlPointA, controlPointB, B, endT);

        DoublePoint adjacentNewA
                = new DoublePoint(newA.getX() + unitTangentVector0.getX(),
                newA.getY() + unitTangentVector0.getY());

        DoublePoint adjacentNewB
                = new DoublePoint(newB.getX() + unitTangentVector1.getX(),
                newB.getY() + unitTangentVector1.getY());

        /* Step 3: Calculate the intersection of the two tangent line from the
        start point and the end point*/
        DoublePoint V = MathTools.calculateIntersectionOfTwoLine(
                newA, adjacentNewA, adjacentNewB, newB);

        /* Step 4: Find incenter of the triangle A0A1V */
        DoublePoint G = MathTools.findInCenterPointOfTriangle(newA, V, newB);

        /* Step 5: Find two centers of biarc */
        double a11 = V.getX() - newA.getX();
        double a12 = V.getY() - newA.getY();
        double b1 = newA.getX() * a11 + newA.getY() * a12;
        double a21 = G.getX() - newA.getX();
        double a22 = G.getY() - newA.getY();
        double b2 = (G.getX() + newA.getX()) / 2.0 * a21
                + (G.getY() + newA.getY()) / 2.0 * a22;
        DoublePoint center1
                = MathTools.solveLinearEquationsOfTwoUnknownVariables(
                a11, a12, b1, a21, a22, b2);

        a11 = V.getX() - newB.getX();
        a12 = V.getY() - newB.getY();
        b1 = newB.getX() * a11 + newB.getY() * a12;
        a21 = G.getX() - newB.getX();
        a22 = G.getY() - newB.getY();
        b2 = (G.getX() + newB.getX()) / 2.0 * a21
                + (G.getY() + newB.getY()) / 2.0 * a22;
        DoublePoint center2
                = MathTools.solveLinearEquationsOfTwoUnknownVariables(
                a11, a12, b1, a21, a22, b2);

        /* Step 6: Calculate the unit tangent vector of the circle on point G */
        DoublePoint H = MathTools.CalculateUnitTangentVectorOfCircle(center1, G);

        /* Step 7: Calculate t */
        double t = findTWithNewtonAndRaphsonMethod(
                A, controlPointA, controlPointB, B, H, G, 0.001,
                startT, endT);


        /* Step 8: Calculate d */
        DoublePoint Q_t = CubicBezierTools.pointOnBezierCurve(t,
                A, controlPointA, controlPointB, B);
        double d = MathTools.euclideanDistance(Q_t, G);

        /* Step 9: Judge if the current approximation meets the allowable error */
        if (d <= allowableError) {

            double radius1 = MathTools.euclideanDistance(center1, G);
            double radius2 = MathTools.euclideanDistance(center2, G);

            double startAngle1
                    = MathTools.calculateAngleRelativeToCircleCenter(center1, newA);
            double endAngle1
                    = MathTools.calculateAngleRelativeToCircleCenter(center1, newB);

            while (endAngle1 < startAngle1 - Math.PI) {
                endAngle1 += 2.0 * Math.PI;
            }
            while (endAngle1 > startAngle1 + Math.PI) {
                endAngle1 -= 2.0 * Math.PI;
            }

            if (startAngle1 <= endAngle1) {
                arcs.add(new Arc(center1, radius1, startAngle1, endAngle1, false));
            } else {
                arcs.add(new Arc(center1, radius1, startAngle1, endAngle1, true));
            }

            double startAngle2
                    = MathTools.calculateAngleRelativeToCircleCenter(center2, newA);
            double endAngle2
                    = MathTools.calculateAngleRelativeToCircleCenter(center2, newB);

            while (endAngle2 < startAngle2 - Math.PI) {
                endAngle2 += 2.0 * Math.PI;
            }
            while (endAngle2 > startAngle2 + Math.PI) {
                endAngle2 -= 2.0 * Math.PI;
            }

            if (startAngle2 <= endAngle2) {
                arcs.add(new Arc(center2, radius2, startAngle2, endAngle2, false));
            } else {
                arcs.add(new Arc(center2, radius2, startAngle2, endAngle2, true));
            }

        } else {

            convertACubicBezierCurveToArcsHelper(
                    A, controlPointA, controlPointB, B, startT, t,
                    allowableError, arcs);
            convertACubicBezierCurveToArcsHelper(
                    A, controlPointA, controlPointB, B, t, endT,
                    allowableError, arcs);
        }
    }

    private static double findTWithNewtonAndRaphsonMethod(
            DoublePoint A, DoublePoint controlPointA,
            DoublePoint controlPointB, DoublePoint B,
            DoublePoint H, DoublePoint G, double allowableError,
            double startT, double endT) {

        double tn = startT + (endT - startT) / 2.0;

        DoublePoint Q_tn = CubicBezierTools.pointOnBezierCurve(tn,
                A, controlPointA, controlPointB, B);

        double fn = MathTools.dotProductOfTwoPoints(Q_tn, H)
                - MathTools.dotProductOfTwoPoints(G, H);

        while (Math.abs(fn) > allowableError) {

            DoublePoint d_Q_tn
                    = CubicBezierTools.calculateDerivativeOnBezierCurve(
                    A, controlPointA, controlPointB, B, tn);

            double d_fn = MathTools.dotProductOfTwoPoints(d_Q_tn, H);

            tn = tn - fn / d_fn;

            Q_tn = CubicBezierTools.pointOnBezierCurve(tn,
                    A, controlPointA, controlPointB, B);

            fn = MathTools.dotProductOfTwoPoints(Q_tn, H)
                    - MathTools.dotProductOfTwoPoints(G, H);
        }

        return tn;
    }
}
