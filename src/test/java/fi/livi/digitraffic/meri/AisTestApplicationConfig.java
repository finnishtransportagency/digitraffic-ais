package fi.livi.digitraffic.meri;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@EnableTransactionManagement
@EntityScan(basePackages = { "fi.livi.digitraffic.util", "fi.livi.digitraffic.meri"})
@ComponentScan(basePackages = "fi.livi.digitraffic.meri")
public class AisTestApplicationConfig {
}
