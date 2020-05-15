package fi.livi.digitraffic.meri.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.core.JsonGenerator;
import com.google.common.base.Splitter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import net.logstash.logback.composite.AbstractJsonProvider;

/**
 * Provider to log key=value -pairs in messages as json key-values.
 */
public class LoggerMessageKeyValuePairJsonProvider extends AbstractJsonProvider<ILoggingEvent> {

    private final static Splitter spaceSplitter = Splitter.on(' ').omitEmptyStrings().trimResults();
    private final static Pattern tagsPattern = Pattern.compile("[<][^>]*[>]");

    @Override
    public void writeTo(final JsonGenerator generator, final ILoggingEvent event) {

        final String formattedMessage = event.getFormattedMessage();

        if (StringUtils.isBlank(formattedMessage)) {
            return;
        }

        final List<Pair<String, String>> kvPairs = parseKeyValuePairs(formattedMessage);

        if (kvPairs.isEmpty()) {
            return;
        }

        final Set<String> hasWrittenFieldNames = new HashSet<>();
        kvPairs.forEach(e -> {
            if (!hasWrittenFieldNames.contains(e.getKey())) {
                try {
                    generator.writeObjectField(e.getKey(), e.getValue());
                    hasWrittenFieldNames.add(e.getKey());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private static List<Pair<String, String>> parseKeyValuePairs(final String formattedMessage) {
        final String message = stripXmlTags(formattedMessage);
        return spaceSplitter.splitToList(message)
            .stream()
            .map(kv -> kv.split("=")) // split message chunks by =
            // Filter empty key or value pairs
            .filter(kv -> kv.length > 1 && StringUtils.isNotBlank(kv[0]) && StringUtils.isNotBlank(kv[1]))
            .map(kv -> Pair.of(kv[0], kv[1]))
            .collect(Collectors.toList());
    }

    private static String stripXmlTags(final String message) {
        return tagsPattern.matcher(message).replaceAll(" ");
    }
}
