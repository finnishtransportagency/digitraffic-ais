package fi.livi.digitraffic.meri;

import org.junit.Assert;

import java.util.Random;
import java.util.stream.IntStream;

import static fi.livi.digitraffic.meri.AssertionUtils.assertPositive;

public class StreamUtils {

    private static final Random random = new Random(System.nanoTime());

    /**
     * @param bound A positive integer
     * @return A positive range of integers
     */
    public static IntStream randomRange(int bound) {
        assertPositive(bound);
        return IntStream.range(0, 1 + random.nextInt(bound));
    }
    /**
     * @return A positive range of integers
     */
    public static IntStream randomRange() {
        return randomRange(1 + random.nextInt());
    }

}
