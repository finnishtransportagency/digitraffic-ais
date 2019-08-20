package fi.livi.digitraffic.meri.model.bridgesluice;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Polygon;

@JsonPropertyOrder({
                       "geometry",
                       "properties"
                   })
public class BridgeSluiceDisruptionFeature extends Feature<Polygon, BridgeSluiceDisruptionProperties> {
}
