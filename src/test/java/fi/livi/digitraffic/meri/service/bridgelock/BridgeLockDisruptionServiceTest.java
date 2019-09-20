package fi.livi.digitraffic.meri.service.bridgelock;

import fi.livi.digitraffic.meri.AbstractDatabaseTestBase;
import fi.livi.digitraffic.meri.StreamUtils;
import fi.livi.digitraffic.meri.domain.bridgelock.BridgeLockDisruption;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static fi.livi.digitraffic.meri.AssertionUtils.assertSize;

public class BridgeLockDisruptionServiceTest extends AbstractDatabaseTestBase {

    @Autowired
    private BridgeLockDisruptionService bridgeLockDisruptionService;

    @Test
    public void findAll() {
        List<BridgeLockDisruption> disruptions = StreamUtils.randomRange(10)
            .mapToObj(i -> ef.newBridgeLockDisruption()).collect(Collectors.toList());
        ef.persistAll();

        assertSize(bridgeLockDisruptionService.findAll(), disruptions.size());
    }

}
