package fi.livi.digitraffic.meri.controller.reader;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.controller.CachedLocker;
import fi.livi.digitraffic.meri.controller.VesselMqttSender;
import fi.livi.digitraffic.meri.controller.ais.AisRadioMsg;

@Component
@ConditionalOnExpression("'${config.test}' != 'true'")
@ConditionalOnProperty("ais.reader.enabled")
public class VesselLoggingListener implements AisMessageListener {
    private static final Logger log = LoggerFactory.getLogger(VesselLoggingListener.class);

    private static final Map<AISLoggingType, SentStatistics> sentStatisticsMap = new ConcurrentHashMap<>();
    private static final Map<AISLoggingType, ReadStatistics> readStatisticsMap = new ConcurrentHashMap<>();

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final CachedLocker aisCachedLocker;
    private final VesselMqttSender vesselSender;
    private final AisMessageReader messageReader;

    public VesselLoggingListener(final CachedLocker aisCachedLocker,
                                 final VesselMqttSender vesselSender,
                                 final AisMessageReader messageReader) {
        this.aisCachedLocker = aisCachedLocker;
        this.vesselSender = vesselSender;
        this.messageReader = messageReader;

        executor.scheduleAtFixedRate(this::logReadStatistics, 30, 60, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(this::logSentStatistics, 30, 60, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(this::readStatus, 30, 10, TimeUnit.SECONDS);
    }

    public enum AISLoggingType {
        POSITION, METADATA, CONNECTION, STATUS
    }

    public enum LoggerType {
        READ, SEND
    }

    @PreDestroy
    public void destroy() {
        executor.shutdown();
    }

    @Override
    public synchronized void receiveMessage(AisRadioMsg message) {
        AISLoggingType type = (message.getMessageType() == AisRadioMsg.MessageType.POSITION) ? AISLoggingType.POSITION : AISLoggingType.METADATA;
        int readMessageCount = message.isMmsiAllowed() ? 1 : 0;
        int filteredMessageCount = !message.isMmsiAllowed() ? 1 : 0;

        final ReadStatistics rs = readStatisticsMap.get(type);

        final ReadStatistics newRs = (rs == null) ?
            new ReadStatistics(readMessageCount, filteredMessageCount) :
            new ReadStatistics(rs.messages + readMessageCount, rs.filtered + filteredMessageCount);

        readStatisticsMap.put(type, newRs);

        int queueSize = messageReader.getMessageQueueSize();

        final ConnectionStatistics cs = (ConnectionStatistics)readStatisticsMap.get(AISLoggingType.CONNECTION);

        final ConnectionStatistics newCs = (cs == null) ?
            new ConnectionStatistics(1, AisTcpSocketClient.ConnectionStatus.CONNECTED, 0, queueSize, queueSize) :
            new ConnectionStatistics(cs.messages + 1, AisTcpSocketClient.ConnectionStatus.CONNECTED, cs.readProblems, cs.messageQueue + queueSize, Math.max(cs.messageQueueMax, queueSize));

        readStatisticsMap.put(AISLoggingType.CONNECTION, newCs);
    }

    public static synchronized void readAisConnectionStatus(final AisTcpSocketClient.ConnectionStatus connectionStatus) {
        int problem = (connectionStatus != AisTcpSocketClient.ConnectionStatus.CONNECTED) ? 1 : 0;

        final ConnectionStatistics cs = (ConnectionStatistics)readStatisticsMap.get(AISLoggingType.CONNECTION);

        final ConnectionStatistics newCs = (cs == null) ?
            new ConnectionStatistics(0, connectionStatus, problem, 0, 0) :
            new ConnectionStatistics(cs.messages, connectionStatus, cs.readProblems + problem, cs.messageQueue, Math.max(cs.messageQueueMax, 0));

        readStatisticsMap.put(AISLoggingType.CONNECTION, newCs);
    }

    public static synchronized void sentAisMessagesStatistics(final AISLoggingType type, final boolean sendSuccessful) {
        int messages = sendSuccessful ? 1 : 0;
        int failures = sendSuccessful ? 0 : 1;

        final SentStatistics ss = sentStatisticsMap.get(type);

        final SentStatistics newSs = (ss == null) ?
            new SentStatistics(messages, failures) :
            new SentStatistics(ss.messages + messages, ss.failures + failures);

        sentStatisticsMap.put(type, newSs);
    }

    private synchronized void logReadStatistics() {
        for (final AISLoggingType aisLoggingType : Arrays.asList(AISLoggingType.POSITION, AISLoggingType.METADATA, AISLoggingType.CONNECTION)) {
            final ReadStatistics readStatistics = readStatisticsMap.get(aisLoggingType);

            if (readStatistics instanceof ConnectionStatistics) {
                final AisTcpSocketClient.ConnectionStatus status = ((ConnectionStatistics)readStatistics).status;

                log.info("Ais-message statistics for loggerType={} messageType={} status={} connectionProblems={} maxQueue={}",
                    LoggerType.READ,
                    aisLoggingType,
                    status,
                    ((ConnectionStatistics)readStatistics).readProblems,
                    ((ConnectionStatistics)readStatistics).messageQueueMax
                    //getAverageQueueSize((ConnectionStatistics)readStatistics)
                );

                readStatisticsMap.put(aisLoggingType, new ConnectionStatistics(0, status, 0, 0,0));
            } else {
                log.info("Ais-message statistics for loggerType={} messageType={} read={} filtered={}",
                    LoggerType.READ,
                    aisLoggingType,
                    readStatistics != null ? readStatistics.messages : 0,
                    readStatistics != null ? readStatistics.filtered : 0
                );

                readStatisticsMap.put(aisLoggingType, new ReadStatistics(0, 0));
            }
        }
    }

    private int getAverageQueueSize(final ConnectionStatistics connectionStatistics) {
        if (connectionStatistics == null || connectionStatistics.messages == 0) {
            return  0;
        }

        return connectionStatistics.messageQueue / connectionStatistics.messages;
    }

    private synchronized void logSentStatistics() {
        for (final AISLoggingType aisMessageType : Arrays.asList(AISLoggingType.POSITION, AISLoggingType.METADATA, AISLoggingType.STATUS)) {
            final SentStatistics sentStatistics = sentStatisticsMap.get(aisMessageType);

            log.info("Ais-message statistics for loggerType={} messageType={} messages={} failures={}",
                LoggerType.SEND,
                aisMessageType,
                sentStatistics != null ? sentStatistics.messages : 0,
                sentStatistics != null ? sentStatistics.failures : 0);

            sentStatisticsMap.put(aisMessageType, new SentStatistics(0, 0));
        }
    }

    private synchronized void readStatus() {
        if (aisCachedLocker.hasLock()) {
            final ConnectionStatistics cs = (ConnectionStatistics)readStatisticsMap.get(AISLoggingType.CONNECTION);

            int errorsCount = 0;

            for (final AISLoggingType aisMessageType : Arrays.asList(AISLoggingType.POSITION, AISLoggingType.METADATA, AISLoggingType.STATUS)) {
                final SentStatistics sentStatistics = sentStatisticsMap.get(aisMessageType);

                if (sentStatistics != null) {
                    errorsCount += sentStatistics.failures;
                }
            }

            final StatusMessage statusMessage = new StatusMessage(
                (cs != null) ? cs.status.toString() : AisTcpSocketClient.ConnectionStatus.UNDEFINED.toString(),
                (cs != null) ? cs.readProblems : 0,
                errorsCount
            );

            final StopWatch sw = StopWatch.createStarted();

            try {
                final boolean sendStatus = vesselSender.sendStatusMessage(statusMessage);

                sentAisMessagesStatistics(AISLoggingType.STATUS, sendStatus);
            } catch (final Exception e) {
                log.error("Json parse error", e);
            }

            //log.info("sendStatus tookMs={}", sw.getTime());
        }
    }

    private static class SentStatistics {
        final int messages;
        final int failures;

        private SentStatistics(int messages, int failures) {
            this.messages = messages;
            this.failures = failures;
        }
    }

    private static class ReadStatistics {
        final int messages;
        final int filtered;

        private ReadStatistics(int messages, int filtered) {
            this.messages = messages;
            this.filtered = filtered;
        }
    }

    private static class ConnectionStatistics extends ReadStatistics {
        final AisTcpSocketClient.ConnectionStatus status;
        final int readProblems;
        final int messageQueue;
        final int messageQueueMax;

        private ConnectionStatistics(int messages, AisTcpSocketClient.ConnectionStatus status, int readProblems, int messageQueue, int messageQueueMax) {
            super(messages, 0);

            this.status = status;
            this.readProblems = readProblems;
            this.messageQueue = messageQueue;
            this.messageQueueMax = messageQueueMax;
        }
    }

    private class StatusMessage {
        private final String connectionStatus;
        private final int readErrors;
        private final int sendErrors;
        private final ZonedDateTime timeStamp;

        private StatusMessage(String connectionStatus, int readErrors, int sendErrors) {
            this.connectionStatus = connectionStatus;
            this.readErrors = readErrors;
            this.sendErrors = sendErrors;

            timeStamp = ZonedDateTime.now();
        }

        public String getStatus() {
            return connectionStatus;
        }

        public ZonedDateTime getUpdateTime() {
            return timeStamp;
        }

        public int getReadErrors() {
            return readErrors;
        }

        public int getSentErrors() {
            return sendErrors;
        }
    }
}
