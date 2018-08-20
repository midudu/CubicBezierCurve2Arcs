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

        DoublePoint A = new DoublePoint(40, 112.5);
        DoublePoint controlPointA = new DoublePoint(62, 212.775);
        DoublePoint controlPointB = new DoublePoint(80.225, 213.225);
        DoublePoint B = new DoublePoint(80.5, 113.5);

        ArrayList<Arc> arcs = new ArrayList<>();
        BezierCurveToArcs.convertACubicBezierCurveToArcs(
                A, controlPointA, controlPointB, B, 0.1, arcs);

        String filePath
                = "E:\\Java_Projects\\CubicBezierCurveToArcs\\example\\1.html";

        WriteSVGFile.WriteBezierCurveAndFittedArcs(
                filePath, A, controlPointA, controlPointB, B, "#0000FF",
                arcs, "#FF0000");
    }
}
