package fi.livi.digitraffic.meri.controller;

import fi.livi.digitraffic.meri.AbstractDatabaseTestBase;
import fi.livi.digitraffic.meri.config.MarineApplicationConfiguration;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.Random;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BridgeLockDisruptionControllerTest extends AbstractDatabaseTestBase {

    private static final Random random = new Random(System.nanoTime());

    @Test
    public void noDisruptions() throws Exception {
        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
            BridgeLockDisruptionController.PATH))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.features").isEmpty());
    }

    @Test
    public void disruptions() throws Exception {
        createSomeDisruptions();

        mockMvc.perform(get(MarineApplicationConfiguration.API_V1_BASE_PATH +
                BridgeLockDisruptionController.PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.features").isNotEmpty());
    }

    private void createSomeDisruptions() {
        IntStream.of(1 + random.nextInt(10)).forEach(i -> {
            ef.newBridgeLockDisruption();
        });
        ef.persistAll();
    }
}
