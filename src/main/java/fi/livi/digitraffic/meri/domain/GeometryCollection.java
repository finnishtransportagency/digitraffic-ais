package fi.livi.digitraffic.meri.domain;

import java.util.List;

public class GeometryCollection implements Geometry {

    public final List<Geometry> geometries;

    public GeometryCollection(List<Geometry> geometries) {
        this.geometries = geometries;
    }
}
