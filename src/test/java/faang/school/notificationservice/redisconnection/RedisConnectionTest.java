package faang.school.notificationservice.redisconnection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@SpringBootTest
public class RedisConnectionTest {

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Test
    public void testRedisConnectionPing() {
        try (RedisConnection connection = jedisConnectionFactory.getConnection()) {
            String pingResponse = connection.ping();
            assertEquals("PONG", pingResponse, "Redis should respond with PONG");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to connect to Redis: " + e.getMessage());
        }
    }
}
