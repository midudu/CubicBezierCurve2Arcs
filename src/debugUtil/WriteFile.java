package debugUtil;

import component.Arc;
import potrace.Curve;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class WriteFile {

    private static void WriteHeaderPartForHtmlWithInlineSvg(
            OutputStream outputStream, String title) throws IOException {

        StringBuilder content = new StringBuilder();

        content.append("<html>\n");
        content.append("  <head>\n");
        content.append("    <script src=\"./svg-pan-zoom.js\"></script>\n");
        content.append("  </head>\n");
        content.append("  <body>\n");
        content.append("    <h1>").append(title).append("</h1>\n");
        content.append("    <div id=\"container\" ").append(
                "style=\"width:650px; height:650px; border:1px solid black;\">\n");
        content.append("      <svg id=\"demo\" ").append(
                "xmlns=\"http://www.w3.org/2000/svg\" ").append(
                "style=\"display:inline; width:inherit; min-width:inherit; max-width:inherit; "
        ).append("height:inherit; min-height:inherit; max-height:inherit;\" ").append(
                "viewBox=\"0 0 900 900\" ").append("version=\"1.1\">\n");
        content.append("        <g id=\"g4\" fill=\"none\" ").append(
                "transform=\"matrix(1.7656463, 0, 0, 1.7656463, 324.90716, 255.00942)\">\n");

        outputStream.write(content.toString().getBytes());
    }

    private static void WriteTailPartForHtmlWithInlineSvg(OutputStream outputStream)
            throws IOException {

        StringBuilder content = new StringBuilder();

        content.append("        </g>\n").append(
                "      </svg>\n").append(
                "    </div>\n");
        content.append("\n\n");

        content.append("    <button id=\"enable\">enable</button>\n");
        content.append("    <button id=\"disable\">disable</button>\n");
        content.append("\n\n");

        content.append("    <script>\n").append(
                "      window.onload = function() {\n").append(
                "        window.zoomTiger = svgPanZoom('#demo', {\n"
        ).append("          zoomEnabled: true,\n").append(
                "          controlIconsEnabled: true,\n").append(
                "          fit: true,\n").append(
                "          center: true,\n").append(
                "        });\n").append(
                "\n").append(
                "        document.getElementById('enable').addEventListener('click', function() {\n"
        ).append("          window.zoomTiger.enableControlIcons();\n").append(
                "        })\n").append(
                "        document.getElementById('disable').addEventListener('click', function() {\n"
        ).append("          window.zoomTiger.disableControlIcons();\n").append(
                "        })\n").append(
                "      };\n");

        content.append("    </script>\n").append(
                "  </body>\n").append("</html>");

        outputStream.write(content.toString().getBytes());
    }

    public static void WriteCurveAndFittedCircle(
            String dstFilePath, String title,
            ArrayList<ArrayList<Curve[]>> curve,
            String color1, ArrayList<ArrayList<Arc>> arcs,
            String color2) throws IOException {

        File file = new File(dstFilePath);
        if (file.exists()) {
            boolean deleteResult = file.delete();
            if (!deleteResult) {
                throw new IOException();
            }
        }

        OutputStream outputStream = Files.newOutputStream(Paths.get(dstFilePath),
                StandardOpenOption.CREATE_NEW);

        WriteHeaderPartForHtmlWithInlineSvg(outputStream, title);

        WriteCurveElement(outputStream, curve, color1);

        WriteCircleElement(outputStream, arcs, color2);

        WriteTailPartForHtmlWithInlineSvg(outputStream);

        outputStream.flush();
        outputStream.close();
    }

    private static void WriteCurveElement(
            OutputStream outputStream,
            ArrayList<ArrayList<Curve[]>> listOfCurveArrays, String color)
            throws IOException {

        outputStream.write("  <path d=\"".getBytes());

        Curve[] curves = null;
        String currentCommand = null;

        for (int i = 0; i < listOfCurveArrays.size(); i++) {
            for (int j = 0; j < listOfCurveArrays.get(i).size(); j++) {

                curves = listOfCurveArrays.get(i).get(j);

                if (curves == null || curves.length == 0) {
                    continue;
                }

                currentCommand = "M "
                        + curves[0].getA().getY() + " "
                        + curves[0].getA().getX() + "\n";
                outputStream.write(currentCommand.getBytes());

                for (int k = 0; k < curves.length; k++) {

                    if (curves[k].getKind() == Curve.CurveKind.Line) {

                        currentCommand = "           L "
                                + curves[k].getB().getY() + " "
                                + curves[k].getB().getX() + "\n";
                        outputStream.write(currentCommand.getBytes());

                    } else if (curves[k].getKind() == Curve.CurveKind.Bezier) {

                        currentCommand = "           C "
                                + curves[k].getControlPointA().getY() + " "
                                + curves[k].getControlPointA().getX() + " "
                                + curves[k].getControlPointB().getY() + " "
                                + curves[k].getControlPointB().getX() + " "
                                + curves[k].getB().getY() + " "
                                + curves[k].getB().getX() + "\n";
                        outputStream.write(currentCommand.getBytes());
                    }
                }
            }
        }

        String string = "           Z\" stroke=\"" + color + "\" fill=\"none\"/>\n";
        outputStream.write(string.getBytes());
    }

    private static void WriteCircleElement(
            OutputStream outputStream, ArrayList<ArrayList<Arc>> arcs,
            String color) throws IOException {

        /*<circle cx="100" cy="50" r="40" stroke="black"
        stroke-width="2" fill="red"/>*/

        for (ArrayList<Arc> arcArrayList : arcs) {
            for (Arc arc : arcArrayList) {

                String content = "<circle cx=\"";
                content += arc.center.x;
                content += "\" cy=\"";
                content += arc.center.y;
                content += " r=";
                content += arc.radius;
                content += "\" stroke=\"";
                content += color;
                content += "\" stroke-width=\"2\" fill=\"red\"/>";
                outputStream.write(content.getBytes());
            }
        }
    }
}
