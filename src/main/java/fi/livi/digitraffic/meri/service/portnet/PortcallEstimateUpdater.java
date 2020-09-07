package fi.livi.digitraffic.meri.service.portnet;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.digitraffic.meri.portnet.xsd.PortAreaDetails;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallNotification;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.Duration;
import java.time.ZonedDateTime;

import static fi.livi.digitraffic.meri.util.TimeUtil.FINLAND_ZONE;

class PortcallEstimate {

    public final EventType eventType;
    public final ZonedDateTime eventTime;
    public final ZonedDateTime recordTime;
    public final Duration eventTimeConfidenceLower;
    public final Duration eventTimeConfidenceUpper;
    public final String source;
    public final Ship ship;
    public final Location location;

    private PortcallEstimate(final PortCallNotification pcn) {
        PortAreaDetails details = pcn.getPortCallDetails().getPortAreaDetails().get(0); // seems to be always 1
        this.eventType = EventType.ETA;
        this.eventTime = ZonedDateTime.ofInstant(details.getBerthDetails().getEta().toGregorianCalendar().getTime().toInstant(), FINLAND_ZONE);
        this.recordTime = ZonedDateTime.ofInstant(details.getBerthDetails().getEtaTimeStamp().toGregorianCalendar().getTime().toInstant(), FINLAND_ZONE);
        this.eventTimeConfidenceLower = null;
        this.eventTimeConfidenceUpper = null;
        this.source = "DT-" + details.getBerthDetails().getEtaSource().name();
        final BigInteger mmsi = pcn.getPortCallDetails().getVesselDetails().getIdentificationData().getMmsi();
        final BigInteger imo = pcn.getPortCallDetails().getVesselDetails().getIdentificationData().getImoLloyds();
        this.ship = new Ship(mmsi != null ? mmsi.intValue() : null,
            imo != null ? imo.intValue() : null);
        this.location = new Location(pcn.getPortCallDetails().getNextPort());
    }

    public static PortcallEstimate fromPortcallNotification(final PortCallNotification pcn) {
        return new PortcallEstimate(pcn);
    }

    @Override
    public String toString() {
        return "PortcallEstimate{" +
            "eventType=" + eventType +
            ", eventTime=" + eventTime +
            ", recordTime=" + recordTime +
            ", eventTimeConfidenceLower=" + eventTimeConfidenceLower +
            ", eventTimeConfidenceUpper=" + eventTimeConfidenceUpper +
            ", source='" + source + '\'' +
            ", ship=" + ship +
            ", location=" + location +
            '}';
    }
}

enum EventType {
    ETA, ATA, ETD
}

class Ship {
    public final Integer mmsi;
    public final Integer imo;

    public Ship(final Integer mmsi, final Integer imo) {
        this.mmsi = mmsi;
        this.imo = imo;
    }
}

class Location {
    public final String port;

    public Location(final String port) {
        this.port = port;
    }
}

interface PortcallEstimateUpdater {
    void update(PortCallNotification pcn);
}

@ConditionalOnExpression("'${portcallestimate.url}' == 'null'")
@Component
class NoOpPortcallEstimateUpdater implements PortcallEstimateUpdater {

    @Override
    public void update(PortCallNotification pcn) {}
}

@ConditionalOnExpression("'${portcallestimate.url}' != 'null'")
@Component
class HttpPortcallEstimateUpdater implements PortcallEstimateUpdater {

    private final HttpClient httpClient = HttpClients.createDefault();
    private final String portcallEstimateUrl;
    private final ObjectMapper om;

    private static final Logger log = LoggerFactory.getLogger(HttpPortcallEstimateUpdater.class);

    @Autowired
    public HttpPortcallEstimateUpdater(
        @Value("${portcallestimate.url}") final String portcallEstimateUrl,
        final ObjectMapper om
        )
    {
        this.portcallEstimateUrl = portcallEstimateUrl;
        this.om = om;
    }

    @Override
    public void update(final PortCallNotification pcn) {
        try {
            final PortcallEstimate pce = PortcallEstimate.fromPortcallNotification(pcn);
            final HttpPost post = new HttpPost(portcallEstimateUrl);
            post.setEntity(new StringEntity(om.writeValueAsString(pce), ContentType.APPLICATION_JSON));
            httpClient.execute(post);
            log.info("Updated portcall estimate {}", pce);
        } catch (Exception e) {
            log.warn("Unable to update portcall estimates", e);
        }
    }
}
