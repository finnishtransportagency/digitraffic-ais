package fi.livi.digitraffic.meri.model.pooki.converter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.livi.digitraffic.meri.config.QuartzSchedulerConfig;

@Component
/**
 * This deserializes times without timezone to zoneddatetimes.  This assumes, that the times
 * without timezone are in fact in Europe/Helsinki timezone.
 */
public class JsonDateTimeDeserializerToZonedDateTime extends JsonDeserializer<ZonedDateTime> {
    private static final Logger log = LoggerFactory.getLogger(JsonDateTimeDeserializerToZonedDateTime.class);

    private final SimpleDateFormat[] DATE_FORMATS =
            new SimpleDateFormat[] { new SimpleDateFormat("d.M.yyyy h:m:s"),
                                     new SimpleDateFormat("d.M.yyyy h:m"),
                                     new SimpleDateFormat("d.M.yyyy") };

    @Override
    public ZonedDateTime deserialize(final JsonParser jp, final DeserializationContext ctxt)
            throws IOException {

        final ObjectCodec oc = jp.getCodec();
        final JsonNode node = oc.readTree(jp);
        final String dateString = node.asText();

        if (StringUtils.isNotBlank(dateString)) {
            log.debug("From {} to {}", dateString, parseDateQuietly(dateString));
        }

        return parseDateQuietly(dateString);
    }

    public ZonedDateTime parseDateQuietly(final String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        for (final SimpleDateFormat dateFormat : DATE_FORMATS)  {
            try {
                dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Helsinki")));
                final Date date = dateFormat.parse(dateTime);
                return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("Europe/Helsinki"));
            } catch (final ParseException e) {
                log.debug("Parse of " + dateTime + " failed", e);
            }
        }
        log.warn("Could not parse dateTime={}", dateTime);

        return null;
    }
}
