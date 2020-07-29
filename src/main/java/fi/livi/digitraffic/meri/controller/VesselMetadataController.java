package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_METADATA_PART_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.ais.VesselMetadataJson;
import fi.livi.digitraffic.meri.service.ais.VesselMetadataService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_METADATA_PART_PATH)
@ConditionalOnWebApplication
public class VesselMetadataController {
    private final VesselMetadataService vesselMetadataService;

    public static final String VESSELS_PATH =  "/vessels";

    @Autowired
    public VesselMetadataController(final VesselMetadataService vesselMetadataService) {
        this.vesselMetadataService = vesselMetadataService;
    }

    @ApiOperation("Return latest vessel metadata by mmsi.")
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of vessel metadata"),
                    @ApiResponse(code = 404, message = "Vessel metadata not found"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @GetMapping(path = VESSELS_PATH + "/{mmsi}", produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ResponseBody
    public VesselMetadataJson vesselMetadataByMssi(@PathVariable("mmsi") final int mmsi) {
        return vesselMetadataService.findAllowedMetadataByMssi(mmsi);
    }

    @ApiOperation("Return latest vessel metadata for all known vessels.")
    @GetMapping(path = VESSELS_PATH, produces = MediaTypes.MEDIA_TYPE_APPLICATION_JSON)
    @ApiResponses({ @ApiResponse(code = 200, message = "Successful retrieval of vessel metadata"),
                    @ApiResponse(code = 500, message = "Internal server error") })
    @ResponseBody
    public List<VesselMetadataJson> allVessels(@ApiParam("From timestamp timestamp in milliseconds from Unix epoch 1970-01-01T00:00:00Z")
                                               @RequestParam(value = "from", required = false)
                                               final Long from) {
        return vesselMetadataService.findAllowedVesselMetadataFrom(from);
    }
}
