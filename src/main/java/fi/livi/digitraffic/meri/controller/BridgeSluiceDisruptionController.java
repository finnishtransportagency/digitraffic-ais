package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_GEO_JSON;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_JSON_UTF8;
import static fi.livi.digitraffic.meri.controller.MediaTypes.MEDIA_TYPE_APPLICATION_VND_GEO_JSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.livi.digitraffic.meri.domain.bridgesluice.BridgeSluiceDisruption;
import fi.livi.digitraffic.meri.model.bridgesluice.BridgeSluiceDisruptionFeature;
import fi.livi.digitraffic.meri.model.bridgesluice.BridgeSluiceDisruptionFeatureCollection;
import fi.livi.digitraffic.meri.model.bridgesluice.BridgeSluiceDisruptionProperties;
import fi.livi.digitraffic.meri.model.geojson.Polygon;
import fi.livi.digitraffic.meri.service.bridgesluice.BridgeSluiceDisruptionService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(API_V1_BASE_PATH)
@ConditionalOnWebApplication
public class BridgeSluiceDisruptionController {

    @Autowired
    private BridgeSluiceDisruptionService service;

    @ApiOperation("Return disruptions for bridges and sluices")
    @RequestMapping(method = RequestMethod.GET,
                    path = "/bridge-sluice-disruptions",
                    produces = { MEDIA_TYPE_APPLICATION_JSON_UTF8,
                                 MEDIA_TYPE_APPLICATION_GEO_JSON,
                                 MEDIA_TYPE_APPLICATION_VND_GEO_JSON })
    @ResponseBody
    public BridgeSluiceDisruptionFeatureCollection findAll() {
        final List<BridgeSluiceDisruption> bridgeSluiceDisruptions = service.findAll();
        return createFeatureCollection(bridgeSluiceDisruptions);
    }

    private BridgeSluiceDisruptionFeatureCollection createFeatureCollection(List<BridgeSluiceDisruption> bridgeSluiceDisruptions) {
        final BridgeSluiceDisruptionFeatureCollection fc = new BridgeSluiceDisruptionFeatureCollection();
        fc.setFeatures(bridgeSluiceDisruptions.stream().map(bsd -> {
            final BridgeSluiceDisruptionFeature feature = new BridgeSluiceDisruptionFeature();

            Polygon polygon = new Polygon();
            polygon.setCoordinates(Collections.singletonList(
                bsd.getPolygon().points.stream().map(p -> Arrays.asList(p.longitude, p.latitude)).collect(Collectors.toList())));
            feature.setGeometry(polygon);

            feature.setProperties(new BridgeSluiceDisruptionProperties(
                bsd.getBridgeSluiceId(),
                bsd.getBridgeSluiceTypeId(),
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
