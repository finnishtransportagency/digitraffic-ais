package fi.livi.digitraffic.meri.domain;

import java.util.List;

public class LinearRing implements Geometry {

    public final List<Point> points;

    public LinearRing(List<Point> points) {
        this.points = points;
    }

}
