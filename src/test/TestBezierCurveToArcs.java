package test;

import bezierCurveToArcs.BezierCurveToArcs;
import component.Arc;
import component.DoublePoint;

import java.util.ArrayList;

public class TestBezierCurveToArcs {

    public static void main(String[] args) {

        DoublePoint A = new DoublePoint(40, 112.5);
        DoublePoint controlPointA = new DoublePoint(62, 112.775);
        DoublePoint controlPointB = new DoublePoint(80.225, 113.225);
        DoublePoint B = new DoublePoint(80.5, 113.5);

        ArrayList<Arc> arcs = new ArrayList<>();
        BezierCurveToArcs.convertACubicBezierCurveToArcs(
                A, controlPointA, controlPointB, B, 0.1, arcs);

        System.out.println("Completed!");
    }
}
