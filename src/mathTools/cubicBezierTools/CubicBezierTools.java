package mathTools.cubicBezierTools;

import component.DoublePoint;
import mathTools.MathTools;

/**
 * This class contains mathematical methods which are only for Bezier curves.
 */
public class CubicBezierTools {

    /**
     * To calculate a point on a Bezier curve according to the parameter t.
     *
     * @param t             the parameter which determines the position of the
     *                      point. The range of it lies in [0.0, 1.0]
     * @param A             the start point of the Bezier curve
     * @param controlPointA the control point which is close to the start point
     * @param controlPointB the control point which is close to the end point
     * @param B             the end point of the Bezier curve
     * @return a point on the current Bezier curve
     */
    public static DoublePoint pointOnBezierCurve(
            double t, DoublePoint A, DoublePoint controlPointA,
            DoublePoint controlPointB, DoublePoint B) {


        double s = 1 - t;

        double x = s * s * s * A.getX() + 3 * (s * s * t) * controlPointA.getX()
                + 3 * (t * t * s) * controlPointB.getX() + t * t * t * B.getX();

        double y = s * s * s * A.getY() + 3 * (s * s * t) * controlPointA.getY()
                + 3 * (t * t * s) * controlPointB.getY() + t * t * t * B.getY();

        return new DoublePoint(x, y);
    }

    /**
     * To calculate the unit tangent vector of a point on the bezier curve.
     *
     * @param A             the start point of the Bezier curve
     * @param controlPointA the control point which is close to the start point
     * @param controlPointB the control point which is close to the end point
     * @param B             the end point of the Bezier curve
     * @param t             a parameter to determine the position on curve
     *                      which must lies in the range [0, 1]
     * @return the unit tangent vector of the point on the Bezier curve
     */
    public static DoublePoint calculateUnitTangentVectorOfBezierCurve(
            DoublePoint A, DoublePoint controlPointA,
            DoublePoint controlPointB, DoublePoint B, double t) {

        assert (t >= 0.0 && t <= 1.0);

        double x0 = A.getX();
        double y0 = A.getY();
        double x1 = controlPointA.getX();
        double y1 = controlPointA.getY();
        double x2 = controlPointB.getX();
        double y2 = controlPointB.getY();
        double x3 = B.getX();
        double y3 = B.getY();

        double s = 1.0 - t;

        double dy_dt = -3 * y0 * s * s + 3 * y1 * (s * s - 2 * t * s)
                + 3 * y2 * (2 * t * s - t * t) + 3 * y3 * t * t;
        double dx_dt = -3 * x0 * s * s + 3 * x1 * (s * s - 2 * t * s)
                + 3 * x2 * (2 * t * s - t * t) + 3 * x3 * t * t;

        if (Math.abs(dx_dt) <= MathTools.EPSILON) {

            if (dy_dt >= 0.0) {
                return new DoublePoint(0.0, 1.0);
            } else {
                return new DoublePoint(0.0, -1.0);
            }
        }

        double hypotenuse = Math.sqrt(dx_dt * dx_dt + dy_dt * dy_dt);

        return new DoublePoint(dx_dt / hypotenuse, dy_dt / hypotenuse);
    }

    /**
     * Let P0, P1, P2, P3 be the start point, first control point, second
     * control point and the end point of the Bezier curve. For any point Q on
     * the Bezier curve, Q(t) = P0*(1-t)^3 + P1*3t(1-t)^2 + P2*3t^2(1-t) + P3*t^3
     * = (x(t), y(t)). This method is to calculate Q'(t) = (x'(t), y'(t)).
     *
     * @param A             the start point of the Bezier curve
     * @param controlPointA the control point which is close to the start
     *                      point
     * @param controlPointB the control point which is close to the end
     *                      point
     * @param B             the end point
     * @param t             a parameter to determine the position on the
     *                      Bezier curve
     * @return Q'(t) = (x'(t), y'(t))
     */
    public static DoublePoint calculateDerivativeOnBezierCurve(
            DoublePoint A, DoublePoint controlPointA,
            DoublePoint controlPointB, DoublePoint B, double t) {

        double x0 = A.getX();
        double y0 = A.getY();
        double x1 = controlPointA.getX();
        double y1 = controlPointA.getY();
        double x2 = controlPointB.getX();
        double y2 = controlPointB.getY();
        double x3 = B.getX();
        double y3 = B.getY();

        double s = 1.0 - t;

        double dy_dt = -3 * y0 * s * s + 3 * y1 * (s * s - 2 * t * s)
                + 3 * y2 * (2 * t * s - t * t) + 3 * y3 * t * t;
        double dx_dt = -3 * x0 * s * s + 3 * x1 * (s * s - 2 * t * s)
                + 3 * x2 * (2 * t * s - t * t) + 3 * x3 * t * t;

        return new DoublePoint(dx_dt, dy_dt);
    }
}
