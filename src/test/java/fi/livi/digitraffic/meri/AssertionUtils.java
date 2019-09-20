package fi.livi.digitraffic.meri;

import java.util.Collection;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.Assert.assertEquals;

public final class AssertionUtils {

    public static void assertPositive(int num) {
        assertTrue(num > 0, "Number was less than or equal to zero: " + num);
    }

    public static void assertSize(Collection<?> coll, int expectedSize) {
        assertEquals("Collection sizes did not match, expected " + expectedSize + ", actual " + coll.size(), expectedSize, coll.size());
    }

}
