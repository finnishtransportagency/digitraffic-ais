package fi.livi.digitraffic.meri.domain;

import java.util.List;

public class MultiLineString implements Geometry {

    public final List<List<Point>> points;

    public MultiLineString(List<List<Point>> points) {
        this.points = points;
    }

}
