package fi.livi.digitraffic.meri.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.postgis.*;

public class GeometryUserType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.OTHER};
    }

    @Override
    public Class returnedClass() {
        return Polygon.class;
    }

    @Override
    public final Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
        return original;
    }

    @Override
    public final Object deepCopy(final Object value) throws HibernateException {
        return value;
    }

    @Override
    public final boolean equals(final Object x, final Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if ((x == null) || (y == null)) {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public final int hashCode(final Object x) throws HibernateException {
        return (x != null) ? x.hashCode() : 0;
    }

    @Override
    public Object nullSafeGet(final ResultSet rs,
                              final String[] names,
                              final SharedSessionContractImplementor session,
                              final Object owner) throws HibernateException, SQLException {
        final Object value = rs.getObject(names[0]);
        if (value == null) {
            return null;
        }
        final Geometry geometry = ((PGgeometry) value).getGeometry();

        if (geometry instanceof GeometryCollection) {
            // assume no nested geometrycollections
            return new fi.livi.digitraffic.meri.domain.GeometryCollection(
                Arrays.stream(((GeometryCollection) geometry).getGeometries()).map(this::toGeometry).collect(Collectors.toList()));
        }
        return toGeometry(geometry);
    }

    @Override
    public final void nullSafeSet(final PreparedStatement st,
                                  final Object value,
                                  final int index,
                                  final SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            //final Polygon polygon = (Polygon) value;
            //    st.setObject(index, new PGpolygon(polygon.points.stream().map(p -> new PGpoint(p.longitude, p.latitude)).toArray(PGpoint[]::new)));
        }
    }

    @Override
    public final boolean isMutable() {
        return false;
    }

    @Override
    public final Object assemble(final Serializable cached, final Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public final Serializable disassemble(final Object value) throws HibernateException {
        return (Serializable) value;
    }

    private fi.livi.digitraffic.meri.domain.Point toPoint(Point point) {
        return new fi.livi.digitraffic.meri.domain.Point(point.x, point.y);
    }

    private List<List<fi.livi.digitraffic.meri.domain.Point>> getLinearRings(Polygon polygon) {
        List<List<fi.livi.digitraffic.meri.domain.Point>> linearRings = new ArrayList<>();
        for (int i = 0; i < polygon.numRings(); i++) {
            final List<fi.livi.digitraffic.meri.domain.Point> points = Arrays.stream(polygon.getRing(i).getPoints()).map(this::toPoint).collect(Collectors.toList());
            linearRings.add(points);
        }
        return linearRings;
    }

    private fi.livi.digitraffic.meri.domain.Geometry toGeometry(Geometry geometry) {
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
        throw new IllegalArgumentException("Unknown geometry type: " + geometry.getType());
    }
}
