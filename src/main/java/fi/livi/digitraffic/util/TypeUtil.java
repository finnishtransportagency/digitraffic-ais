package fi.livi.digitraffic.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

import javax.xml.datatype.XMLGregorianCalendar;

import fi.livi.digitraffic.meri.portnet.xsd.TimeSource;

public final class TypeUtil {
    private TypeUtil() {

    }

    public static Integer getInteger(final BigInteger bi) {
        return bi == null ? null : bi.intValue();
    }

    public static Double getDouble(final BigDecimal bd) {
        return bd == null ? null : bd.doubleValue();
    }

    public static String getEnum(final Enum e) {
        return e == null ? null : e.name();
    }

    public static String getSource(final TimeSource tc) {
        return tc == null ? null : tc.value();
    }

    public static Timestamp getTimestamp(final XMLGregorianCalendar cal) {
        return cal == null ? null : new Timestamp(cal.toGregorianCalendar().getTimeInMillis());
    }
}
