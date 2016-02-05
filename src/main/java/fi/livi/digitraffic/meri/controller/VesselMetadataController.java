package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_METADATA_PART_PATH;
import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.VesselMetadata;

@RestController
public class VesselMetadataController {
    @RequestMapping(method = RequestMethod.GET, path = API_V1_BASE_PATH + API_METADATA_PART_PATH + "/vessels/{mmsi}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody public VesselMetadata vesselMetadataByMssi(@PathVariable("mmsi") final int mmsi) {
        return new VesselMetadata(mmsi, "testi");
    }

    @RequestMapping(method = RequestMethod.GET, path = API_V1_BASE_PATH + API_METADATA_PART_PATH + "/vessels",
            produces = MediaType .APPLICATION_JSON_VALUE)
    @ResponseBody public List<VesselMetadata> allVessels() {
        return Arrays.asList(
                new VesselMetadata(123, "testi"),
                new VesselMetadata(1234, "testi2")
        );
    }
}
