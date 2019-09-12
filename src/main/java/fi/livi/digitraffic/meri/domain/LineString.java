package fi.livi.digitraffic.meri.domain;

import java.util.List;

public class LineString implements Geometry {

    public final List<Point> points;

    public LineString(List<Point> points) {
        this.points = points;
    }

}
