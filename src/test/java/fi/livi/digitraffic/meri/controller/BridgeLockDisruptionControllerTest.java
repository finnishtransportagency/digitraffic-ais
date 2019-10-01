package fi.livi.digitraffic.meri.controller;

import fi.livi.digitraffic.meri.AbstractDatabaseTestBase;
import fi.livi.digitraffic.meri.config.MarineApplicationConfiguration;
import fi.livi.digitraffic.meri.domain.bridgelock.BridgeLockDisruption;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static fi.livi.digitraffic.meri.TestStreamUtils.randomRange;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BridgeLockDisruptionControllerTest extends AbstractDatabaseTestBase {

    @Test
    public void disruptions() throws Exception {
        final List<BridgeLockDisruption> disruptions = createSomeDisruptions();

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                BridgeLockDisruptionController.PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.features", hasSize(disruptions.size())));
    }

    private List<BridgeLockDisruption> createSomeDisruptions() {
        final List<BridgeLockDisruption> disruptions = randomRange(10)
            .mapToObj(i -> objectMother.newBridgeLockDisruption()).collect(Collectors.toList());
        objectMother.persistAll();
        return disruptions;
    }
}
