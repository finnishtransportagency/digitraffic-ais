package fi.livi.digitraffic.meri;

import fi.livi.digitraffic.meri.domain.*;
import fi.livi.digitraffic.meri.domain.bridgelock.BridgeLockDisruption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static fi.livi.digitraffic.meri.TestStreamUtils.randomRange;

@Component
public class ObjectMother {

    private static final Random random = new Random(System.nanoTime());

    private final List<EntityBase> entities = new ArrayList<>();

    @Autowired
    private EntityManager em;

    public BridgeLockDisruption newBridgeLockDisruption() {
        final BridgeLockDisruption bld = new BridgeLockDisruption(random.nextLong(),
            random.nextLong(),
            ZonedDateTime.now(),
            ZonedDateTime.now().plusHours(random.nextInt(50)),
            randomGeometry(),
            randomString(),
            randomString(),
            randomString(),
            randomString(),
            randomString(),
            randomString());
        entities.add(bld);
        return bld;
    }

    /**
     * Truncate tables in dependent order
     */
    @Transactional
    public void truncateDatabase() {
        em.createNativeQuery("DELETE FROM BRIDGELOCK_DISRUPTION").executeUpdate();
    }

    @Transactional
    public void persistAll() {
        entities.forEach(e -> em.persist(e));
        em.flush();
    }

    private String randomString() {
        return UUID.randomUUID().toString();
    }

    // TODO add more geometry types
    private Geometry randomGeometry() {
        List<Class<? extends Geometry>> geometryTypes = Arrays.asList(LinearRing.class,
            LineString.class,
            MultiLineString.class,
            MultiPoint.class,
            MultiPolygon.class,
            Point.class,
            Polygon.class);
        Collections.shuffle(geometryTypes);
        final Class<? extends Geometry> geometryType = geometryTypes.iterator().next();
        if (geometryType == LineString.class) {
            return new LineString(randomPoints());
        } else if (geometryType == MultiLineString.class) {
            return new MultiLineString(randomListsOfPoints());
        } else if (geometryType == MultiPoint.class) {
            return new MultiPoint(randomPoints());
        }
        return randomPoint();
    }

    private List<List<Point>> randomListsOfPoints() {
        return randomRange(10).mapToObj(i -> randomPoints()).collect(Collectors.toList());
    }

    private List<Point> randomPoints() {
        return randomRange(10).mapToObj(i -> randomPoint()).collect(Collectors.toList());
    }

    private Point randomPoint() {
        return new Point(1 + random.nextInt(10000), 1 + random.nextInt(10000));
    }

}
