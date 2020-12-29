package fi.livi.digitraffic.meri;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SecretsPropertiesListener implements ApplicationListener<ApplicationPreparedEvent> {

    @Override
    public void onApplicationEvent(final ApplicationPreparedEvent applicationPreparedEvent) {
        final ConfigurableEnvironment env = applicationPreparedEvent.getApplicationContext().getEnvironment();

        final String secretName = env.getProperty("spring.aws.secretsmanager.secretName");
        final String endpoint = env.getProperty("spring.aws.secretsmanager.endpoint");
        final String region = env.getProperty("spring.aws.secretsmanager.region");

        if (StringUtils.isBlank(secretName) || StringUtils.isBlank(endpoint) || StringUtils.isBlank(region)) {
            return;
        }

        try {
            final String secretString = getSecret(secretName, endpoint, region);

            final ObjectMapper om = new ObjectMapper();
            final JavaType type = om.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
            final HashMap<String, String> secretJson = om.readValue(secretString, type);

            final Properties props = new Properties();

            for (final Map.Entry<String, String> entry : secretJson.entrySet()) {
                props.put(entry.getKey(), entry.getValue());
            }

            env.getPropertySources().addFirst(new PropertiesPropertySource("aws.secrets.manager", props));
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing or writing secret", e);
        }
    }

    private String getSecret(final String secretName, final String endpoint, final String region) {
        final AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
        final AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
        clientBuilder.setEndpointConfiguration(config);
        final AWSSecretsManager client = clientBuilder.build();

        final GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
            .withSecretId(secretName).withVersionStage("AWSCURRENT");
        try {
            final GetSecretValueResult getSecretValueResult = client.getSecretValue(getSecretValueRequest);

            if (getSecretValueResult == null) {
                throw new IllegalArgumentException("Value of secret was null");
            }

            return getSecretValueResult.getSecretString();

        } catch (Exception e) {
            throw new RuntimeException("Error getting secret", e);
        }
    }

}
