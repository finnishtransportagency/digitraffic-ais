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
    public final Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public final Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public final boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if ((x == null) || (y == null)) {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public final int hashCode(Object x) throws HibernateException {
        return (x != null) ? x.hashCode() : 0;
    }

    @Override
    public Object nullSafeGet(final ResultSet rs,
                              final String[] names,
                              final SharedSessionContractImplementor session,
                              final Object owner) throws HibernateException, SQLException {
        Object value = rs.getObject(names[0]);
        if (value == null) {
            return null;
        }
        List<Point> points = Arrays.stream(((PGpolygon) value).points).map(p -> new Point(p.x, p.y)).collect(Collectors.toList());
        return new Polygon(points);
    }
    @Override
    public final void nullSafeSet(PreparedStatement st,
                                  Object value,
                                  int index,
                                  SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            Polygon polygon = (Polygon) value;
            st.setObject(index, new PGpolygon(polygon.points.stream().map(p -> new PGpoint(p.longitude, p.latitude)).toArray(PGpoint[]::new)));
        }
    }

    @Override
    public final boolean isMutable() {
        return false;
    }

    @Override
    public final Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public final Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }
}
