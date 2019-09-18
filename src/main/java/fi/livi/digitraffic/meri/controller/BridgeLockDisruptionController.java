package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import fi.livi.digitraffic.meri.domain.GeometryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.domain.bridgelock.BridgeLockDisruption;
import fi.livi.digitraffic.meri.model.bridgelock.BridgeLockDisruptionFeature;
import fi.livi.digitraffic.meri.model.bridgelock.BridgeLockDisruptionFeatureCollection;
import fi.livi.digitraffic.meri.model.bridgelock.BridgeLockDisruptionProperties;
import fi.livi.digitraffic.meri.model.geojson.Polygon;
import fi.livi.digitraffic.meri.service.bridgelock.BridgeLockDisruptionService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(API_V1_BASE_PATH)
@ConditionalOnWebApplication
public class BridgeLockDisruptionController {

    public static final String PATH = "/bridge-lock-disruptions";

    @Autowired
    private BridgeLockDisruptionService service;

    @ApiOperation("Return disruptions for bridges and locks")
    @RequestMapping(method = RequestMethod.GET,
        path = PATH,
        produces = {MEDIA_TYPE_APPLICATION_JSON_UTF8,
            MEDIA_TYPE_APPLICATION_GEO_JSON,
            MEDIA_TYPE_APPLICATION_VND_GEO_JSON})
    @ResponseBody
    public BridgeLockDisruptionFeatureCollection findAll() {
        final List<BridgeLockDisruption> bridgeLockDisruptions = service.findAll();
        return createFeatureCollection(bridgeLockDisruptions);
    }

    private BridgeLockDisruptionFeatureCollection createFeatureCollection(List<BridgeLockDisruption> bridgeLockDisruptions) {
        final BridgeLockDisruptionFeatureCollection fc = new BridgeLockDisruptionFeatureCollection();
        fc.setFeatures(bridgeLockDisruptions.stream()
            .filter(bsd -> bsd.getGeometry() != null)
            .map(bsd -> {
                final BridgeLockDisruptionFeature feature = new BridgeLockDisruptionFeature();

                feature.setGeometry(GeometryUtils.geometryToGeoJsonGeometry(bsd.getGeometry()));

                feature.setProperties(new BridgeLockDisruptionProperties(
                    bsd.getBridgeLockId(),
                    bsd.getBridgeLockTypeId(),
                    bsd.getStartDate(),
                    bsd.getEndDate(),
                    bsd.getDescriptionFi(),
                    bsd.getDescriptionSv(),
                    bsd.getDescriptionEn(),
                    bsd.getAdditionalInformationFi(),
                    bsd.getAdditionalInformationSv(),
                    bsd.getAdditionalInformationEn()
                ));
                return feature;
            }).collect(Collectors.toList()));
        return fc;
    }

}
