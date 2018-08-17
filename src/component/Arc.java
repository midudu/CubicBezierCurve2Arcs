package component;

/**
 * This class is to store an arc object. There are 5 properties to determine an
 * arc: center, radius, start angle, end angle and direction.
 * <p>
 * Note that the start angle lies in the range [-pi, pi], and the end angle
 * lies in the range [start angle - pi, start angle + pi].
 */
public class Arc {

    /* the center of the circle which the arc is on */
    private DoublePoint center;

    /* the radius of the circle which the arc is on */
    private double radius;

    /* the start angle of the arc which lies in the range [-pi, pi] */
    private double startAngle;

    /* the end angle of the arc which lies in the range
       [{@code startAngle} - pi, {@code startAngle} + pi] */
    private double endAngle;

    /* a variable to determine the direction of the arc.
       True for clockwise and false for anti-clockwise */
    private boolean clockwiseFlag;

    /**
     * Constructor
     *
     * @param center        the center of the circle which the arc is on
     * @param radius        the radius of the circle which the arc is on
     * @param startAngle    the start angle of the arc which lies in the range
     *                      [-pi, pi]
     * @param endAngle      the end angle of the arc which lies in the range
     *                      [{@code startAngle} - pi, {@code startAngle} + pi]
     * @param clockwiseFlag the direction of the arc.
     *                      True for clockwise and false for anti-clockwise
     */
    public Arc(DoublePoint center, double radius,
               double startAngle, double endAngle, boolean clockwiseFlag) {

        this.center = center;
        this.radius = radius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.clockwiseFlag = clockwiseFlag;
    }

    /**
     * To get this.endAngle.
     *
     * @return this.endAngle
     */
    public double getEndAngle() {
        return endAngle;
    }

    /**
     * To get this.radius.
     *
     * @return this.radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * To get this.startAngle.
     *
     * @return this.startAngle
     */
    public double getStartAngle() {
        return startAngle;
    }

    /**
     * To get this.center.
     *
     * @return this.center
     */
    public DoublePoint getCenter() {
        return center;
    }

    /**
     * To get this.clockwiseFlag.
     *
     * @return this.clockwiseFlag
     */
    public boolean getClockwiseFlag() {
        return clockwiseFlag;
    }
}
