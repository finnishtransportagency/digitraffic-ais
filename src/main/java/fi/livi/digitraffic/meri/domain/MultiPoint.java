package fi.livi.digitraffic.meri.domain;

import java.util.List;

public class MultiPoint implements Geometry {

    public final List<Point> points;

    public MultiPoint(List<Point> points) {
        this.points = points;
    }

}
