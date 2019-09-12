package fi.livi.digitraffic.meri.domain;

import java.util.List;

public class MultiPolygon implements Geometry {

    public final List<List<List<Point>>> points;

    public MultiPolygon(List<List<List<Point>>> points) {
        this.points = points;
    }

}
