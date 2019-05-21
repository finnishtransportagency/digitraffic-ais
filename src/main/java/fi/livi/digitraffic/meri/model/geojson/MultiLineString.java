package fi.livi.digitraffic.meri.model.geojson;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "GeoJSON MultiPoint Geometry object", parent = Geometry.class, subTypes = {LineString.class})
public class MultiLineString extends Geometry<List<List<List<Double>>>> {

    public MultiLineString() {
        super(GeometryType.MultiLineString);
    }

    @ApiModelProperty(required = true, allowableValues = "MultiLineString", example = "MultiLineString")
    @Override
    public GeometryType getType() {
        return super.getType();
    }

    public void setCoordinates(List<List<List<Double>>> coordinates) {
        super.setCoordinates(coordinates);
    }
}