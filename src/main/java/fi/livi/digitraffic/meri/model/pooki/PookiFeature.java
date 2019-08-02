
package fi.livi.digitraffic.meri.model.pooki;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Feature;
import fi.livi.digitraffic.meri.model.geojson.Geometry;

@JsonPropertyOrder({
    "type",
    "geometry",
    "properties"
})
public class PookiFeature extends Feature<Geometry, PookiProperties> {

    public PookiFeature() {
    }

    public PookiFeature(PookiProperties properties) {
        super(properties);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}