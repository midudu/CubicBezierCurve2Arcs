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

    private static final double AllOWABLE_ERROR_FOR_FIND_T = 0.001;

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
        DoublePoint A0
                = CubicBezierTools.pointOnBezierCurve(startT,
                A, controlPointA, controlPointB, B);

        DoublePoint A1 = CubicBezierTools.pointOnBezierCurve(endT,
                A, controlPointA, controlPointB, B);

        /* Step 2: Calculate the intersection of the two tangent lines from the
        new start point and the end point*/
        DoublePoint V = calculateV(A0, A1, A, controlPointA, controlPointB, B,
                startT, endT);

        /* Step 3: Find the incenter of the triangle A0A1V */
        DoublePoint G = MathTools.findInCenterPointOfTriangle(A0, V, A1);

        /* Step 4: Find two centers of biarc */
        DoublePoint center1 = calculateCenterOfArc(A0, V, G);
        DoublePoint center2 = calculateCenterOfArc(A1, V, G);

        /* Step 5: Calculate the unit tangent vector of the circle on point G */
        DoublePoint H = MathTools.calculateUnitTangentVectorOfCircle(center1, G);

        /* Step 6: Calculate t */
        double t = findTWithNewtonAndRaphsonMethod(
                A, controlPointA, controlPointB, B, H, G, AllOWABLE_ERROR_FOR_FIND_T,
                startT, endT);

        /* Step 7: Calculate max error between the fitted arcs and the original
        Bezier curve */
        double maxError = calculateMaxError(
                A, controlPointA, controlPointB, B, G, t);

        /* Step 8: Judge if the current approximation meets the allowable error */
        if (maxError <= allowableError) {

            Arc firstArc = generateArc(center1, A0, G);
            Arc secondArc = generateArc(center2, G, A1);

            arcs.add(firstArc);
            arcs.add(secondArc);

        } else {

            convertACubicBezierCurveToArcsHelper(
                    A, controlPointA, controlPointB, B, startT, t,
                    allowableError, arcs);
            convertACubicBezierCurveToArcsHelper(
                    A, controlPointA, controlPointB, B, t, endT,
                    allowableError, arcs);
        }
    }

    /**
     * To find the t value so that (Q(t) - G) · H = 0, where
     * Q(t) = P0 * (1-t)^3 + P1 * 3t(1-t)^2 + P2 * 3t^2(1-t) + P3 * t^3. (P0,
     * P1, P2, P3 are the start point, the first control point, the second
     * control point and the end point of the current Bezier curve).
     * <p>
     * Newton-Raphson method is used in this function because
     * f(t) = (Q(t) - G) · H is monotone for t which lies in the range
     * [{@code startT}, {@code endT}. In Newton-Raphson method,
     * tn+1 = tn - f(tn)/f'(tn).
     *
     * @param A              the start point of the Bezier curve
     * @param controlPointA  the first control point which is close to the
     *                       start point
     * @param controlPointB  the second control point which is close to the
     *                       end point
     * @param B              the end point of the Bezier curve
     * @param H              the unit tangent vector on point {@code G} of the
     *                       biarc
     * @param G              the intersection point of biarc
     * @param allowableError the allowable error for
     *                       f(t) <= {@code allowableError}
     * @param startT         the t parameter which determines the start position
     * @param endT           the t parameter which determines the end position
     * @return the t value which makes f(t) is near to zero
     */
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

    /**
     * Let the point {@code newA} be the start point of the current Bezier
     * curve and the point {@code newB} be the end point of the current Bezier
     * curve. Let L1 be the tangent line to the Bezier curve on the point
     * {@code newA} and L2 be the tangent line to the Bezier curve on the point
     * {@code newB}. Then the point V is the intersection point of L1, L2. This
     * function is to calculate the coordinate of V.
     *
     * @param newA          the new start point of the Bezier curve
     * @param newB          the new end point of the Bezier curve
     * @param A             the original start point of the Bezier curve
     * @param controlPointA the first control point of the Bezier curve which
     *                      is close to {@code A}
     * @param controlPointB the second control point of the Bezier curve which
     *                      is close to {@code B}
     * @param B             the original end point of the Bezier curve
     * @param startT        the t parameter which determines the position of
     *                      the point {@code newA}
     * @param endT          the t parameter which determines the position of
     *                      the point {@code newB}
     * @return the coordinate of V
     */
    private static DoublePoint calculateV(
            DoublePoint newA, DoublePoint newB,
            DoublePoint A, DoublePoint controlPointA,
            DoublePoint controlPointB, DoublePoint B,
            double startT, double endT) {

        DoublePoint unitTangentVector0
                = CubicBezierTools.calculateUnitTangentVectorOfBezierCurve(
                A, controlPointA, controlPointB, B, startT);

        DoublePoint unitTangentVector1
                = CubicBezierTools.calculateUnitTangentVectorOfBezierCurve(
                A, controlPointA, controlPointB, B, endT);

        DoublePoint adjacentNewA
                = new DoublePoint(newA.getX() + unitTangentVector0.getX(),
                newA.getY() + unitTangentVector0.getY());

        DoublePoint adjacentNewB
                = new DoublePoint(newB.getX() + unitTangentVector1.getX(),
                newB.getY() + unitTangentVector1.getY());

        return MathTools.calculateIntersectionOfTwoLine(
                newA, adjacentNewA, adjacentNewB, newB);
    }

    /**
     * Find the center O of a circle which makes the point {@code A} and the point
     * {@code G} on the circle and the line OA is vertical to the line AV.
     *
     * @param A the first point
     * @param V the second point
     * @param G the third point
     * @return the center of the circle
     */
    private static DoublePoint calculateCenterOfArc(
            DoublePoint A, DoublePoint V, DoublePoint G) {

        double a11 = V.getX() - A.getX();
        double a12 = V.getY() - A.getY();
        double b1 = A.getX() * a11 + A.getY() * a12;

        double a21 = G.getX() - A.getX();
        double a22 = G.getY() - A.getY();
        double b2 = (G.getX() + A.getX()) / 2.0 * a21
                + (G.getY() + A.getY()) / 2.0 * a22;

        return MathTools.solveLinearEquationsOfTwoUnknownVariables(
                a11, a12, b1, a21, a22, b2);
    }

    /**
     * This method is to calculate the max error between the original Bezier
     * curve and the fitted arcs.
     *
     * @param A             the start point of the Bezier curve
     * @param controlPointA the first control point of the Bezier curve which
     *                      is close to {@code A}
     * @param controlPointB the second control point of the Bezier curve which
     *                      is close to {@code B}
     * @param B             the end point of the Bezier curve
     * @param G             the incenter point
     * @param t             the t value which makes f(t) = (Q(t) - G) · H = 0
     * @return the max error between the original Bezier curve and the fitted
     * arcs.
     */
    private static double calculateMaxError(
            DoublePoint A, DoublePoint controlPointA,
            DoublePoint controlPointB, DoublePoint B,
            DoublePoint G, double t) {

        DoublePoint Q_t = CubicBezierTools.pointOnBezierCurve(t,
                A, controlPointA, controlPointB, B);

        return MathTools.euclideanDistance(Q_t, G);
    }

    /**
     * To generate an {@code Arc} object according to the center, start point
     * and the end point.
     *
     * @param center     the center of the circle which makes the current arc on it
     * @param startPoint the start point of the arc
     * @param endPoint   the end point of the arc
     * @return an Arc object
     */
    private static Arc generateArc(
            DoublePoint center, DoublePoint startPoint, DoublePoint endPoint) {

        double radius = MathTools.euclideanDistance(center, endPoint);

        double startAngle
                = MathTools.calculateAngleRelativeToCircleCenter(center, startPoint);
        double endAngle
                = MathTools.calculateAngleRelativeToCircleCenter(center, endPoint);

        while (endAngle < startAngle - Math.PI) {
            endAngle += 2.0 * Math.PI;
        }
        while (endAngle > startAngle + Math.PI) {
            endAngle -= 2.0 * Math.PI;
        }

        if (startAngle <= endAngle) {
            return new Arc(center, radius, startAngle, endAngle, false);
        } else {
            return new Arc(center, radius, startAngle, endAngle, true);
        }
    }
}
