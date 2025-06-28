package faang.school.notificationservice.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

public class IntegrationTestContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @ServiceConnection
    public static final KafkaContainer CONTAINER =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"))
                    .withReuse(false)
                    .withEnv("KAFKA_AUTO_CREATE_TOPICS_ENABLE", "false");

    static {
        CONTAINER.setPortBindings(List.of("9092:9093"));
        CONTAINER.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
    }
}
