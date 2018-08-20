package writeSVGFile;

import component.Arc;
import component.DoublePoint;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.ArrayList;

/**
 * This class contains methods to write files which contains svg element.
 */
public class WriteSVGFile {

    public static void WriteBezierCurveAndFittedArcs(
            String dstFilePath,
            DoublePoint A, DoublePoint controlPointA,
            DoublePoint controlPointB, DoublePoint B, String colorOfBezier,
            ArrayList<Arc> arcs, String colorOfArcs) throws IOException {

        if (!dstFilePath.contains(".html")
                || dstFilePath.indexOf(".html") != dstFilePath.length() - 5) {

            System.out.println("Check File Path!");
            return;
        }

        if (!judgeIfColorStringMeetsCondition(colorOfBezier)
                || !judgeIfColorStringMeetsCondition(colorOfArcs)) {

            System.out.println("Check Color Format!");
            return;
        }


        Files.deleteIfExists(Paths.get(dstFilePath));

        OutputStream outputStream
                = Files.newOutputStream(Paths.get(dstFilePath), StandardOpenOption.CREATE);

        WriteHeaderPartForHtmlWithInlineSvg(outputStream);

        WriteBezierCurveForHtmlWithInlineSvg(
                outputStream, A, controlPointA, controlPointB, B, colorOfBezier);

        WriteArcsForHtmlWithInlineSvg(outputStream, arcs, colorOfArcs);

        WriteTailPartForHtmlWithInlineSvg(outputStream);

        outputStream.close();

        System.out.println("Completed!");
    }

    private static boolean judgeIfColorStringMeetsCondition(String color) {

        if (color == null || color.length() != 7 || color.charAt(0) != '#') {
            return false;
        }

        for (int i = 1; i < 7; i++) {

            char currentChar = color.charAt(i);

            if (!((currentChar >= '0' && currentChar <= '9')
                    || (currentChar >= 'A' && currentChar <= 'F'))) {
                return false;
            }
        }

        return true;
    }

    private static void WriteHeaderPartForHtmlWithInlineSvg(
            OutputStream outputStream) throws IOException {

        String content = "<html>\n"
                + "  <head>\n"
                + "    <script src=\"./svg-pan-zoom.js\"></script>\n"
                + "  </head>\n"
                + "  <body>\n"
                + "    <div id=\"container\" "
                + "style=\"width:650px; height:650px; border:1px solid black;\">\n"
                + "      <svg id=\"demo\" "
                + "xmlns=\"http://www.w3.org/2000/svg\" "
                + "style=\"display:inline; width:inherit; min-width:inherit; max-width:inherit; "
                + "height:inherit; min-height:inherit; max-height:inherit;\" "
                + "viewBox=\"0 0 900 900\" "
                + "version=\"1.1\">\n"
                + "        <g id=\"g4\" fill=\"none\" "
                + "transform=\"matrix(1.7656463, 0, 0, 1.7656463, 324.90716, 255.00942)\">\n";

        outputStream.write(content.getBytes());
        outputStream.flush();
    }

    private static void WriteBezierCurveForHtmlWithInlineSvg(
            OutputStream outputStream, DoublePoint A, DoublePoint controlPointA,
            DoublePoint controlPointB, DoublePoint B, String colorOfBezier)
            throws IOException {

        String content = "           <path d=\"\n"
                + "                    M "
                + String.valueOf(A.getX()) + " "
                + String.valueOf(A.getY()) + "\n"
                + "                    C "
                + String.valueOf(controlPointA.getX()) + " "
                + String.valueOf(controlPointA.getY()) + " "
                + String.valueOf(controlPointB.getX()) + " "
                + String.valueOf(controlPointB.getY()) + " "
                + String.valueOf(B.getX()) + " "
                + String.valueOf(B.getY()) + "\n"
                + "                    \" stroke=\""
                + colorOfBezier
                + "\" fill=\"none\" stroke-width=\"1px\"/>\n";

        outputStream.write(content.getBytes());
        outputStream.flush();
    }

    private static void WriteArcsForHtmlWithInlineSvg(
            OutputStream outputStream, ArrayList<Arc> arcs, String colorOfArcs)
            throws IOException {

        String content = "           <path d=\"\n";

        double startXPosition
                = arcs.get(0).getCenter().getX()
                + arcs.get(0).getRadius() * Math.cos(arcs.get(0).getStartAngle());

        double startYPosition
                = arcs.get(0).getCenter().getY()
                + arcs.get(0).getRadius() * Math.sin(arcs.get(0).getStartAngle());

        content += ("                    M " + String.valueOf(startXPosition) + " "
                + String.valueOf(startYPosition) + "\n");

        for (Arc arc : arcs) {

            double endXPosition
                    = arc.getCenter().getX()
                    + arc.getRadius() * Math.cos(arc.getEndAngle());

            double endYPosition
                    = arc.getCenter().getY()
                    + arc.getRadius() * Math.sin(arc.getEndAngle());

            content += ("                    A " + arc.getRadius() + " "
                    + arc.getRadius() + " 0 0 ");

            if (arc.getClockwiseFlag()) {
                content += "1 ";
            } else {
                content += "0 ";
            }

            content += String.valueOf(endXPosition) + " "
                    + String.valueOf(endYPosition) + "\n";
        }

        content += "                    \" stroke=\""
                + colorOfArcs
                + "\" fill=\"none\" stroke-width=\"1px\"/>\n";

        outputStream.write(content.getBytes());
        outputStream.flush();
    }

    private static void WriteTailPartForHtmlWithInlineSvg(
            OutputStream outputStream) throws IOException {

        String content = "        </g>\n"
                + "      </svg>\n"
                + "    </div>\n"
                + "\n\n"
                + "    <button id=\"enable\">enable</button>\n"
                + "    <button id=\"disable\">disable</button>\n"
                + "\n\n"
                + "    <script>\n"
                + "      window.onload = function() {\n"
                + "        window.zoomTiger = svgPanZoom('#demo', {\n"
                + "          zoomEnabled: true,\n"
                + "          controlIconsEnabled: true,\n"
                + "          fit: true,\n"
                + "          center: true,\n"
                + "        });\n"
                + "\n"
                + "        document.getElementById('enable').addEventListener(" +
                "'click', function() {\n"
                + "          window.zoomTiger.enableControlIcons();\n"
                + "        })\n"
                + "        document.getElementById('disable').addEventListener(" +
                "'click', function() {\n"
                + "          window.zoomTiger.disableControlIcons();\n"
                + "        })\n"
                + "      };\n"
                + "    </script>\n"
                + "  </body>\n"
                + "</html>";

        outputStream.write(content.getBytes());
        outputStream.flush();
    }
}
