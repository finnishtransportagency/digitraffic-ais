package fi.livi.digitraffic.meri.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.geometric.PGpoint;
import org.postgresql.geometric.PGpolygon;

import fi.livi.digitraffic.meri.domain.Point;
import fi.livi.digitraffic.meri.domain.Polygon;

public class PolygonUserType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.OTHER };
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
        final List<Point> points = Arrays.stream(((PGpolygon) value).points).map(p -> new Point(p.x, p.y)).collect(Collectors.toList());
        return new Polygon(points);
    }
    @Override
    public final void nullSafeSet(final PreparedStatement st,
                                  final Object value,
                                  final int index,
                                  final SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            final Polygon polygon = (Polygon) value;
            st.setObject(index, new PGpolygon(polygon.points.stream().map(p -> new PGpoint(p.longitude, p.latitude)).toArray(PGpoint[]::new)));
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
}
