package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_LOCATIONS_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.model.ais.VesselLocationFeatureCollection;
import fi.livi.digitraffic.meri.service.ais.VesselLocationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(API_V1_BASE_PATH + API_LOCATIONS_PATH)
@ConditionalOnWebApplication
public class VesselLocationController {
    private static final Logger LOG = LoggerFactory.getLogger(VesselLocationController.class);

    private final VesselLocationService vesselLocationService;

    public static final String LATEST_PATH =  "/latest";

    @Autowired
    public VesselLocationController(final VesselLocationService vesselLocationService) {
        this.vesselLocationService = vesselLocationService;
    }

    @ApiOperation("Find latest vessel locations by mmsi and optional timestamp interval in milliseconds from Unix epoch.")
    @GetMapping(path = LATEST_PATH + "/{mmsi}", produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                             MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                             MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public VesselLocationFeatureCollection vesselLocationsByMssiAndTimestamp(
            @ApiParam(value = "Maritime Mobile Service Identity (MMSI)", required = true)
            @PathVariable("mmsi")
            final int mmsi,
            @ApiParam("From timestamp timestamp in milliseconds from Unix epoch 1970-01-01T00:00:00Z")
            @RequestParam(value = "from", required = false)
            final Long from,
            @ApiParam("To timestamp")
            @RequestParam(value = "to", required = false)
            final Long to) {

        LOG.info(String.format("vesselLocationsByMssiAndTimestamp mmsi:\t%d from:\t%d to:\t%d", mmsi, from, to));

        return vesselLocationService.findAllowedLocations(mmsi, from, to);
    }

    @ApiOperation("Find latest vessel locations by timestamp interval in milliseconds from Unix epoch.")
    @GetMapping(path = LATEST_PATH, produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                 MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                 MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public VesselLocationFeatureCollection vesselLocationsByTimestamp(
            @ApiParam("From timestamp timestamp in milliseconds from Unix epoch 1970-01-01T00:00:00Z")
            @RequestParam(value = "from", required = false)
            final Long from,
            @ApiParam("To timestamp")
            @RequestParam(value = "to", required = false)
            final Long to) {

        LOG.info(String.format("vesselLocationsByTimestamp from:\t%d to:\t%d", from, to));

        return vesselLocationService.findAllowedLocations(from, to);
    }

    @ApiOperation("Find vessel locations within a circle surrounding a point. " +
                  "(CAUTION: Data is unreliable because it is missing some vessels such as fishing boats, boats with the AIS system turned off, " +
                  "boats without the AIS system, vessels outside of the AIS range and so on.)")
    @GetMapping(path = "latitude/{latitude}/longitude/{longitude}/radius/{radius}/from/{from}", produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                                                                             MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                                                                             MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public VesselLocationFeatureCollection vesselLocationsWithingRadiusFromPoint(
            @ApiParam(value = "Radius of search circle in kilometers (km) using haversine formula.", required = true)
            @PathVariable(value = "radius")
            final double radius,
            @ApiParam(value = "Latitude of the point", required = true)
            @PathVariable(value = "latitude")
            final double latitude,
            @ApiParam(value = "Longitude of the point", required = true)
            @PathVariable(value = "longitude")
            final double longitude,
            @ApiParam(value = "Return vessel locations received after given time in ISO date time format {yyyy-MM-dd'T'HH:mm:ss.SSSZ} e.g" +
                ". 2016-10-31T06:30:00.000Z.", required = true)
            @PathVariable(value = "from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            final ZonedDateTime from) {

        return vesselLocationService.findAllowedLocationsWithinRadiusFromPoint(radius, latitude, longitude, from.toInstant().toEpochMilli());
    }

    @ApiOperation("Find vessel locations within a circle surrounding a vessel. " +
                  "(CAUTION: Data is unreliable because it is missing some vessels such as fishing boats, boats with the AIS system turned off, " +
                  "boats without the AIS system, vessels outside of the AIS range and so on.)")
    @GetMapping(path = "mmsi/{mmsi}/radius/{radius}/from/{from}", produces = { MEDIA_TYPE_APPLICATION_JSON,
                                                                               MEDIA_TYPE_APPLICATION_GEO_JSON,
                                                                               MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public VesselLocationFeatureCollection vesselLocationsWithingRadiusFromMMSI(
            @ApiParam(value = "Radius of search circle in kilometers (km) using haversine formula.", required = true)
            @PathVariable(value = "radius")
            final double radius,
            @ApiParam(value = "MMSI of the vessel", required = true)
            @PathVariable(value = "mmsi")
            final int mmsi,
            @ApiParam(value = "Return vessel locations received after given time in ISO date time format {yyyy-MM-dd'T'HH:mm:ss.SSSZ} e.g" +
                ". 2016-10-31T06:30:00.000Z.", required = true)
            @PathVariable(value = "from")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            final ZonedDateTime from) {

        return vesselLocationService.findAllowedLocationsWithinRadiusFromMMSI(radius, mmsi, from.toInstant().toEpochMilli());
    }
}
