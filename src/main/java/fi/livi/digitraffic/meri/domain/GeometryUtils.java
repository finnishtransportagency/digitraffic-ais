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
/*
if (geometry instanceof Point) {
            return new fi.livi.digitraffic.meri.domain.Point(((Point) geometry).x, ((Point) geometry).y);
        } else if (geometry instanceof MultiPoint) {
            return new fi.livi.digitraffic.meri.domain.MultiPoint(
                Arrays.stream(((MultiPoint) geometry).getPoints()).map(this::toPoint).collect(Collectors.toList()));
        } else if (geometry instanceof LineString) {
            return new fi.livi.digitraffic.meri.domain.LineString(
                Arrays.stream(((LineString) geometry).getPoints()).map(this::toPoint).collect(Collectors.toList()));
        } else if (geometry instanceof MultiLineString) {
            return new fi.livi.digitraffic.meri.domain.MultiLineString(
                Arrays.stream(((MultiLineString) geometry).getLines()).map(l ->
                    Arrays.stream(l.getPoints()).map(this::toPoint).collect(Collectors.toList())).collect(Collectors.toList()));
        } else if (geometry instanceof LinearRing) {
            return new fi.livi.digitraffic.meri.domain.LinearRing(
                Arrays.stream(((LinearRing) geometry).getPoints()).map(this::toPoint).collect(Collectors.toList()));
        } else if (geometry instanceof Polygon) {
            final Polygon poly = (Polygon) geometry;
            final List<List<fi.livi.digitraffic.meri.domain.Point>> linearRings = getLinearRings((Polygon) geometry);
            return new fi.livi.digitraffic.meri.domain.Polygon(linearRings);
        } else if (geometry instanceof MultiPolygon) {
            return new fi.livi.digitraffic.meri.domain.MultiPolygon(Arrays.stream(((MultiPolygon) geometry).getPolygons()).map(this::getLinearRings).collect(Collectors.toList()));
        }
 */