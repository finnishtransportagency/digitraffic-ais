package fi.livi.digitraffic.meri.model.bridgelock;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Geometry;

@JsonPropertyOrder({
       "geometry",
       "properties"
})
public class BridgeLockDisruptionFeature extends Feature<Geometry, BridgeLockDisruptionProperties> {
}
