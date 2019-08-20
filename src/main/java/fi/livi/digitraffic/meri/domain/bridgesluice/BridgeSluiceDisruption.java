package fi.livi.digitraffic.meri.domain.bridgesluice;

import java.time.ZonedDateTime;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import fi.livi.digitraffic.meri.dao.PolygonUserType;
import fi.livi.digitraffic.meri.domain.Polygon;

@Entity
@Access(AccessType.FIELD)
@Table(name = "bridge_sluice_disruption")
@TypeDef(name = "PolygonUserType",
         typeClass = PolygonUserType.class)
public class BridgeSluiceDisruption {

    @Id
    private long id;

    @Column(name = "bridgesluice_id",
            nullable = false)
    private long bridgeSluiceId;

    @Column(name = "bridgesluice_type_id",
            nullable = false)
    private long bridgeSluiceTypeId;

    @Column(name = "start_date",
            nullable = false)
    private ZonedDateTime startDate;

    @Column(name = "end_date",
            nullable = false)
    private ZonedDateTime endDate;

    @Column(nullable = false)
    @Type(type = "PolygonUserType")
    private Polygon polygon;

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

    BridgeSluiceDisruption() {
        // for Hibernate
    }

    public long getId() {
        return id;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public long getBridgeSluiceId() {
        return bridgeSluiceId;
    }

    public long getBridgeSluiceTypeId() {
        return bridgeSluiceTypeId;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
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
