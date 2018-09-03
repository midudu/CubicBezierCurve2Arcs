package test;

import bezierCurveToArcs.BezierCurveToArcs;
import component.Arc;
import component.DoublePoint;
import writeSVGFile.WriteSVGFile;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is to test the {@code BezierCurveToArcs} class and show examples
 * of how to use the methods in {@code BezierCurveToArcs}.
 */
public class TestBezierCurveToArcs {

    public static void main(String[] args) throws IOException {

        DoublePoint A = new DoublePoint(816.5, 24);
        DoublePoint controlPointA = new DoublePoint(815, 22);
        DoublePoint controlPointB = new DoublePoint(815, 22);
        DoublePoint B = new DoublePoint(815.5, 24);

        ArrayList<Arc> arcs = new ArrayList<>();

        BezierCurveToArcs.convertACubicBezierCurveToArcs(
                A, controlPointA, controlPointB, B, 0.1, arcs);

        double enlargedCoefficient = 1.0;
        ArrayList<Arc> enlargedArcs = new ArrayList<>();
        for (Arc arc : arcs) {
            Arc enlargedArc = new Arc(
                    new DoublePoint(arc.getCenter().getX() * enlargedCoefficient,
                            arc.getCenter().getY() * enlargedCoefficient),
                    arc.getRadius() * enlargedCoefficient,
                    arc.getStartAngle(), arc.getEndAngle(), arc.getClockwiseFlag());
            enlargedArcs.add(enlargedArc);
        }

        DoublePoint enlargedA = new DoublePoint(A.getX() * enlargedCoefficient,
                A.getY() * enlargedCoefficient);
        DoublePoint enlargedControlPointA
                = new DoublePoint(controlPointA.getX() * enlargedCoefficient,
                controlPointA.getY() * enlargedCoefficient);
        DoublePoint enlargedControlPointB
                = new DoublePoint(controlPointB.getX() * enlargedCoefficient,
                controlPointB.getY() * enlargedCoefficient);
        DoublePoint enlargedB = new DoublePoint(B.getX() * enlargedCoefficient,
                B.getY() * enlargedCoefficient);


        String filePath
                = "E:\\Java_Projects\\CubicBezierCurveToArcs\\example\\3.html";

        WriteSVGFile.WriteBezierCurveAndFittedArcs(
                filePath, enlargedA, enlargedControlPointA,
                enlargedControlPointB, enlargedB, "#0000FF",
                enlargedArcs, "#FF0000");
    }
}
