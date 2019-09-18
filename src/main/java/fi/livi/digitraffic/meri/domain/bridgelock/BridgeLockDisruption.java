package fi.livi.digitraffic.meri.domain.bridgelock;

import java.time.ZonedDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import fi.livi.digitraffic.meri.domain.Geometry;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import fi.livi.digitraffic.meri.dao.GeometryUserType;

@Entity
@Access(AccessType.FIELD)
@Table(name = "bridgelock_disruption")
@TypeDef(name = "GeometryUserType",
         typeClass = GeometryUserType.class)
public class BridgeLockDisruption {

    @Id
    @Column(name = "bridgelock_id",
            nullable = false)
    private long bridgeLockId;

    @Column(name = "bridgelock_type_id",
            nullable = false)
    private long bridgeLockTypeId;

    @Column(name = "start_date",
            nullable = false)
    private ZonedDateTime startDate;

    @Column(name = "end_date",
            nullable = false)
    private ZonedDateTime endDate;

    @Column(name = "geometry")
    @Type(type = "GeometryUserType")
    private Geometry geometry;

    @Column(name = "description_fi")
    private String descriptionFi;

    @Column(name = "description_sv")
    private String descriptionSv;

    @Column(name = "description_en")
    private String descriptionEn;

    @Column(name = "additional_info_fi")
    private String additionalInformationFi;

    @Column(name = "additional_info_sv")
    private String additionalInformationSv;

    @Column(name = "additional_info_en")
    private String additionalInformationEn;

    BridgeLockDisruption() {
        // for Hibernate
    }

    public long getBridgeLockId() {
        return bridgeLockId;
    }

    public long getBridgeLockTypeId() {
        return bridgeLockTypeId;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getDescriptionFi() {
        return descriptionFi;
    }

    public String getDescriptionSv() {
        return descriptionSv;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public String getAdditionalInformationFi() {
        return additionalInformationFi;
    }

    public String getAdditionalInformationSv() {
        return additionalInformationSv;
    }

    public String getAdditionalInformationEn() {
        return additionalInformationEn;
    }
}
