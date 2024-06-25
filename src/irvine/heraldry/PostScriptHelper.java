package irvine.heraldry;

/**
 * Utility functions to help with PostScript rendering.
 * @author Sean A. Irvine
 */
final class PostScriptHelper {

  private PostScriptHelper() {
  }

  private static String path(final double[] x, final double[] y, final String op) {
    assert x.length == y.length;
    final StringBuilder sb = new StringBuilder();
    if (x.length > 1) {
      sb.append("newpath ").append(x[0]).append(' ').append(y[0]).append(" moveto");
      for (int k = 1; k < x.length; ++k) {
        sb.append(' ')
          .append(x[k])
          .append(' ')
          .append(y[k])
          .append(" lineto");
      }
      sb.append(" closepath ").append(op);
    }
    return sb.toString();
  }

  static String fill(final double[] x, final double[] y) {
    return path(x, y, "fill");
  }

  static String stroke(final double[] x, final double[] y) {
    return path(x, y, "stroke");
  }

  static String clip(final double[] x, final double[] y) {
    return path(x, y, "clip");
  }

  static String scale(final double width, final double height) {
    return "1 " + (height / width) + " scale";
  }

  private static String pathOval(final double width, final double height, final String op) {
    final double a = width / 2;
    return "gsave " + scale(width, height) + " newpath " + a + " " + a + " " + a + " 0 360 arc " + op + " grestore";
  }

  static String fillOval(final double width, final double height) {
    return pathOval(width, height, "fill");
  }

  static String drawOval(final double width, final double height) {
    return pathOval(width, height, "stroke");
  }

  private static String pathArc(final double x, final double y, final double r, final double start, final double end, final String op) {
    return "newpath " + x + " " + y + " " + r + " " + start + " " + end + " arc " + op;
  }

  static String fillArc(final double x, final double y, final double r, final double start, final double end) {
    return pathArc(x, y, r, start, end, x + " " + y + " lineto closepath fill");
  }

  static String drawArc(final double x, final double y, final double r, final double start, final double end) {
    return pathArc(x, y, r, start, end, "stroke");
  }

  static String clipArc(final double x, final double y, final double r, final double start, final double end) {
    return pathArc(x, y, r, start, end, "clip");
  }

  static String fillChord(final double x, final double y, final double r, final double start, final double end) {
    return pathArc(x, y, r, start, end, "fill");
  }

  static String fillRect(final double x, final double y, final double w, final double h) {
    return x + " " + y + " " + w + " " + h + " rectfill";
  }

  static String clipRect(final double x, final double y, final double w, final double h) {
    return x + " " + y + " " + w + " " + h + " rectclip";
  }


}
