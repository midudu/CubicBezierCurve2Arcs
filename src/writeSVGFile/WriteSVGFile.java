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

    /**
     * To write the original Bezier curve and the fitted arcs in an .html file
     * with inline svg element.
     *
     * @param targetFilePath the target file path which must end with ".html"
     * @param A              the start point of the Bezier curve
     * @param controlPointA  the first control point of the Bezier curve which
     *                       is close to {@code A}
     * @param controlPointB  the second control point of the Bezier curve which
     *                       is close to {@code B}
     * @param B              the end point of the Bezier curve
     * @param colorOfBezier  the color of the Bezier curve to be painted. The
     *                       format is like "#0000FF". The {@code colorOfBezier}
     *                       string must begin with '#' and have another 6
     *                       characters in '0'-'9','A','B','C','D','E' and 'F'.
     *                       Every two characters make up of a red, green or
     *                       blue value. The first two characters represent the
     *                       value of red; the middle two characters represent
     *                       the value of green and the last two characters
     *                       represent the value of blue.
     * @param arcs           a series of fitted curves
     * @param colorOfArcs    the color of the fitted arcs to be painted of which
     *                       the format is similar to {@code colorOfBezier}
     * @throws IOException when problems are met during file output
     */
    public static void WriteBezierCurveAndFittedArcs(
            String targetFilePath,
            DoublePoint A, DoublePoint controlPointA,
            DoublePoint controlPointB, DoublePoint B, String colorOfBezier,
            ArrayList<Arc> arcs, String colorOfArcs) throws IOException {

        if (!targetFilePath.contains(".html")
                || targetFilePath.indexOf(".html") != targetFilePath.length() - 5) {

            System.out.println("Check File Path!");
            return;
        }

        if (!judgeIfColorStringHasCorrectFormat(colorOfBezier)
                || !judgeIfColorStringHasCorrectFormat(colorOfArcs)) {

            System.out.println("Check Color Format!");
            return;
        }


        Files.deleteIfExists(Paths.get(targetFilePath));
        OutputStream outputStream
                = Files.newOutputStream(Paths.get(targetFilePath), StandardOpenOption.CREATE);

        /* Write header part */
        WriteHeaderPartForHtmlWithInlineSvg(outputStream);

        /* Write Bezier curve */
        WriteBezierCurveForHtmlWithInlineSvg(
                outputStream, A, controlPointA, controlPointB, B, colorOfBezier);

        /* Write fitted arcs */
        WriteArcsForHtmlWithInlineSvg(outputStream, arcs, colorOfArcs);

        /* Write tail part */
        WriteTailPartForHtmlWithInlineSvg(outputStream);

        outputStream.close();

        System.out.println("Completed!");
    }

    /**
     * To judge if the input parameter has a correct format.
     *
     * @param color a {@code String} object which represent the color
     * @return true for correct and false for wrong
     */
    private static boolean judgeIfColorStringHasCorrectFormat(String color) {

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

    /**
     * To write the header part of the .html file.
     *
     * @param outputStream an {@code OutputStream} object for output
     * @throws IOException when problems are met during file output
     */
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

    /**
     * To write a Bezier curve in a svg element.
     *
     * @param outputStream  an {@code OutputStream} object for output
     * @param A             the start point of the Bezier curve
     * @param controlPointA the first control point of the Bezier curve which
     *                      is close to {@code A}
     * @param controlPointB the second control point of the Bezier curve which
     *                      is close to {@code B}
     * @param B             the end point of the Bezier curve
     * @param colorOfBezier the color of the Bezier curve to be painted. The
     *                      format is like "#0000FF". The {@code colorOfBezier}
     *                      string must begin with '#' and have another 6
     *                      characters in '0'-'9','A','B','C','D','E' and 'F'.
     *                      Every two characters make up of a red, green or
     *                      blue value. The first two characters represent the
     *                      value of red; the middle two characters represent
     *                      the value of green and the last two characters
     *                      represent the value of blue.
     * @throws IOException when problems are met during file output
     */
    private static void WriteBezierCurveForHtmlWithInlineSvg(
            OutputStream outputStream, DoublePoint A, DoublePoint controlPointA,
            DoublePoint controlPointB, DoublePoint B, String colorOfBezier)
            throws IOException {

        if (A == null || controlPointA == null
                || controlPointB == null || B == null) {
            return;
        }

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

    /**
     * To write arcs in a svg element.
     *
     * @param outputStream an {@code OutputStream} object for output
     * @param arcs         a series of fitted arcs
     * @param colorOfArcs  the color of the fitted arcs to be painted. The
     *                     format is like "#0000FF". The {@code colorOfBezier}
     *                     string must begin with '#' and have another 6
     *                     characters in '0'-'9','A','B','C','D','E' and 'F'.
     *                     Every two characters make up of a red, green or
     *                     blue value. The first two characters represent the
     *                     value of red; the middle two characters represent
     *                     the value of green and the last two characters
     *                     represent the value of blue.
     * @throws IOException when problems are met during file output
     */
    private static void WriteArcsForHtmlWithInlineSvg(
            OutputStream outputStream, ArrayList<Arc> arcs, String colorOfArcs)
            throws IOException {

        if (arcs == null || arcs.isEmpty()) {
            return;
        }

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
                content += "0 ";
            } else {
                content += "1 ";
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

    /**
     * To write the tail part of the .html file.
     *
     * @param outputStream an {@code OutputStream} object for output
     * @throws IOException when problems are met during file output
     */
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
