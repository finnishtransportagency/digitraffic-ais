package fi.livi.digitraffic.meri.model.bridgesluice;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.FeatureCollection;
import io.swagger.annotations.ApiModel;

@JsonPropertyOrder({
                       "type",
                       "dateUpdatedTime",
                       "features",
                   })
@ApiModel(description = "GeoJSON FeatureCollection object")
public class BridgeSluiceDisruptionFeatureCollection extends FeatureCollection<BridgeSluiceDisruptionFeature> {
}
