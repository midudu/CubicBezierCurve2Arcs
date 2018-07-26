package cubicBezierCurveToArcs;

import component.Arc;
import component.CubicBezierCurve;
import component.DoublePoint;
import debugUtil.WriteFile;
import mathTools.MathTools;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import potrace.Curve;
import potrace.Path;
import potrace.Potrace;

import java.util.ArrayList;

class CubicBezierCurveToArcsTools {

    public static void cubicBezierCurveToArcs(
            CubicBezierCurve bezierCurve, ArrayList<Arc> arcs,
            double allowableError) throws Exception {

        DoublePoint A = bezierCurve.A;
        DoublePoint controlPointA = bezierCurve.controlPointA;
        DoublePoint controlPointB = bezierCurve.controlPointB;
        DoublePoint B = bezierCurve.B;

        if (!arcs.isEmpty()) {
            arcs.clear();
        }

        cubicBezierCurveToArcsHelper(A, controlPointA, controlPointB, B,
                0.0, 1.0,
                arcs, allowableError);
    }

    private static void cubicBezierCurveToArcsHelper(
            final DoublePoint A, final DoublePoint controlPointA,
            final DoublePoint controlPointB, final DoublePoint B,
            final double startT, final double endT,
            final ArrayList<Arc> arcs, final double allowableError)
            throws Exception {

        /*Step 1: Calculate the unit tangent vector of the Bezier curve
                 on the start point and the end point*/
        DoublePoint unitTangentVector0 =
                MathTools.calculateUnitTangentVectorOfBezierCurve(
                        A, controlPointA, controlPointB, B, startT);

        DoublePoint unitTangentVector1 =
                MathTools.calculateUnitTangentVectorOfBezierCurve(
                        A, controlPointA, controlPointB, B, endT);

        DoublePoint adjacentA = new DoublePoint(
                A.x + unitTangentVector0.x, A.y + unitTangentVector0.x);
        DoublePoint adjacentB = new DoublePoint(
                B.x + unitTangentVector1.x, B.y + unitTangentVector1.x);

        /*Step 2: Calculate the intersection of the two tangent line from the
                start point and the end point*/
        DoublePoint V = MathTools.calculateIntersectionOfTwoLine(
                A, adjacentA, adjacentB, B);

        /*Step 3: Find incenter of the triangle A0A1V*/
        DoublePoint G = MathTools.findInCenterPoint(A, V, B);

        /*Step 4: Find center of the circle which makes A0, A1, G on the circle*/
        DoublePoint center = new DoublePoint(0.0, 0.0);
        double radius = MathTools.getRadiusAndCenterByThreePointsOnCircle(
                A, G, B, center);

        /*Step 5: Calculate the unit tangent vector of the circle on point G*/
        DoublePoint H = MathTools.calculateUnitTangentVectorOfCircle(center, G);

        /*Step 6: Calculate t*/
        double t = findT(A, controlPointA, controlPointB, B, H, G,
                MathTools.EPSILON, startT, endT);

        /*Step 7: Calculate d*/
        DoublePoint Q_t = MathTools.calculatePointOnBezierCurve(
                A, controlPointA, controlPointB, B, t);
        double d = MathTools.euclideanDistance(Q_t, G);

        /*Step 8: Judge if the current approximation meets the allowable error*/
        if (d <= allowableError) {

            Arc arc = new Arc(center, radius, 0.0, 0.0);
            arcs.add(arc);

        } else {

            cubicBezierCurveToArcsHelper(
                    A, controlPointA, controlPointB, B, startT, t,
                    arcs, allowableError);
            cubicBezierCurveToArcsHelper(
                    A, controlPointA, controlPointB, B, t, endT,
                    arcs, allowableError);
        }
    }

    private static double findT(final DoublePoint A, final DoublePoint controlPointA,
                                final DoublePoint controlPointB, final DoublePoint B,
                                final DoublePoint H, final DoublePoint G,
                                final double epsilon, double startT, double endT) {

        while (true) {

            double middleT = startT + (endT - startT) / 2.0;
            DoublePoint Q_t = MathTools.calculatePointOnBezierCurve(
                    A, controlPointA, controlPointB, B, middleT);
            double f = MathTools.dotProduct(Q_t, H) - MathTools.dotProduct(G, H);

            if (Math.abs(f) <= epsilon) {
                return middleT;
            } else if (f > 0.0) {
                endT = middleT;
            } else {
                startT = middleT;
            }
        }
    }

    public static void main(String[] args) throws Exception {

        String dllPath = "C:\\OpenCV\\opencv\\build\\java\\x64\\opencv_java320.dll";
        System.load(dllPath);

        double allowableError = 0.1;

        String srcImagePath = "E:\\Cpp_Project\\Potrace\\resources\\33.png";
        Mat srcImage = Imgcodecs.imread(srcImagePath);
        boolean[][] matrix = Potrace.bitmapToBinary(srcImage);

        ArrayList<ArrayList<Path>> plistp = new ArrayList<>();
        ArrayList<ArrayList<Curve[]>> ListOfCurveArrays = new ArrayList<>();
        Potrace.potrace_trace(matrix, plistp, ListOfCurveArrays);

        ArrayList<ArrayList<Arc>> arcsList = new ArrayList<>();

        /*for (ArrayList<Curve[]> curveArrayList : ListOfCurveArrays) {
            for (Curve[] curves : curveArrayList) {
                for (Curve curve : curves) {

                    if (curve.getKind() == Curve.CurveKind.Bezier) {
                        DoublePoint A = new DoublePoint(
                                curve.getA().getX(), curve.getA().getY());

                        DoublePoint controlPointA = new DoublePoint(
                                curve.getControlPointA().getX(),
                                curve.getControlPointA().getY());

                        DoublePoint controlPointB = new DoublePoint(
                                curve.getControlPointB().getX(),
                                curve.getControlPointB().getY());

                        DoublePoint B = new DoublePoint(
                                curve.getB().getX(), curve.getB().getY());

                        CubicBezierCurve bezierCurve = new CubicBezierCurve(
                                A, controlPointA, controlPointB, B);

                        ArrayList<Arc> arcs = new ArrayList<>();
                        cubicBezierCurveToArcs(bezierCurve, arcs, allowableError);
                        arcsList.add(arcs);
                    }
                }
            }
        }*/

        WriteFile.WriteCurveAndFittedCircle(
                "2.html", "hahaha",
                ListOfCurveArrays, "#FF0000",
                arcsList, "#0000FF");

        System.out.println("Completed");
    }
}
