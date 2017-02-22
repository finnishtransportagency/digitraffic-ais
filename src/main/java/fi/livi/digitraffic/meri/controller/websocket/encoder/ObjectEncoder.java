package fi.livi.digitraffic.meri.controller.websocket.encoder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectEncoder<T> implements Encoder.Text<T> {
    private static final Logger LOG = LoggerFactory.getLogger(ObjectEncoder.class);

    @Override
    public void init(final EndpointConfig config) {
        // no need
    }

    @Override
    public String encode(final T message) throws EncodeException {
        try {
            return new ObjectMapper().writeValueAsString(message);
        } catch (final JsonProcessingException e) {
            LOG.error("Error when encoding", e);

            return "";
        }
    }

    @Override
    public void destroy() {
        // no need
    }

}