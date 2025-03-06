package faang.school.notificationservice;

import com.redis.testcontainers.RedisContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
class NotificationServiceAppTests {
    @Test
    void contextLoads() {
        Assertions.assertThat(40 + 2).isEqualTo(42);
    }

    @Container
    public static PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:13.6")
                    .waitingFor(Wait.forListeningPort());

    @Container
    public static RedisContainer REDIS_CONTAINER =
            new RedisContainer("redis/redis-stack:latest")
                    .waitingFor(Wait.forListeningPort());

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        POSTGRES_CONTAINER.start();
        REDIS_CONTAINER.start();
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getRedisHost);
        registry.add("spring.data.redis.port", REDIS_CONTAINER::getRedisPort);
    }
}
