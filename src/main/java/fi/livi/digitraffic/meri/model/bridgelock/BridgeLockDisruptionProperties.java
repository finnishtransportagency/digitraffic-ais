package fi.livi.digitraffic.meri.model.bridgelock;

import java.time.ZonedDateTime;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.annotations.ApiModelProperty;

public class BridgeLockDisruptionProperties extends Properties {

    @ApiModelProperty(value = "Id of bridge or lock",
                      required = true)
    public final long bridgeLockId;

    @ApiModelProperty(value = "Type of bridge or lock",
                      required = true)
    public final long bridgeLockTypeId;

    @ApiModelProperty(value = "Start date and time of disruption",
                      required = true)
    public final ZonedDateTime startDate;

    @ApiModelProperty(value = "End date and time of disruption",
                      required = true)
    public final ZonedDateTime endDate;

    @ApiModelProperty(value = "Disruption description in finnish",
                      required = false)
    public final String descriptionFinnish;

    @ApiModelProperty(value = "Disruption description in swedish",
                      required = false)
    public final String descriptionSwedish;

    @ApiModelProperty(value = "Disruption description in english",
                      required = false)
    public final String descriptionEnglish;

    @ApiModelProperty(value = "Additional disruption information in finnish",
                      required = false)
    public final String additionalInformationFinnish;

    @ApiModelProperty(value = "Additional disruption information in swedish",
                      required = false)
    public final String additionalInformationSwedish;

    @ApiModelProperty(value = "Additional disruption information in english",
                      required = false)
    public final String additionalInformationEnglish;

    public BridgeLockDisruptionProperties(
        final long bridgeLockId,
        final long bridgeLockTypeId,
        final ZonedDateTime startDate,
        final ZonedDateTime endDate,
        final String descriptionFinnish,
        final String descriptionSwedish,
        final String descriptionEnglish,
        final String additionalInformationFinnish,
        final String additionalInformationSwedish,
        final String additionalInformationEnglish) {
        this.bridgeLockId = bridgeLockId;
        this.bridgeLockTypeId = bridgeLockTypeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.descriptionFinnish = descriptionFinnish;
        this.descriptionSwedish = descriptionSwedish;
        this.descriptionEnglish = descriptionEnglish;
        this.additionalInformationFinnish = additionalInformationFinnish;
        this.additionalInformationSwedish = additionalInformationSwedish;
        this.additionalInformationEnglish = additionalInformationEnglish;
    }
}
