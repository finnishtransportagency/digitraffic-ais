package fi.livi.digitraffic.meri.controller.websocket;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.reader.ReconnectingHandler;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
public class WebsocketStatistics {
    private static final Map<WebsocketType, SentStatistics> sentStatisticsMap = new ConcurrentHashMap<>();
    private static final Map<WebsocketType, ReadStatistics> readStatisticsMap = new ConcurrentHashMap<>();

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private static final Logger log = LoggerFactory.getLogger(WebsocketStatistics.class);

    public static final String UNDEFINED = "UNDEFINED";

    public enum WebsocketType {
        LOCATIONS, VESSEL_LOCATION, METADATA
    }

    public WebsocketStatistics(@Value("${ais.websocketRead.enabled}") final boolean websocketReadEnabled) {
        if(websocketReadEnabled) {
            executor.scheduleAtFixedRate(WebsocketStatistics::logReadStatistics, 0, 1, TimeUnit.MINUTES);
        }

        executor.scheduleAtFixedRate(WebsocketStatistics::logSentStatistics, 0, 1, TimeUnit.MINUTES);
    }

    public synchronized ReadStatistics getReadStatistics() {
        return readStatisticsMap.get(WebsocketStatistics.WebsocketType.LOCATIONS);
    }

    private static synchronized void logReadStatistics() {
        for (final WebsocketType websocketType : Arrays.asList(WebsocketType.LOCATIONS, WebsocketType.METADATA)) {
            final ReadStatistics readStatistics = readStatisticsMap.get(websocketType);
            log.info("Read websocket statistics for webSocketType={} messages={} status={}",
                     websocketType, readStatistics != null ? readStatistics.messages : 0, readStatistics != null ? readStatistics.status : UNDEFINED);

            readStatisticsMap.put(websocketType, new ReadStatistics(0, readStatistics != null ? readStatistics.status : UNDEFINED));
        }
    }

    private static synchronized void logSentStatistics() {
        for (final WebsocketType websocketType : Arrays.asList(WebsocketType.LOCATIONS, WebsocketType.METADATA)) {
            final SentStatistics sentStatistics = sentStatisticsMap.get(websocketType);
            log.info("Sent websocket statistics for webSocketType={} messages={} filtered={}", websocketType, sentStatistics != null ?
                sentStatistics.messages : 0, sentStatistics != null ? sentStatistics.filtered : 0);

            sentStatisticsMap.put(websocketType, new SentStatistics(0, 0));
        }
    }

    public static synchronized void sentWebsocketStatistics(final WebsocketType type, final int sent, final int filtered) {
        final SentStatistics sam = sentStatisticsMap.get(type);

        final SentStatistics newSam = new SentStatistics(
            sam == null ? sent : sam.messages + sent, sam == null ? filtered : sam.filtered + filtered);

        sentStatisticsMap.put(type, newSam);
    }

    public static synchronized void readWebsocketStatistics(final WebsocketType type) {
        final ReadStatistics rs = readStatisticsMap.get(type);

        final ReadStatistics newRs = rs == null ? new ReadStatistics(1, UNDEFINED) : new ReadStatistics(rs.messages + 1, rs.status);

        readStatisticsMap.put(type, newRs);
    }

    public static synchronized void readWebsocketStatus(final WebsocketType type, final ReconnectingHandler.ConnectionStatus status) {
        final ReadStatistics rs = readStatisticsMap.get(type);

        final ReadStatistics newRs = new ReadStatistics(rs == null ? 1 : rs.messages, status.name());

        readStatisticsMap.put(type, newRs);
    }

    private static class SentStatistics {
        final int messages;
        final int filtered;

        private SentStatistics(final int messages, final int filtered) {
            this.messages = messages;
            this.filtered = filtered;
        }
    }

    public static class ReadStatistics {
        final int messages;
        final String status;

        private ReadStatistics(final int messages, final String status) {
            this.messages = messages;
            this.status = status;
        }
    }

    @PreDestroy
    public void destroy() {
        executor.shutdown();
    }
}
