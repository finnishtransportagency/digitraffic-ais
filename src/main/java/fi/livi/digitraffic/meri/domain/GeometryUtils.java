package fi.livi.digitraffic.meri.domain;

import java.util.List;
import java.util.stream.Collectors;

public class GeometryUtils {

    public static fi.livi.digitraffic.meri.model.geojson.Geometry geometryToGeoJsonGeometry(Geometry geom) {
        if (geom instanceof Point) {
            return new fi.livi.digitraffic.meri.model.geojson.Point(((Point) geom).longitude, ((Point) geom).latitude);
        } else if (geom instanceof MultiPoint) {
            final fi.livi.digitraffic.meri.model.geojson.MultiPoint multiPoint = new fi.livi.digitraffic.meri.model.geojson.MultiPoint();
            multiPoint.setCoordinates(((MultiPoint) geom).points.stream().map(GeometryUtils::toPoint).collect(Collectors.toList()));
            return multiPoint;
        } else if (geom instanceof LineString) {
            final fi.livi.digitraffic.meri.model.geojson.LineString lineString = new fi.livi.digitraffic.meri.model.geojson.LineString();
            lineString.setCoordinates(((LineString) geom).points.stream().map(GeometryUtils::toPoint).collect(Collectors.toList()));
            return lineString;
        } else if (geom instanceof MultiLineString) {
            final fi.livi.digitraffic.meri.model.geojson.MultiLineString multiLineString = new fi.livi.digitraffic.meri.model.geojson.MultiLineString();
            multiLineString.setCoordinates(((MultiLineString) geom).points.stream().map(GeometryUtils::toPoints).collect(Collectors.toList()));
            return multiLineString;
        } else if (geom instanceof LinearRing) {
            final fi.livi.digitraffic.meri.model.geojson.LineString lineString = new fi.livi.digitraffic.meri.model.geojson.LineString();
            lineString.setCoordinates(((LinearRing) geom).points.stream().map(GeometryUtils::toPoint).collect(Collectors.toList()));
            return lineString;
        } else if (geom instanceof Polygon) {
            final fi.livi.digitraffic.meri.model.geojson.Polygon polygon = new fi.livi.digitraffic.meri.model.geojson.Polygon();
            polygon.setCoordinates(((Polygon) geom).points.stream().map(GeometryUtils::toPoints).collect(Collectors.toList()));
            return polygon;
        } else if (geom instanceof MultiPolygon) {
            final fi.livi.digitraffic.meri.model.geojson.MultiPolygon multiPolygon = new fi.livi.digitraffic.meri.model.geojson.MultiPolygon();
            multiPolygon.setCoordinates(((MultiPolygon) geom).points.stream().map(p -> p.stream().map(GeometryUtils::toPoints).collect(Collectors.toList())).collect(Collectors.toList()));
            return multiPolygon;
        } else if (geom instanceof GeometryCollection) {
            throw new IllegalArgumentException("No support for geometry collection yet");
        }
        throw new IllegalArgumentException("Unknown geometry type");
    }

    private static List<List<Double>> toPoints(List<Point> points) {
        return points.stream().map(GeometryUtils::toPoint).collect(Collectors.toList());
    }

    private static List<Double> toPoint(Point point) {
        return List.of(point.longitude, point.latitude);
    }
}
